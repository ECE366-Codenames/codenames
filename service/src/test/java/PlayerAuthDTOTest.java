import codenames.dto.PlayerAuthDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerAuthDTOTest {

    @Test
    void testDefaultValues() {
        PlayerAuthDTO player = new PlayerAuthDTO();

        assertNull(player.getFirebaseUid());
        assertNull(player.getEmail());
        assertNull(player.getUsername());
    }

    @Test
    void testSetFirebaseUid() {
        PlayerAuthDTO player = new PlayerAuthDTO();

        player.setFirebaseUid("uid123");

        assertEquals("uid123", player.getFirebaseUid());
    }

    @Test
    void testSetEmail() {
        PlayerAuthDTO player = new PlayerAuthDTO();

        player.setEmail("test@example.com");

        assertEquals("test@example.com", player.getEmail());
    }

    @Test
    void testSetUsername() {
        PlayerAuthDTO player = new PlayerAuthDTO();

        player.setUsername("Alice");

        assertEquals("Alice", player.getUsername());
    }

    @Test
    void testSetAllFields() {
        PlayerAuthDTO player = new PlayerAuthDTO();

        player.setFirebaseUid("uid456");
        player.setEmail("alice@example.com");
        player.setUsername("Alice");

        assertEquals("uid456", player.getFirebaseUid());
        assertEquals("alice@example.com", player.getEmail());
        assertEquals("Alice", player.getUsername());
    }
}