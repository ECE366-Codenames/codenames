package codenames.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "game")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private String status;

    @Column(name = "winner_team")
    private Integer winnerTeam;

    public Game() {}

    public Game(String status) {
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() { return id; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getStatus() { return status; }
    public Integer getWinnerTeam() { return winnerTeam; }

    public void setStatus(String status) { this.status = status; }
    public void setWinnerTeam(Integer winnerTeam) { this.winnerTeam = winnerTeam; }
}
