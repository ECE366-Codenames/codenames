package codenames.business;

import codenames.dto.PlayerDTO;
import codenames.model.Player;
import codenames.model.GamePlayer;
import codenames.model.Game;
import codenames.repository.GamePlayerRepository;
import codenames.repository.PlayerRepository;
import codenames.repository.GameRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final GameRepository gameRepository;
    private final GamePlayerRepository gamePlayerRepository;

    public PlayerService(PlayerRepository playerRepository, GameRepository gameRepository, GamePlayerRepository gamePlayerRepository) {
        this.playerRepository = playerRepository;
        this.gameRepository = gameRepository;
        this.gamePlayerRepository = gamePlayerRepository;
    }

    @Transactional
    public Long createPlayer(PlayerDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        Player player = new Player();
        player.setUsername(dto.getUsername());
        player.setEmail(dto.getEmail());
        
        String hashed = BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt());
        player.setPasswordHash(hashed);

        // initialize defaults
        player.setWins(0);
        player.setLosses(0);
        player.setIsOnline(false);

        return playerRepository.save(player).getId();
    }

    @Transactional
    public Long addPlayerToGame(Long gameId, Long playerId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found"));
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));

        if (gamePlayerRepository.existsByGameAndPlayer(game, player)) {
            throw new IllegalArgumentException("Player already in game");
        }

        List<GamePlayer> players = gamePlayerRepository.findByGame(game);

        if (players.size() >= 4) {
            throw new IllegalStateException("Game is full");
        }

        GamePlayer gp = new GamePlayer(game, player);
        return gamePlayerRepository.save(gp).getId();
    }

    @Transactional(readOnly = true)
    public Player getPlayerById(Long id) {
        return playerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Player not found"));
    }

}
