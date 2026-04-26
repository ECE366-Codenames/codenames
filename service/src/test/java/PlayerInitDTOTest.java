import codenames.dto.PlayerInitDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PlayerInitDTOTest {

    @Test
    void testInitialization() {
        PlayerInitDTO player = new PlayerInitDTO(
                "Alice",
                "alice@example.com",
                "secretPassword"
        );

        assertEquals("Alice", player.getUsername());
        assertEquals("alice@example.com", player.getEmail());
        assertEquals("secretPassword", player.getPassword());
    }

    @Test
    void testDifferentValues() {
        PlayerInitDTO player = new PlayerInitDTO(
                "Bob",
                "bob@example.com",
                "anotherPassword"
        );

        assertEquals("Bob", player.getUsername());
        assertEquals("bob@example.com", player.getEmail());
        assertEquals("anotherPassword", player.getPassword());
    }
}