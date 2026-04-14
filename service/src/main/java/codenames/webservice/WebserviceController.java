package codenames.webservice;

import codenames.business.GameService;
import codenames.business.PlayerService;
import codenames.dto.*;
import codenames.model.Game;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = {"http://localhost", "http://localhost:5173", "http://codenames.eastus.azurecontainer.io"})
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
    
    @PostMapping("/game/{id}/start") // begins game & initializes player roles
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

//    @PostMapping("/player") //the username, password, and email of a new player go in the body of the request in JSON format
//    public Long createPlayer(@RequestBody PlayerInitDTO dto) {
//        return playerService.createPlayer(dto);
//    }

    @PostMapping("/game/{id}/join") //the player Id is a request parameter
    public String joinGame(@PathVariable Long id, @RequestParam String playerId) {
        return playerService.addPlayerToGame(id, playerId);
    }

    @GetMapping("/game/{id}/players")
    public List<GamePlayerDTO> getPlayers(@PathVariable Long id) {
        return playerService.getPlayersByGameId(id);
    }

    @PostMapping("/player/auth")
    public String createOrGetPlayer(@RequestBody PlayerAuthDTO dto) {
        return playerService.createOrGetPlayer(dto.getFirebaseUid(), dto.getEmail(), dto.getUsername());
    }

    @PostMapping("/game/{id}/clue")
    public void submitClue(@PathVariable Long id, @RequestBody ClueDTO dto) {
        gameService.submitClue(id, dto.getWord(), dto.getNumber());
    }

    @PostMapping("/game/{id}/pass")
    public void passTurn(@PathVariable Long id) {
        gameService.passTurn(id);
    }

}
