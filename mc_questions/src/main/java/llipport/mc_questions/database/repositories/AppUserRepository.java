package llipport.mc_questions.database.repositories;

import llipport.mc_questions.database.entities.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;


public interface AppUserRepository extends JpaRepository<AppUser, UUID> {

    AppUser findByName(String name);

    AppUser findByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}
