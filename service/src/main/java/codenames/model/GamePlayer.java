package codenames.model;

import jakarta.persistence.*;

@Entity
@Table(name = "game_players")
public class GamePlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @ManyToOne
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(name = "is_red")
    private boolean isRed;

    @Column(name = "is_spymaster")
    private boolean isSpymaster;

    public GamePlayer() {}

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    public Long getId() { return id; }

    public Game getGame() { return game; }
    public void setGame(Game game) { this.game = game; }

    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }

    public boolean isRed() { return isRed; }
    public void setRed(boolean red) { isRed = red; }

    public boolean isSpymaster() { return isSpymaster; }
    public void setSpymaster(boolean spymaster) { isSpymaster = spymaster; }
}
