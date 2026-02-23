package cz.vlasak_vjacka.backend;

import jakarta.annotation.Nonnull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final InstrumentRepository instrumentRepository;

    // Používáme doporučený konstruktor (místo @Autowired na polích)
    public DataSeeder(UserRepository userRepository, InstrumentRepository instrumentRepository) {
        this.userRepository = userRepository;
        this.instrumentRepository = instrumentRepository;
    }

    @Override
    public void run(@Nonnull String... args) {
        System.out.println("🌱 Zahajuji seedování dat...");

        // 1. Smazání starých dat (POZOR: Instrumenty musí jít první kvůli cizím klíčům)
        instrumentRepository.deleteAll();
        userRepository.deleteAll();

        // 2. Vytvoření testovacího uživatele
        User test = new User();
        test.username = "SupDojizdak";
        test.email = "sup@dojizdak.cz";
        userRepository.save(test);

        // 3. Vytvoření testovacích nástrojů
        Instrument kytara = new Instrument();
        kytara.name = "Fender Stratocaster";
        kytara.price = 45000.0;
        kytara.owner = test; // Propojení s uživatelem

        Instrument basa = new Instrument();
        basa.name = "Gibson Thunderbird";
        basa.price = 38000.0;
        basa.owner = test;

        Instrument drum = new Instrument();
        drum.name = "Yamaha DR-10";
        drum.price = 15000.0;
        drum.owner = test;

        // 4. Uložení nástrojů
        instrumentRepository.saveAll(List.of(kytara, basa, drum));

        System.out.println("✅ Data byla úspěšně nahrána!");
    }
}