package codenames.dto;

import java.util.List;

public class GameDTO {
    private long id;
    private String status;
    private boolean redTurn;
    private List<CardDTO> cards;

    public GameDTO(long id, String status, boolean redTurn, List<CardDTO> cards) {
        this.id = id;
        this.status = status;
        this.redTurn = redTurn;
        this.cards = cards;
    }

    public long getId() {
        return id;
    }
    public String getStatus() {
        return status;
    }
    public boolean isRedTurn() {
        return redTurn;
    }
    public List<CardDTO> getCards() {
        return cards;
    }
}
