package llipport.mc_questions.database.repositories;

import llipport.mc_questions.database.entities.Settings;
import org.springframework.data.repository.CrudRepository;

import java.util.UUID;

public interface SettingsRepository extends CrudRepository<Settings, UUID> {
    Settings findByName(String name);
}
