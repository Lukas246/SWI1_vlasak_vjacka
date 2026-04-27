package cz.vlasak_vjacka.backend.config;

import cz.vlasak_vjacka.backend.model.Instrument;
import cz.vlasak_vjacka.backend.model.Project;
import cz.vlasak_vjacka.backend.model.User;
import cz.vlasak_vjacka.backend.repository.InstrumentRepository;
import cz.vlasak_vjacka.backend.repository.ProjectRepository;
import cz.vlasak_vjacka.backend.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;
    private final ProjectRepository projectRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           InstrumentRepository instrumentRepository,
                           ProjectRepository projectRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.instrumentRepository = instrumentRepository;
        this.projectRepository = projectRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        System.out.println("--- DataInitializer: START (Project is Owner) ---");

        // 1. Čištění
        projectRepository.deleteAll();
        instrumentRepository.deleteAll();
        userRepository.deleteAll();

        // 2. Uživatelé
        User sup = createUser("sup", "sup@dojizdak.cz", "TajneHeslo123", "ROLE_ADMIN");
        User jan = createUser("honza", "jan@novak.cz", "honza", "ROLE_USER");
        User ludvig = createUser("Ludvig van B.", "ludvig@classic.de", "beethoven", "ROLE_ADMIN");
        userRepository.save(sup);
        userRepository.save(jan);
        userRepository.save(ludvig);

        // 3. Projekty
        Project band = new Project();
        band.setName("Rockový Výtah");
        band.setDescription("Hlasitá hudba pro lidi v pohybu.");

        Project orchestra = new Project();
        orchestra.setName("Vídeňská Filharmonie");
        orchestra.setDescription("Klasika v nejlepším podání.");

        // 4. PROPOJENÍ (Plníme VLASTNÍKA vazby - Project)
        // Přidáváme uživatele do projektů
        band.getMembers().add(sup);
        band.getMembers().add(jan);

        orchestra.getMembers().add(ludvig);
        orchestra.getMembers().add(sup);

        // 5. ULOŽENÍ
        // Nejdříve uložíme projekty (tím se zapíše spojovací tabulka)
        projectRepository.saveAll(List.of(band, orchestra));

        // Nástroje (1:N)
        instrumentRepository.save(new Instrument("Fender Stratocaster", 85000.0, "Top", sup));

        System.out.println("--- DataInitializer: HOTOVO ---");
    }

    private User createUser(String username, String email, String pass, String role) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPassword(passwordEncoder.encode(pass));
        u.setRole(role);
        return u;
    }
}