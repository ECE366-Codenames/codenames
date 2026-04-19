package codenames.dto;

public class GamePlayerDTO {
    private String playerId;
    private String username;
    private boolean isSpymaster;
    private boolean isRed;
    private boolean isReady;

    public GamePlayerDTO(String playerId, String username, boolean isRed, boolean isSpymaster) {
        this.playerId = playerId;
        this.username = username;
        this.isRed = isRed;
        this.isSpymaster = isSpymaster;
        this.isReady = false;
    }

    public GamePlayerDTO(String playerId, String username, boolean isRed, boolean isSpymaster, boolean isReady) {
        this.playerId = playerId;
        this.username = username;
        this.isRed = isRed;
        this.isSpymaster = isSpymaster;
        this.isReady = isReady;
    }

    public String getPlayerId() {return playerId;}

    public String getUsername() {return username;}

    public boolean isSpymaster() {return isSpymaster;}

    public boolean isRed() {return isRed;}

    public boolean isReady() {return isReady;}

    public void setReady(boolean ready) {isReady = ready;}
}
