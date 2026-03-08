package codenames.dto;

public class CardDTO {
    private String word;
    private String type; // null if not spymaster
    private boolean revealed;
    private int position;

    public CardDTO(String word, boolean revealed, String type, int position) {
        this.word = word;
        this.revealed = revealed;
        this.type = type;
        this.position = position;
    }

    public String getWord() {
        return word;
    }

    public String getType() {
        return type;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public int getPosition() {return position;}
}
