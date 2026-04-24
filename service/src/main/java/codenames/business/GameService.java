package codenames.business;

import codenames.dto.CardDTO;
import codenames.dto.GameDTO;
import codenames.model.*;
import codenames.repository.GameCardRepository;
import codenames.repository.GameRepository;
import codenames.repository.WordRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import codenames.repository.GamePlayerRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final WordRepository wordRepository;
    private final GameCardRepository gameCardRepository;
    private final GamePlayerRepository gamePlayerRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public GameService(GameRepository gameRepository, WordRepository wordRepository, GameCardRepository gameCardRepository, GamePlayerRepository gamePlayerRepository, SimpMessagingTemplate messagingTemplate) {
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
        this.gameCardRepository = gameCardRepository;
        this.gamePlayerRepository = gamePlayerRepository;
        this.messagingTemplate = messagingTemplate;
    }

    public Long createGame() {
        Game game = new Game(Status.WAITING);
        Game savedGame = gameRepository.save(game);
        return savedGame.getId();
    }

    @Transactional
    public Long startGame(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        if (game.getStatus() != Status.WAITING) {
            throw new RuntimeException("Game already started");
        }

        List<GamePlayer> players = gamePlayerRepository.findByGame(game);
        if (players.size() != 4) {
            throw new IllegalStateException("Not enough players in game");
        }

        for (int i = 0; i < players.size(); i++) {
            GamePlayer gp = players.get(i);

            if (i < 2) { 
                gp.setRed(true);          
                gp.setSpymaster(i == 0);  
            } else {
                gp.setRed(false);          
                gp.setSpymaster(i == 2);  
            }
        }

        game.setStatus(Status.STARTED);
        game.setRedTurn(true);

        List<Word> words = wordRepository.getRandomWords(25);
        List<CardType> cardTypes = assignCardTypes();

        List<GameCard> cards = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            GameCard card = new GameCard(game, words.get(i), cardTypes.get(i), i);
            cards.add(card);
        }

        game.setCards(cards);

        notifyGameUpdate(gameId);

        return game.getId();
    }

    public Game getGameById(Long id) {
        return gameRepository.findById(id).orElseThrow(() -> new RuntimeException("Game not found"));
    }

    private List<CardType> assignCardTypes() {
        List<CardType> cardTypes = new ArrayList<>();

        for (int i = 0; i < 9; i++) cardTypes.add(CardType.RED);
        for (int i = 0; i < 8; i++) cardTypes.add(CardType.BLUE);
        for (int i = 0; i < 7; i++) cardTypes.add(CardType.NEUTRAL);
        cardTypes.add(CardType.ASSASSIN);

        Collections.shuffle(cardTypes);
        return cardTypes;
    }

    @Transactional
    public void guess(Long gameId, int position, String playerId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));

        if(game.getStatus() != Status.STARTED){
            throw new RuntimeException("Game not started");
        }

        if(game.getTurnPhase() != TurnPhase.GUESS){
            throw new RuntimeException("Not guessing phase");
        }

        //make sure that the player making the guess is the guesser on the currently-going team
        GamePlayer currentPlayer = gamePlayerRepository.findByGameAndPlayer_Id(game, playerId)
                .orElseThrow(() -> new RuntimeException("Player not in game"));
        
        if (currentPlayer.isSpymaster() || currentPlayer.isRed() != game.getRedTurn()) {
            throw new RuntimeException("You cannot make a guess at this time");
        }

        GameCard card = gameCardRepository.findByGameIdAndPosition(gameId, position).orElseThrow(() -> new RuntimeException("Card not found"));

        if (card.isRevealed()) {
            throw new RuntimeException("Card already revealed");
        }

        card.setRevealed(true);
        game.setGuessesRemaining(game.getGuessesRemaining() - 1);

        boolean endTurn = false;

        switch (card.getCardType()) {
            case ASSASSIN:
                game.setRedWin(!game.getRedTurn());
                game.setStatus(Status.COMPLETE);
                return;
            case NEUTRAL:
                endTurn = true;
                break;
            case RED:
                if (!game.getRedTurn()) {
                    // Red team guessed blue, switch turns
                    endTurn = true;
                }
                break;
            case BLUE:
                if (game.getRedTurn()) {
                    // Blue team guessed red, switch turns
                    endTurn = true;
                }
                break;

        }

        checkWinCondition(game);

        if (endTurn || game.getGuessesRemaining() == 0) {
            game.setRedTurn(!game.getRedTurn());
            game.setTurnPhase(TurnPhase.CLUE);
            game.setClueWord(null);
            game.setClueNumber(0);
            game.setGuessesRemaining(0);
        }

        notifyGameUpdate(gameId);

    }

    @Transactional
    public void passTurn(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));

        game.setRedTurn(!game.getRedTurn());
        game.setTurnPhase(TurnPhase.CLUE);
        game.setClueWord(null);
        game.setClueNumber(0);
        game.setGuessesRemaining(0);

        notifyGameUpdate(gameId);
    }

    private void checkWinCondition(Game game) {
        long redRemaining = game.getCards().stream().filter(c -> c.getCardType() == CardType.RED && !c.isRevealed()).count();
        long blueRemaining = game.getCards().stream().filter(c -> c.getCardType() == CardType.BLUE && !c.isRevealed()).count();

        if (redRemaining == 0) {
            game.setStatus(Status.COMPLETE);
            game.setRedWin(true);
        } else if (blueRemaining == 0) {
            game.setStatus(Status.COMPLETE);
            game.setRedWin(false);
        }
    }

    @Transactional
    public void submitClue(Long gameId, String clueWord, int clueNumber) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));

        if(game.getTurnPhase() != TurnPhase.CLUE){
            throw new RuntimeException("Not in clue phase");
        }
        game.setClueWord(clueWord);
        game.setClueNumber(clueNumber);
        game.setGuessesRemaining(clueNumber + 1);
        game.setTurnPhase(TurnPhase.GUESS);

        notifyGameUpdate(gameId);
    }

    @Transactional
    public void abort(Long gameId){
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        if(game.getStatus() == Status.WAITING){
            game.setStatus(Status.ABORTED);
            return;
        }
        if(game.getStatus() != Status.STARTED){
            throw new RuntimeException("Game already ended");
        }
        gameCardRepository.deleteByGameId(gameId);
        game.setStatus(Status.ABORTED);
    }

    @Transactional
    public void cleanup(Long gameId){ // delete game cards from db after game successfully completed
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        if(game.getStatus() != Status.COMPLETE){
            throw new RuntimeException("Game not complete");
        }
        gameCardRepository.deleteByGameId(gameId);
    }

    public GameDTO toDTO(Game game, boolean isSpymaster) {
        List<CardDTO> cardsDTOs = game.getCards().stream().map(card -> new CardDTO(
                //if card is revealed, then set the word to null, since the card is now "flipped over"
                //i.e. since the card is flipped, we no longer need the word, only the color
                card.isRevealed() ? null : card.getWord().toString(),
                card.isRevealed(),
                //only give card type to spymaster, or if card is revealed
                isSpymaster || card.isRevealed() ? card.getCardType().toString() : null,
                card.getPosition()
        )).toList();

        return new GameDTO(
                game.getId(),
                game.getStatus(),
                game.getRedTurn(),
                game.getRedWin(),
                cardsDTOs,
                game.getTurnPhase(),
                game.getClueWord(),
                game.getClueNumber(),
                game.getGuessesRemaining()
        );
    }

    private void notifyGameUpdate(Long gameId) {
        Game game = gameRepository.findById(gameId).orElseThrow();
        messagingTemplate.convertAndSend("/topic/game/" + gameId, toDTO(game, false));
    }
}


