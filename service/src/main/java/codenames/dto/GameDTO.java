package codenames.dto;

import java.util.List;
import codenames.model.Status;

public class GameDTO {
    private long id;
    private Status status;
    private Boolean redTurn;
    private Boolean redWin;
    private List<CardDTO> cards;

    public GameDTO(long id, Status status, Boolean redTurn, Boolean redWin, List<CardDTO> cards) {
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
    public Boolean isRedTurn() {
        return redTurn;
    }

    public Boolean getRedWin() {
        return redWin;
    }

    public List<CardDTO> getCards() {
        return cards;
    }
}
