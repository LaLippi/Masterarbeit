package llipport.mc_questions.database.repositories;

import llipport.mc_questions.database.entities.Question;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface QuestionRepository extends CrudRepository<Question, Long> {

    Question findByQuestionText(String text);

    Question findByUuid(UUID id);

    void deleteByUuid(UUID id);

    boolean existsByUuid(UUID uuid);
}
