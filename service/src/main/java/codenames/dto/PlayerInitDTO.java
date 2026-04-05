package codenames.dto;

public class PlayerInitDTO {
    private String username;
    private String email;
    private String password; // only used for creating new player, not stored in db

    public PlayerInitDTO(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
