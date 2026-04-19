package codenames.dto;

public class CardDTO {
    private String word;
    private String color; // null if not spymaster (RED, BLUE, NEUTRAL, ASSASSIN)
    private boolean revealed;
    private int position;

    public CardDTO(String word, boolean revealed, String color, int position) {
        this.word = word;
        this.revealed = revealed;
        this.color = color;
        this.position = position;
    }

    public String getWord() {
        return word;
    }

    public String getColor() {
        return color;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public int getPosition() {
        return position;
    }
}
