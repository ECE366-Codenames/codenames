package codenames.dto;

public class PlayerDTO {
    private long id;
    private String username;
    private String email;
    private int wins;
    private int losses;
    private boolean isOnline;
    private String password; // only used for creating new player, not stored in db

    public PlayerDTO(long id, String username, String email, String passwordHash, int wins, int losses, Boolean isOnline, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.wins = wins;
        this.losses = losses;
        this.isOnline = isOnline;
        this.password = password;
    }

    public long getId() {
        return id;
    }
    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public Boolean isOnline() {
        return isOnline;
    }

    public String getPassword() {
        return password;
    }

}
