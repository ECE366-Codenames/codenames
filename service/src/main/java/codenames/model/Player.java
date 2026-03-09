package codenames.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "password_hash")
    private String password_hash;

    @Column(name = "wins")
    private int wins;

    @Column(name = "losses")
    private int losses;

    @Column(name = "is_online")
    private Boolean is_online;

    public Player() {}

    public Player(String username) {
        this.username = username;
    }

    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword_hash() { return password_hash; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public Boolean getIs_online() { return is_online; }

    public void setWins(int wins) { this.wins = wins; }
    public void setRedTurn(int losses) { this.losses = losses; }
    public void setIs_online(Boolean is_online) { this.is_online = is_online; }
}
