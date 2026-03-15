package cz.vlasak_vjacka.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @PostMapping("/api/users/add")
    public ResponseEntity<?> addUser(@RequestBody User newUser) {
        // 1. Validace pomocí getterů
        if (userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email už je v systému!"));
        }
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Uživatelské jméno už je v systému!"));
        }

        // 2. Nastavení defaultní role před uložením
        newUser.setRole("USER");

        // 3. Uložení - ID se vygeneruje automaticky díky @GeneratedValue v entitě
        User savedUser = userRepository.save(newUser);

        // 4. Vrátíme uložený objekt zpět do Reactu
        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/api/users/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/api/users/my-instruments")
    public List<Instrument> getUserInstruments(@RequestParam UUID userId) {
        // Najdeme uživatele
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // Vrátíme seznam jeho nástrojů
            return user.getInstruments();
        }

        return null; // Nebo prázdný seznam List.of()
    }

    @GetMapping("/api/users/add-instrument")
    public String addInstrumentToUser(@RequestParam UUID userId, @RequestParam String name, @RequestParam Double price) {
        // Najdeme uživatele
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "Chyba: Uživatel s ID " + userId + " neexistuje!";

        // Vytvoříme a propojíme nástroj
        Instrument i = new Instrument();
        i.setName(name);
        i.setPrice(price);
        i.setOwner(user); // Toto nastaví cizí klíč v databázi

        instrumentRepository.save(i);
        return "Nástroj '" + name + "' byl přiřazen uživateli " + user.getUsername();
    }


}