package codenames.business;

import codenames.model.CardType;
import codenames.model.Game;
import codenames.model.GameCard;
import codenames.model.Word;
import codenames.repository.GameRepository;
import codenames.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class GameService {

    private final GameRepository gameRepository;
    private final WordRepository wordRepository;

    public GameService(GameRepository gameRepository, WordRepository wordRepository) {
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
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

        Game savedGame = gameRepository.save(game);

        return savedGame.getId();


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

}
