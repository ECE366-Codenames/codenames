package codenames.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "red_turn")
    private Boolean redTurn;

    @Column(name = "red_win")
    private Boolean redWin;

    @Enumerated(EnumType.STRING)
    @Column(name = "turn_phase")
    private TurnPhase turnPhase;

    @Column(name = "clue_word")
    private String clueWord;

    @Column(name = "clue_number")
    private int clueNumber;

    @Column(name = "guesses_remaining")
    private int guessesRemaining;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameCard> cards;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Game() {}

    public Game(Status status) {
        this.status = status;
        this.createdAt = LocalDateTime.now();
        this.redTurn = true;
        this.turnPhase = TurnPhase.CLUE;
    }

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public Status getStatus() { return status; }
    public Boolean getRedWin() { return redWin; }
    public Boolean getRedTurn() { return redTurn; }
    public List<GameCard> getCards() { return cards; }

    public void setStatus(Status status) { this.status = status; }
    public void setRedTurn(Boolean redTurn) { this.redTurn = redTurn; }
    public void setRedWin(Boolean redWin) { this.redWin = redWin; }
    public void setCards(List<GameCard> cards) { this.cards = cards; }
}
