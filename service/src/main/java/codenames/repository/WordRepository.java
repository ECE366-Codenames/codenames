package codenames.repository;

import codenames.model.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;

public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = "SELECT * FROM word ORDER BY RANDOM() LIMIT :count", nativeQuery = true)
    List<Word> getRandomWords(int count);

}
