package cz.vlasak_vjacka.backend.controller;

import cz.vlasak_vjacka.backend.LoginRequest;
import cz.vlasak_vjacka.backend.User;
import cz.vlasak_vjacka.backend.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthController(PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. Očištění vstupů (prevence chyb s neviditelnými znaky)
        String username = loginRequest.username != null ? loginRequest.username.trim() : "";
        String password = loginRequest.password != null ? loginRequest.password.trim() : "";

        // 2. Hledání uživatele
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return ResponseEntity.status(401).body(Map.of("error", "Uživatel nenalezen"));
        }

        // 3. Ověření hesla (raw heslo z požadavku vs hash z DB)
        if (passwordEncoder.matches(password, user.getPassword())) {

            // Tady se později bude generovat skutečný JWT token
            String dummyToken = "jwt-session-" + java.util.UUID.randomUUID();

            // 4. Vrácení dat, která React potřebuje pro stav aplikace
            return ResponseEntity.ok(Map.of(
                    "message", "Přihlášení úspěšné",
                    "username", user.getUsername(),
                    "role", user.getRole(),
                    "token", dummyToken
            ));
        } else {
            return ResponseEntity.status(401).body(Map.of("error", "Neplatné heslo"));
        }
    }
}