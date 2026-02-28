package codenames.game;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
public class CodenamesGame {

    private final WordRepository wordRepo;

    public CodenamesGame(WordRepository wordRepo) {
        this.wordRepo = wordRepo;
    }

    public  void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("""
                \nMENU:
                   1: Start New Game
                   2: History
                  \s
                Enter option:\s""");

        int option = scanner.nextInt();

        if (option == 1) {
            playGame();
        }
        else if (option == 2) {
            showHistory();
        }
    }
    private void playGame() {
        List<Word> words = wordRepo.findAll();

        System.out.println("Total words in DB: " + words.size());

        words.stream().limit(10).forEach(w -> System.out.println(w.getWord()));
    }

    private void showHistory() {

    }
}