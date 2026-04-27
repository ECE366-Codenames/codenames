import codenames.business.PlayerService;
import codenames.dto.GamePlayerDTO;
import codenames.model.*;
import codenames.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayerServiceTest {

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GamePlayerRepository gamePlayerRepository;

    @InjectMocks
    private PlayerService playerService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // createOrGetPlayer
    @Test
    void testCreateNewPlayer() {
        Player player = new Player();
        player.setId("uid123");

        when(playerRepository.findById("uid123")).thenReturn(Optional.empty());
        when(playerRepository.save(any(Player.class))).thenReturn(player);

        String result = playerService.createOrGetPlayer(
                "uid123",
                "test@example.com",
                "testuser"
        );

        assertEquals("uid123", result);
        verify(playerRepository).save(any(Player.class));
    }

    @Test
    void testGetExistingPlayer() {
        Player player = new Player();
        player.setId("uid123");

        when(playerRepository.findById("uid123")).thenReturn(Optional.of(player));

        String result = playerService.createOrGetPlayer(
                "uid123",
                "test@example.com",
                "testuser"
        );

        assertEquals("uid123", result);
        verify(playerRepository, never()).save(any(Player.class));
    }

    // addPlayerToGame
    @Test
    void testAddPlayerToGameSuccess() {
        Game game = new Game(Status.WAITING);
        Player player = new Player();
        player.setId("p1");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(playerRepository.findById("p1")).thenReturn(Optional.of(player));
        when(gamePlayerRepository.existsByGameAndPlayer(game, player)).thenReturn(false);
        when(gamePlayerRepository.findByGame(game)).thenReturn(List.of());

        String result = playerService.addPlayerToGame(1L, "p1");

        assertEquals("p1", result);
        verify(gamePlayerRepository).save(any(GamePlayer.class));
    }

    @Test
    void testAddPlayerGameFull() {
        Game game = new Game(Status.WAITING);
        Player player = new Player();
        player.setId("p1");

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(playerRepository.findById("p1")).thenReturn(Optional.of(player));
        when(gamePlayerRepository.existsByGameAndPlayer(game, player)).thenReturn(false);

        // simulate full game (4 players already)
        GamePlayer gp = mock(GamePlayer.class);
        when(gamePlayerRepository.findByGame(game)).thenReturn(List.of(gp, gp, gp, gp));

        assertThrows(IllegalStateException.class,
                () -> playerService.addPlayerToGame(1L, "p1"));
    }

    // getPlayerById
    @Test
    void testGetPlayerById() {
        Player player = new Player();
        player.setId("p1");

        when(playerRepository.findById("p1")).thenReturn(Optional.of(player));

        Player result = playerService.getPlayerById("p1");

        assertEquals("p1", result.getId());
    }

    // getPlayersByGameId
    @Test
    void testGetPlayersByGameId() {
        Game game = new Game(Status.STARTED);

        Player player = new Player();
        player.setId("p1");
        player.setUsername("Alice");

        GamePlayer gp = mock(GamePlayer.class);
        when(gp.getPlayer()).thenReturn(player);
        when(gp.isRed()).thenReturn(true);
        when(gp.isSpymaster()).thenReturn(false);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gamePlayerRepository.findByGame(game)).thenReturn(List.of(gp));

        List<GamePlayerDTO> result = playerService.getPlayersByGameId(1L);

        assertEquals(1, result.size());
        assertEquals("p1", result.get(0).getPlayerId());
        assertEquals("Alice", result.get(0).getUsername());
        assertTrue(result.get(0).isRed());
    }

    // removePlayerFromGame
    @Test
    void testRemovePlayerFromGame() {
        Game game = new Game(Status.STARTED);
        Player player = new Player();
        player.setId("p1");

        GamePlayer gp = mock(GamePlayer.class);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(playerRepository.findById("p1")).thenReturn(Optional.of(player));
        when(gamePlayerRepository.findByGameAndPlayer(game, player))
                .thenReturn(Optional.of(gp));

        playerService.removePlayerFromGame(1L, "p1");

        verify(gamePlayerRepository).delete(gp);
    }
}