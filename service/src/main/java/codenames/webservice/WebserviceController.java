package codenames.webservice;

import codenames.business.GameService;
import codenames.dto.GameDTO;
import codenames.model.Game;
import org.springframework.web.bind.annotation.*;

@RestController
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

    @PostMapping("/create") // creates new game with 25 cards and returns game id
    public Long createGame() {
        return gameService.createGame();
    }

    @PostMapping("/game/{id}/guess/{position}") // guesses word at position, sets that card as revealed and updates turn
    public void guess(@PathVariable Long id, @PathVariable int position) {
        gameService.guess(id, position);
    }

}
