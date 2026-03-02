package codenames.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_cards")
public class GameCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToOne
    @JoinColumn(name = "word")
    private Word word;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private boolean revealed = false;

    private int position;

    public GameCard() {}

    public GameCard(Game game, Word word, CardType cardType, int position) {
        this.game = game;
        this.word = word;
        this.cardType = cardType;
        this.position = position;
        this.revealed = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        this.word = word;
    }

    public CardType getCardType() {
        return cardType;
    }

    public void setCardType(CardType cardType) {
        this.cardType = cardType;
    }

    public boolean isRevealed() {
        return revealed;
    }

    public void setRevealed(boolean revealed) {
        this.revealed = revealed;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}
