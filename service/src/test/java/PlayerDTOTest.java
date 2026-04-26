import codenames.dto.PlayerDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerDTOTest {

    @Test
    void testInitialization() {
        PlayerDTO player = new PlayerDTO(
                1L,
                "Alice",
                "alice@example.com",
                "hashedPassword123",
                10,
                2,
                true,
                "plainPassword"
        );

        assertEquals(1L, player.getId());
        assertEquals("Alice", player.getUsername());
        assertEquals("alice@example.com", player.getEmail());
        assertEquals(10, player.getWins());
        assertEquals(2, player.getLosses());
        assertTrue(player.isOnline());
        assertEquals("plainPassword", player.getPassword());
    }

    @Test
    void testOfflinePlayer() {
        PlayerDTO player = new PlayerDTO(
                2L,
                "Bob",
                "bob@example.com",
                "hash",
                0,
                0,
                false,
                "secret"
        );

        assertFalse(player.isOnline());
        assertEquals(0, player.getWins());
        assertEquals(0, player.getLosses());
    }
}