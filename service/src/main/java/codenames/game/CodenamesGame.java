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
        List<Word> boardWords = wordRepo.getRandomWords(25);

        for (int i = 0; i < boardWords.size(); i++) {
            System.out.printf("%d: %-15s", i+1, boardWords.get(i).getWord());

            if ((i + 1) % 5 == 0) {System.out.println();}
        }
    }

    private void showHistory() {
        System.out.println("History coming soon...");
    }
}