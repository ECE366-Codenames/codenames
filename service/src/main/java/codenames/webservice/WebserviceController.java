package codenames.webservice;

import codenames.business.GameService;
import codenames.model.Game;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebserviceController {
    private final GameService gameService;

    public WebserviceController(GameService gameService) {
        this.gameService = gameService;
    }

    @GetMapping("/game/{id}")
    public Game getGameById(@PathVariable Long id) {
        Game game = gameService.getGameById(id);
        System.out.println("Getting game by id: " + id);
        System.out.println(game.getCards());
        return gameService.getGameById(id);
    }

    @PostMapping("/create")
    public Long createGame() {
        return gameService.createGame();
    }
}
