package codenames.repository;

import codenames.model.GamePlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GamePlayerRepository extends JpaRepository<GamePlayer, Long> {
    List<GamePlayer> findByGame(Game game);

    boolean existsByGameAndPlayer(Game game, Player player);
}
