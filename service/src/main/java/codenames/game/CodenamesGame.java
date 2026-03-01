package codenames.game;

import codenames.model.CardType;
import codenames.model.Word;
import codenames.repository.WordRepository;
import codenames.service.GameService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

@Component
public class CodenamesGame {

    private final WordRepository wordRepo;

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String ASSASSIN = "\u001B[35m";
    private final GameService gameService;

    private static class LocalCard {
        Word word;
        CardType cardType;
        boolean revealed = false;

        LocalCard(Word word, CardType cardType) {
            this.word = word;
            this.cardType = cardType;
        }
    }

    public CodenamesGame(WordRepository wordRepo, GameService gameService) {
        this.wordRepo = wordRepo;
        this.gameService = gameService;
    }

    public void start() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("""
                \nMENU:
                   1: Start New Game
                   2: History
                   3: Rules
                  \s
                Enter option:\s""");

        int option = scanner.nextInt();

        if (option == 1) {
            playGame();
        }
        else if (option == 2) {
            showHistory();
        }
        else if (option == 3) {
            showRules();
        }
    }
    private void playGame() {

        Scanner scanner = new Scanner(System.in);

        List<Word> boardWords = wordRepo.getRandomWords(25);
        List<CardType> cardTypes = gameService.assignCardTypes();

        List<LocalCard> board = new ArrayList<>();

        for (int i = 0; i < boardWords.size(); i++) {
            board.add(new LocalCard(boardWords.get(i), cardTypes.get(i)));
        }

        System.out.println("\n----- SPYMASTER -----");
        printBoard(board, true);

        System.out.print("\nEnter clue: ");
        String clue = scanner.nextLine();
        System.out.print("\nEnter number of cards: ");
        int numCards = scanner.nextInt();

        for (int i = 0; i < 20; i++) {System.out.println();} // clear screen

        System.out.println("\n----- FIELD AGENT -----");

        int redRemaining = 9;
        int blueRemaining = 8;

        printBoard(board, false);

    }

    private void showHistory() {
        System.out.println("History coming soon...");
    }

    private void showRules() {
        System.out.println("Rules coming soon...");
    }

    private void printBoard(List<LocalCard> board, boolean spymasterView) {
        for (int i = 0; i < board.size(); i++) {
            LocalCard card = board.get(i);

            String displayWord = card.word.getWord();

            if (card.revealed || spymasterView) {
                switch (card.cardType) {
                    case RED -> displayWord = RED + displayWord + RESET;
                    case BLUE -> displayWord = BLUE + displayWord + RESET;
                    case ASSASSIN -> displayWord = ASSASSIN + displayWord + RESET;
                    case NEUTRAL -> {}
                }
            }

            System.out.printf("%d: %-15s", i + 1, displayWord);
            if ((i + 1) % 5 == 0) {System.out.println();}
        }
    }

}