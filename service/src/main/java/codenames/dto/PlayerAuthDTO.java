package codenames.dto;

public class PlayerAuthDTO {
    private String firebaseUid;
    private String email;
    private String username;

    public String getFirebaseUid() { return firebaseUid; }
    public String getEmail() { return email; }
    public String getUsername() { return username; }

    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }
    public void setEmail(String email) { this.email = email; }
    public void setUsername(String username) { this.username = username; }
}
