package codenames.dto;

public class PlayerDTO {
    private long id;
    private String username;
    private String email;
    private String password_hash;
    private int wins;
    private int losses;
    private boolean is_online;

    public PlayerDTO(long id, String username, String email, String password_hash, int wins, int losses, Boolean is_online) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password_hash = password_hash;
        this.wins = wins;
        this.losses = losses;
        this.is_online = is_online;
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

    public String getPassword_hash() {
        return password_hash;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }

    public Boolean isOnline() {
        return is_online;
    }

}
