package codenames.repository;

import codenames.model.GamePlayer;
import codenames.model.Game;
import codenames.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
    List<GamePlayer> findByGame(Game game);

    boolean existsByGameAndPlayer(Game game, Player player);

    Optional<GamePlayer> findByGameAndPlayer(Game game, Player player);

    Optional<GamePlayer> findByGameAndPlayer_Id(Game game, String playerId);
}
