package codenames.dto;

import java.util.List;
import codenames.model.Status;

public class GameDTO {
    private long id;
    private Status status;
    private boolean redTurn;
    private boolean redWin;
    private List<CardDTO> cards;

    public GameDTO(long id, Status status, boolean redTurn, boolean redWin, List<CardDTO> cards) {
        this.id = id;
        this.status = status;
        this.redTurn = redTurn;
        this.cards = cards;
        this.redWin = redWin;
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

    public boolean redWin() {
        return redWin;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
}
