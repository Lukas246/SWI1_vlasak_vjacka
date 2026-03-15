package cz.vlasak_vjacka.backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        return args -> {
            System.out.println("DataInitializer: Čistím staré pozůstatky...");

            // 1. Totální čistka přes SQL
            jdbcTemplate.execute("DELETE FROM instruments");
            jdbcTemplate.execute("DELETE FROM users");

            // 2. Definice uživatelů a jejich hesel
            // Pro jednoduchost: admini mají "admin123", uživatelé "user123"
            String supId = "550e8400-e29b-41d4-a716-446655440000";
            String janId = "67e55044-10b1-426f-9247-bb680e5fe0c8";
            String ludvigId = "12345678-1234-1234-1234-1234567890ab";

            insertUser(jdbcTemplate, passwordEncoder, supId, "sup", "sup@dojizdak.cz", "TajneHeslo123", "ROLE_ADMIN");
            insertUser(jdbcTemplate, passwordEncoder, janId, "Jan Novak", "jan@novak.cz", "user123", "ROLE_USER");
            insertUser(jdbcTemplate, passwordEncoder, ludvigId, "Ludvig van B.", "ludvig@classic.de", "beethoven", "ROLE_ADMIN");

            // 3. Vložení nástrojů (přímo s ID majitelů)
            // Sup Dojizdak
            insertInstrument(jdbcTemplate, "Fender Stratocaster 1964", 85000.0, "Původní stav.", supId);
            insertInstrument(jdbcTemplate, "Gibson Thunderbird Bass", 42000.5, "Rockový zvuk.", supId);

            // Jan Novak
            insertInstrument(jdbcTemplate, "Yamaha P-45 Digital Piano", 12500.0, "Pro cvičení.", janId);

            // Ludvig van B.
            insertInstrument(jdbcTemplate, "Steinway & Sons Model D", 2500000.0, "Koncertní křídlo.", ludvigId);

            System.out.println("DataInitializer: Databáze je připravena a hesla zahashována!");
        };
    }

    // Pomocná metoda pro SQL vklad uživatele
    private void insertUser(JdbcTemplate jdbc, PasswordEncoder encoder, String id, String name, String email, String pass, String role) {
        jdbc.update("INSERT INTO users (id, username, email, password, role) VALUES (?, ?, ?, ?, ?)",
                id, name, email, encoder.encode(pass), role);
    }

    // Pomocná metoda pro SQL vklad nástroje
    private void insertInstrument(JdbcTemplate jdbc, String name, Double price, String desc, String userId) {
        jdbc.update("INSERT INTO instruments (id, name, price, description, user_id) VALUES (?, ?, ?, ?, ?)",
                UUID.randomUUID().toString(), name, price, desc, userId);
    }
}