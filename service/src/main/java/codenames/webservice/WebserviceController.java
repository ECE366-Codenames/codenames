package codenames.webservice;

import codenames.business.GameService;
import codenames.dto.GameDTO;
import codenames.model.Game;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://codenames.eastus.azurecontainer.io"})
public class WebserviceController {
    private final GameService gameService;

    public WebserviceController(GameService gameService) {
        this.gameService = gameService;
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

    //add: add player to game
    
    @PostMapping("/game/{id}/start") // begins game
    public Long startGame(@PathVariable Long id) {
        //add: check if enough players have joined
        return gameService.startGame(id);
        //add: assign roles to players 
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

}
