package llipport.mc_questions.database.repositories;

import llipport.mc_questions.database.entities.Exam;
import org.springframework.data.repository.CrudRepository;

import java.sql.Date;
import java.util.List;
import java.util.UUID;

public interface ExamRepository extends CrudRepository<Exam, UUID> {
    List<Exam> findByDate(Date date);

    Exam findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}
