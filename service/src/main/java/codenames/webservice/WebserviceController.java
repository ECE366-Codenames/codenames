package codenames.webservice;

import codenames.business.GameService;
import codenames.business.PlayerService;
import codenames.dto.GameDTO;
import codenames.dto.PlayerDTO;
import codenames.model.Game;
import org.springframework.web.bind.annotation.*;

@RestController
public class WebserviceController {
    private final GameService gameService;
    private final PlayerService playerService;

    public WebserviceController(GameService gameService, PlayerService playerService) {
        this.gameService = gameService;
        this.playerService = playerService;
    }

    @GetMapping("/game/{id}") // returns game with id
    public GameDTO getGameById(@PathVariable Long id, @RequestParam(required = false) boolean spymaster) {
        Game game = gameService.getGameById(id);
        System.out.println("Getting game by id: " + id);
        System.out.println(game.getCards());
        return gameService.toDTO(game, spymaster);
    }

    @PostMapping("/create") // creates new game, puts in status waiting
    public Long createGame() {
        return gameService.createGame();
    }
    
    @PostMapping("/game/{id}/start") // begins game
    public Long startGame(@PathVariable Long id) {
        return gameService.startGame(id);
    }

    @PostMapping("/game/{id}/guess/{position}") // guesses word at position, sets that card as revealed and updates turn
    public void guess(@PathVariable Long id, @PathVariable int position) {
        gameService.guess(id, position);
    }

    @PostMapping("/game/{id}/end")
    public void endGame(@PathVariable Long id) {
        gameService.abort(id);
    }

    @PostMapping("/game/{id}/cleanup")
    public void cleanup(@PathVariable Long id) {
        gameService.cleanup(id);
    }

    @PostMapping("/player")
    public Long createPlayer(@RequestBody PlayerDTO dto) {
        return playerService.createPlayer(dto);
    }

    @PostMapping("/game/{id}/join")
    public Long joinGame(@PathVariable Long id, @RequestParam Long playerId) {
        return playerService.addPlayerToGame(id, playerId);
    }



}
