package codenames.dto;

public class CardDTO {
    private String word;
    private String type; // null if not spymaster
    private boolean revealed;

    public CardDTO(String word, String type, boolean revealed) {
        this.word = word;
        this.type = type;
        this.revealed = revealed;
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
}
