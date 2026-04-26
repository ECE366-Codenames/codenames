import codenames.dto.ClueDTO;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ClueDTOTest {

    @Test
    void testInitialization() {
        ClueDTO clue = new ClueDTO("Fruit", 3);

        assertEquals("Fruit", clue.getWord());
        assertEquals(3, clue.getNumber());
    }

    @Test
    void testSetWord() {
        ClueDTO clue = new ClueDTO("Fruit", 3);

        clue.setWord("Animal");

        assertEquals("Animal", clue.getWord());
    }

    @Test
    void testSetNumber() {
        ClueDTO clue = new ClueDTO("Fruit", 3);

        clue.setNumber(5);

        assertEquals(5, clue.getNumber());
    }

    @Test
    void testSettersTogether() {
        ClueDTO clue = new ClueDTO("Fruit", 3);

        clue.setWord("Vehicle");
        clue.setNumber(2);

        assertEquals("Vehicle", clue.getWord());
        assertEquals(2, clue.getNumber());
    }
}