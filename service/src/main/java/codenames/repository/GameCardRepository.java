package codenames.repository;

import codenames.model.GameCard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GameCardRepository extends JpaRepository<GameCard, Long> {
    Optional<GameCard> findByGameIdAndPosition(Long gameId, int position);
}
