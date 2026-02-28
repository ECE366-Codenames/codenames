package codenames.game;

import java.util.Scanner;

public class Codenames {
    public static void main(String[] args) {
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
    private static void playGame() {

    }

    private static void showHistory() {

    }
}