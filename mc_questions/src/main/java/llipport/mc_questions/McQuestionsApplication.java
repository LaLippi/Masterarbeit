package llipport.mc_questions;

import llipport.mc_questions.database.entities.AppUser;
import llipport.mc_questions.database.entities.Settings;
import llipport.mc_questions.database.repositories.AppUserRepository;
import llipport.mc_questions.database.repositories.SettingsRepository;
import llipport.mc_questions.database.util.ROLE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class McQuestionsApplication {

    private static final Logger log = LoggerFactory.getLogger(McQuestionsApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(McQuestionsApplication.class, args);
    }

    @Bean
    public CommandLineRunner checkDefaults(AppUserRepository userRepository, SettingsRepository settingsRepository) {
        return (args) -> {
            log.info("Found " + userRepository.count() + " users");
            if (userRepository.count() == 0) {
                log.info("Found no users in database, creating default admin user");
                PasswordEncoder bcrypt = new BCryptPasswordEncoder();
                AppUser admin = new AppUser("Admin", bcrypt.encode("pw"), ROLE.ADMIN);
                userRepository.save(admin);
            }
            if (settingsRepository.findByName("default") == null) {
                log.info("Did not find \"default\" settings, creating them");
                Settings defaultSettings = new Settings(
                        "default",
                        12,
                        12,
                        1,
                        1,
                        2,
                        5,
                        5,
                        1
                );
                settingsRepository.save(defaultSettings);
            }
            log.info("Anwendung gestartet");
        };
    }

}
