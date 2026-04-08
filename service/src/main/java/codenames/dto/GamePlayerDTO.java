package codenames.dto;

public class GamePlayerDTO {
    private long playerId;
    private String username;
    private boolean isSpymaster;
    private boolean isRed;

    public GamePlayerDTO(long playerId, String username, boolean isRed, boolean isSpymaster) {
        this.playerId = playerId;
        this.username = username;
        this.isRed = isRed;
        this.isSpymaster = isSpymaster;
    }

    public long getPlayerId() {return playerId;}

    public String getUsername() {return username;}

    public boolean isSpymaster() {return isSpymaster;}

    public boolean isRed() {return isRed;}
}
