package codenames.business;

import codenames.model.Word;
import codenames.repository.GameRepository;
import codenames.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    private final GameRepository gameRepository;
    private final WordRepository wordRepository;
    public GameService(GameRepository gameRepository, WordRepository wordRepository) {
        this.gameRepository = gameRepository;
        this.wordRepository = wordRepository;
    }

    public String createGame() {
        List<Word> words = wordRepository.getRandomWords(25);
        System.out.println(words);
        return "game created";
    }

}
