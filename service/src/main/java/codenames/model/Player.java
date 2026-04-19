package codenames.model;

import jakarta.persistence.*;

@Entity
@Table(name = "player")
public class Player {
    @Id
    private String id;

    @Column(name = "username")
    private String username;

    @Column(name = "email")
    private String email;

    @Column(name = "wins")
    private int wins;

    @Column(name = "losses")
    private int losses;

    @Column(name = "is_online")
    private Boolean isOnline;

    public Player() {}

    public Player(String id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    public String getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public int getWins() { return wins; }
    public int getLosses() { return losses; }
    public Boolean getIsOnline() { return isOnline; }

    public void setId(String id) { this.id = id; }
    public void setWins(int wins) { this.wins = wins; }
    public void setLosses(int losses) { this.losses = losses; }
    public void setUsername(String username) { this.username = username; }
    public void setEmail(String email) { this.email = email; }
    public void setIsOnline(Boolean isOnline) { this.isOnline = isOnline; }
}
