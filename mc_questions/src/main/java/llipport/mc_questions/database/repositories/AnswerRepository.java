package llipport.mc_questions.database.repositories;

import llipport.mc_questions.database.entities.Answer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.UUID;

public interface AnswerRepository extends CrudRepository<Answer, UUID> {

    List<Answer> findByText(String text);

    Answer findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}
