package codenames.model;

import jakarta.persistence.*;

@Entity
@Table(name = "word")
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String word;

    public Word() {}
    public Word(String word) {
        this.word = word;
    }

    public String getWord() { return word; }

    public void setWord(String word) { this.word = word; }
}
