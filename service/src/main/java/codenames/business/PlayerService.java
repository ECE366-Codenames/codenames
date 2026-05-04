package codenames.business;

import codenames.dto.GamePlayerDTO;
import codenames.dto.PlayerDTO;
import codenames.dto.PlayerInitDTO;
import codenames.model.Player;
import codenames.model.GamePlayer;
import codenames.model.Game;
import codenames.model.Status;
import codenames.repository.GamePlayerRepository;
import codenames.repository.PlayerRepository;
import codenames.repository.GameRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final GameService gameService;

    public PlayerService(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository, SimpMessagingTemplate messagingTemplate, GameService gameService) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.messagingTemplate = messagingTemplate;
        this.gameService = gameService;
    }

    @Transactional
    public String createOrGetPlayer(String firebaseUid, String email, String username) {
        //check if player already exists
        return playerRepository.findById(firebaseUid)
                .map(Player::getId)
                .orElseGet(() -> {
                    //create new player with Firebase uid as id
                    Player player = new Player();
                    player.setId(firebaseUid);
                    player.setEmail(email);
                    player.setUsername(username != null ? username : email.split("@")[0]);
                    player.setWins(0);
                    player.setLosses(0);
                    player.setIsOnline(true);

                    return playerRepository.save(player).getId();
                });
    }

    @Transactional
    public String addPlayerToGame(Long gameId, String playerId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        if (gamePlayerRepository.existsByGameAndPlayer(game, player)) {
            return playerId;
        }

        if (game.getStatus() != codenames.model.Status.WAITING) {
            throw new IllegalStateException("Game has already started");
        }

        List<GamePlayer> players = gamePlayerRepository.findByGame(game);

        if (players.size() >= 4) {
            throw new IllegalStateException("Game is full");
        }

        GamePlayer gp = new GamePlayer(game, player);
        gamePlayerRepository.save(gp);

        notifyLobbyUpdate(gameId, "player-joined"); //to get updates on other players screens
        return playerId;
    }

    @Transactional(readOnly = true)
    public Player getPlayerById(String id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

    public List<GamePlayerDTO> getPlayersByGameId(Long gameId){
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        List<GamePlayer> gamePlayers = gamePlayerRepository.findByGame(game);

        return gamePlayers.stream()
                .map(gp -> new GamePlayerDTO(
                        gp.getPlayer().getId(),
                        gp.getPlayer().getUsername(),
                        gp.isRed(),
                        gp.isSpymaster()
                ))
                .toList();
    }

    @Transactional
    public void removePlayerFromGame(Long gameId, String playerId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new RuntimeException("Player not found"));

        GamePlayer gp = gamePlayerRepository.findByGameAndPlayer(game, player)
                .orElseThrow(() -> new RuntimeException("Player not in game"));

        gamePlayerRepository.delete(gp);
        if (game.getStatus() == Status.STARTED) { //if player leaves game, game aborts
            gameService.abort(gameId);
        }
        notifyLobbyUpdate(gameId, "player-left");
    }

    private void notifyLobbyUpdate(Long gameId, String message) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    messagingTemplate.convertAndSend("/topic/game/" + gameId, message);
                }
            });
        } else {
            messagingTemplate.convertAndSend("/topic/game/" + gameId, message);
        }
    }

}
