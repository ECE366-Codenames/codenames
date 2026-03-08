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

    private String status;

    @Column(name = "red_turn")
    private Boolean redTurn;

    @Column(name = "red_win")
    private Boolean redWin;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    private List<GameCard> cards;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }

    public Game() {}

    public Game(String status) {
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public Boolean getRedWin() { return redWin; }
    public Boolean getRedTurn() { return redTurn; }
    public List<GameCard> getCards() { return cards; }

    public void setStatus(String status) { this.status = status; }
    public void setRedTurn(Boolean redTurn) { this.redTurn = redTurn; }
    public void setRedWin(Boolean redWin) { this.redWin = redWin; }
    public void setCards(List<GameCard> cards) { this.cards = cards; }
}
