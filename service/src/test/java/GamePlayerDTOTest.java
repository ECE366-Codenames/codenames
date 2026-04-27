import codenames.dto.GamePlayerDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class GamePlayerDTOTest {

    @Test
    void testInitializationRedSpymaster() {
        GamePlayerDTO player = new GamePlayerDTO(
                "p1",
                "Alice",
                true,
                true
        );

        assertEquals("p1", player.getPlayerId());
        assertEquals("Alice", player.getUsername());
        assertTrue(player.isRed());
        assertTrue(player.isSpymaster());
    }

    @Test
    void testInitializationBlueNonSpymaster() {
        GamePlayerDTO player = new GamePlayerDTO(
                "p2",
                "Bob",
                false,
                false
        );

        assertEquals("p2", player.getPlayerId());
        assertEquals("Bob", player.getUsername());
        assertFalse(player.isRed());
        assertFalse(player.isSpymaster());
    }
}