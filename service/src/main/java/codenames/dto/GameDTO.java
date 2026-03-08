package codenames.dto;

import java.util.List;
import codenames.model.Status;

public class GameDTO {
    private long id;
    private Status status;
    private boolean redTurn;
    private List<CardDTO> cards;

    public GameDTO(long id, Status status, boolean redTurn, List<CardDTO> cards) {
        this.id = id;
        this.status = status;
        this.redTurn = redTurn;
        this.cards = cards;
    }

    public long getId() {
        return id;
    }
    public Status getStatus() {
        return status;
    }
    public boolean isRedTurn() {
        return redTurn;
    }
    public List<CardDTO> getCards() {
        return cards;
    }
}
