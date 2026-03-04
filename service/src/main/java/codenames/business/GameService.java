package codenames.business;

import codenames.model.CardType;
import codenames.model.Game;
import codenames.model.GameCard;
import codenames.model.Word;
import codenames.repository.GameCardRepository;
import codenames.repository.GameRepository;
import codenames.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final WordRepository wordRepository;
    private final GameCardRepository gameCardRepository;

    public GameService(GameRepository gameRepository, WordRepository wordRepository, GameCardRepository gameCardRepository) {
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
        this.gameCardRepository = gameCardRepository;
    }

    public Long createGame() {

        Game game = new Game("CREATED");
        game.setRedTurn(true);

        List<Word> words = wordRepository.getRandomWords(25);
        List<CardType> cardTypes = assignCardTypes();

        List<GameCard> cards = new ArrayList<>();

        for (int i = 0; i < words.size(); i++) {
            GameCard card = new GameCard(game, words.get(i), cardTypes.get(i), i+1);
            cards.add(card);
        }

        game.setCards(cards);

        Game savedGame = gameRepository.save(game);

        return savedGame.getId();

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
    public void guess(Long gameId, int position) {
        Game game = gameRepository.findById(gameId).orElseThrow(() -> new RuntimeException("Game not found"));
        GameCard card = gameCardRepository.findByGameIdAndPosition(gameId, position).orElseThrow(() -> new RuntimeException("Card not found"));

        if (card.isRevealed()) {
            throw new RuntimeException("Card already revealed");
        }

        card.setRevealed(true);

        switch (card.getCardType()) {
            case ASSASSIN:
                game.setRedWin(!game.getRedTurn());
                game.setStatus("RED WIN");
                break;
            case NEUTRAL:
                game.setRedTurn(!game.getRedTurn());
                break;
            case RED:
                if (!game.getRedTurn()) {
                    game.setRedTurn(true);
                }
                break;
            case BLUE:
                if (game.getRedTurn()) {
                    game.setRedTurn(false);
                }
                break;

        }

        checkWinCondition(game);

    }

    private void checkWinCondition(Game game) {
        long redRemaining = game.getCards().stream().filter(c -> c.getCardType() == CardType.RED && !c.isRevealed()).count();
        long blueRemaining = game.getCards().stream().filter(c -> c.getCardType() == CardType.BLUE && !c.isRevealed()).count();

        if (redRemaining == 0) {
            game.setStatus("RED WIN");
            game.setRedWin(true);
        } else if (blueRemaining == 0) {
            game.setStatus("BLUE WIN");
            game.setRedWin(false);
        }
    }

}


