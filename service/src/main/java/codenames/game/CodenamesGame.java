package codenames.game;

import codenames.model.CardType;
import codenames.model.Word;
import codenames.repository.WordRepository;
import codenames.service.GameService;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class CodenamesGame {

    private final WordRepository wordRepo;

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String BLUE = "\u001B[34m";
    private static final String ASSASSIN = "\u001B[35m";
    private static final String BG_WHITE = "\u001B[47m";
    private static final String FG_BLACK = "\u001B[30m";
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

        boolean redTurn = true;
        int redRemaining = 9;
        int blueRemaining = 8;

        Scanner scanner = new Scanner(System.in);

        List<Word> boardWords = wordRepo.getRandomWords(25);
        List<CardType> cardTypes = gameService.assignCardTypes();

        List<LocalCard> board = new ArrayList<>();

        for (int i = 0; i < boardWords.size(); i++) {
            board.add(new LocalCard(boardWords.get(i), cardTypes.get(i)));
        }

        while (true) {
            System.out.println("\n----- SPYMASTER -----");
            printBoard(board, true);

            System.out.print("\nEnter clue: ");
            String clue = scanner.nextLine();
            System.out.print("\nEnter number of cards: ");
            int numCards = scanner.nextInt();

            for (int i = 0; i < 20; i++) {System.out.println();} // clear screen

            System.out.println("\n----- FIELD AGENT -----");

            int turn = 1;
            while (turn <= numCards) {

                System.out.println("Your clue: " + clue);
                System.out.println("Guesses remaining: " + (numCards - turn + 1));
                printBoard(board, false);

                System.out.print("\nChoose a card number (1-25, 0 to end turn): ");
                int choice = scanner.nextInt();
                if (choice == 0) {break;}
                choice--;

                for (int i = 0; i < 20; i++) {
                    System.out.println();
                } // clear screen

                if (choice < -1 || choice > board.size() - 1) {
                    System.out.println("Invalid choice. Try again.");
                    continue;
                }

                LocalCard selected = board.get(choice);

                if (selected.revealed) {
                    System.out.println("This card has already been revealed. Try again.");
                    continue;
                }

                selected.revealed = true;

                switch (selected.cardType) {
                    case RED -> {
                        redRemaining--;
                        turn++;
                        System.out.println(RED + "RED AGENT!" + RESET);
                    }
                    case BLUE -> {
                        blueRemaining--;
                        turn++;
                        System.out.println(BLUE + "BLUE AGENT!" + RESET);
                    }
                    case NEUTRAL -> {
                        turn++;
                        System.out.println("NEUTRAL AGENT!");
                    }
                    case ASSASSIN -> {
                        turn = numCards;
                        System.out.println(ASSASSIN + "ASSASSIN! GAME OVER!" + RESET);
                        printBoard(board, true);
                        System.out.println("Game complete. Press enter to go home...");
                        scanner.nextLine();
                        scanner.nextLine();
                        start();
                    }
                }

                if (redRemaining == 0) {
                    System.out.println(RED + "RED AGENT WON!" + RESET);
                    System.out.println("Game complete. Press enter to go home...");
                    scanner.nextLine();
                    scanner.nextLine();
                    start();
                }
                if (blueRemaining == 0) {
                    System.out.println(BLUE + "BLUE AGENT WON!" + RESET);
                    System.out.println("Game complete. Press enter to go home...");
                    scanner.nextLine();
                    scanner.nextLine();
                    start();
                }

            }
            printBoard(board, false);
            System.out.println("Turn complete. Press enter to continue...");
            scanner.nextLine();
            scanner.nextLine();
            for (int i = 0; i < 20; i++) {System.out.println();} // clear screen
            redTurn = !redTurn;
        }
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

            if (card.revealed) {
                displayWord = BG_WHITE + displayWord + RESET;
                if (card.cardType == CardType.NEUTRAL) {
                    displayWord = FG_BLACK + displayWord + RESET;
                }
            }

            if (card.revealed || spymasterView) {
                switch (card.cardType) {
                    case RED -> displayWord = RED + displayWord + RESET + "    ";
                    case BLUE -> displayWord = BLUE + displayWord + RESET + "    ";
                    case ASSASSIN -> displayWord = ASSASSIN + displayWord + RESET + "    ";
                    case NEUTRAL -> displayWord = displayWord + "    ";
                }
            }

            System.out.printf("%d: %-15s", i + 1, displayWord);
            if ((i + 1) % 5 == 0) {System.out.println();}
        }
    }

}