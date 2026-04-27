import codenames.dto.CardDTO;
import codenames.dto.GameDTO;
import codenames.model.Status;
import codenames.model.TurnPhase;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameDTOTest {

    @Test
    void testInitialization() {
        // Arrange
        CardDTO card1 = new CardDTO("Apple", false, "RED", 0);
        CardDTO card2 = new CardDTO("Dog", true, "BLUE", 1);
        List<CardDTO> cards = List.of(card1, card2);

        GameDTO game = new GameDTO(
                1L,
                Status.STARTED,
                true,
                null,
                cards,
                TurnPhase.GUESS,
                "Fruit",
                2,
                1
        );

        // Assert
        assertEquals(1L, game.getId());
        assertEquals(Status.STARTED, game.getStatus());
        assertTrue(game.isRedTurn());
        assertNull(game.getRedWin());
        assertEquals(cards, game.getCards());
        assertEquals(TurnPhase.GUESS, game.getTurnPhase());
        assertEquals("Fruit", game.getClueWord());
        assertEquals(2, game.getClueNumber());
        assertEquals(1, game.getGuessesRemaining());
    }

    @Test
    void testCompletedGameRedWins() {
        GameDTO game = new GameDTO(
                2L,
                Status.COMPLETE,
                false,
                true,
                List.of(),
                TurnPhase.CLUE,
                "Animal",
                3,
                0
        );

        assertEquals(Status.COMPLETE, game.getStatus());
        assertTrue(game.getRedWin());
        assertFalse(game.isRedTurn());
        assertEquals(TurnPhase.CLUE, game.getTurnPhase());
    }
}