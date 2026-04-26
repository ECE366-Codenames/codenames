import codenames.business.GameService;
import codenames.dto.GameDTO;
import codenames.model.*;
import codenames.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameServiceTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private WordRepository wordRepository;

    @Mock
    private GameCardRepository gameCardRepository;

    @Mock
    private GamePlayerRepository gamePlayerRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // createGame
    @Test
    void testCreateGame() {
        Game game = new Game(Status.WAITING);
        setId(game, 1L);

        when(gameRepository.save(any(Game.class))).thenReturn(game);

        Long result = gameService.createGame();

        assertNotNull(result);
        verify(gameRepository).save(any(Game.class));
    }

    // startGame success
    @Test
    void testStartGameSuccess() {
        Game game = new Game(Status.WAITING);
        setId(game, 1L); // ✅ FIX: prevent NullPointerException

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gamePlayerRepository.findByGame(game)).thenReturn(createPlayers());
        when(wordRepository.getRandomWords(25)).thenReturn(createWords());

        Long result = gameService.startGame(1L);

        assertEquals(Status.STARTED, game.getStatus());
        assertNotNull(game.getCards());
        assertEquals(25, game.getCards().size());
        assertTrue(game.getRedTurn());

        verify(messagingTemplate, atLeastOnce())
                .convertAndSend(anyString(), any(GameDTO.class));
    }

    @Test
    void testStartGameWrongStatus() {
        Game game = new Game(Status.STARTED);
        setId(game, 2L);

        when(gameRepository.findById(2L)).thenReturn(Optional.of(game));

        assertThrows(RuntimeException.class,
                () -> gameService.startGame(2L));
    }

    @Test
    void testStartGameNotEnoughPlayers() {
        Game game = new Game(Status.WAITING);
        setId(game, 3L);

        when(gameRepository.findById(3L)).thenReturn(Optional.of(game));
        when(gamePlayerRepository.findByGame(game)).thenReturn(List.of());

        assertThrows(IllegalStateException.class,
                () -> gameService.startGame(3L));
    }

    // guess validation
    @Test
    void testGuessWrongPhase() {
        Game game = new Game(Status.STARTED);
        setId(game, 4L);
        game.setTurnPhase(TurnPhase.CLUE);

        when(gameRepository.findById(4L)).thenReturn(Optional.of(game));

        assertThrows(RuntimeException.class,
                () -> gameService.guess(4L, 0, "p1"));
    }

    // helper
    private void setId(Game game, Long id) {
        try {
            java.lang.reflect.Field field = Game.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(game, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // helpers
    private List<GamePlayer> createPlayers() {
        return List.of(
                mock(GamePlayer.class),
                mock(GamePlayer.class),
                mock(GamePlayer.class),
                mock(GamePlayer.class)
        );
    }

    private List<Word> createWords() {
        List<Word> words = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            Word w = new Word();
            w.setWord("word" + i); // assumes Word has setText()
            words.add(w);
        }

        return words;
    }
}