package codenames.dto;

public class ClueDTO {
    private String word;
    private int number;

    public ClueDTO(String word, int number) {
        this.word = word;
        this.number = number;
    }

    public String getWord() {return word;}
    public int getNumber() {return number;}

    public void setWord(String word) {this.word = word;}
    public void setNumber(int number) {this.number = number;}
}
