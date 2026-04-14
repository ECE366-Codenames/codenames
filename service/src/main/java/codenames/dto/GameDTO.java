package codenames.dto;

import java.util.List;
import codenames.model.Status;
import codenames.model.TurnPhase;

public class GameDTO {
    private long id;
    private Status status;
    private Boolean redTurn;
    private Boolean redWin;
    private List<CardDTO> cards;
    private TurnPhase turnPhase;
    private String clueWord;
    private int clueNumber;
    private int guessesRemaining;

    public GameDTO(long id, Status status, Boolean redTurn, Boolean redWin, List<CardDTO> cards, TurnPhase turnPhase, String clueWord, int clueNumber, int guessesRemaining) {
        this.id = id;
        this.status = status;
        this.redTurn = redTurn;
        this.cards = cards;
        this.redWin = redWin;
        this.turnPhase = turnPhase;
        this.clueWord = clueWord;
        this.clueNumber = clueNumber;
        this.guessesRemaining = guessesRemaining;
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
    public TurnPhase getTurnPhase() {return turnPhase;}
    public String getClueWord() {return clueWord;}
    public int getClueNumber() {return clueNumber;}
    public int getGuessesRemaining() {return guessesRemaining;}
}
