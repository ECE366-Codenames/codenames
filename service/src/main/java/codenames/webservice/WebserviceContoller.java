package codenames.webservice;

import codenames.business.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tools.jackson.databind.ObjectMapper;

@RestController
public class WebserviceContoller {
    @GetMapping("/testPlayer")
    public String getTestPlayer() {
        return "TEST PLAYER";
    }

    private final GameService gameService;

    public WebserviceContoller(GameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/create")
    public String createPlayer(@RequestBody String json) {
        System.out.println("createPlayer");
        System.out.println(json);
        ObjectMapper objectMapper = new ObjectMapper();
        return this.gameService.createGame();
    }
}
