import codenames.dto.CardDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CardDTOTest {

    @Test
    void testCardInitialization() {
        CardDTO carddto = new CardDTO("Apple", false, "RED", 0);

        assertEquals("Apple", carddto.getWord());
        assertEquals("RED", carddto.getType());
        assertFalse(carddto.isRevealed());
        assertEquals(0, carddto.getPosition());
    }

    @Test
    void testRevealedStateFromConstructor() {
        CardDTO carddto = new CardDTO("Apple", true, "RED", 1);

        assertTrue(carddto.isRevealed());
    }
}