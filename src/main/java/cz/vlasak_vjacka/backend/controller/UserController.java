package cz.vlasak_vjacka.backend.controller;

import cz.vlasak_vjacka.backend.model.Instrument;
import cz.vlasak_vjacka.backend.model.User;
import cz.vlasak_vjacka.backend.repository.InstrumentRepository;
import cz.vlasak_vjacka.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/api/users/add")
    public ResponseEntity<?> addUser(@Valid @RequestBody User newUser) {
        // 1. Validace pomocí getterů
        if (userRepository.existsByEmail(newUser.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email už je v systému!"));
        }
        if (userRepository.existsByUsername(newUser.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Uživatelské jméno už je v systému!"));
        }

        //2. ROLE a take fix s tim problem_role.png
        if (newUser.getRole() == null || newUser.getRole().trim().isEmpty()) {
            newUser.setRole("ROLE_USER");
        }
        else if (!newUser.getRole().startsWith("ROLE_")) {
            newUser.setRole("ROLE_" + newUser.getRole().toUpperCase());
        }

        // 3. Šifrování hesla
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        // 4. Uložení - ID se vygeneruje automaticky díky @GeneratedValue v entitě
        User savedUser = userRepository.save(newUser);

        // 5. Vrátíme uložený objekt zpět do Reactu
        return ResponseEntity.ok(savedUser);
    }
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
    @PutMapping("/api/users/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @Valid @RequestBody User updatedUser) {

        Optional<User> existingUserOpt = userRepository.findById(id);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "Uživatel nenalezen!"));
        }

        User existingUser = existingUserOpt.get();

        if (!existingUser.getEmail().equals(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Email už používá někdo jiný!"));
        }

        if (!existingUser.getUsername().equals(updatedUser.getUsername()) && userRepository.existsByUsername(updatedUser.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("message", "Uživatelské jméno už používá někdo jiný!"));
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());

        if (updatedUser.getRole() != null && !updatedUser.getRole().trim().isEmpty()) {
            if (!updatedUser.getRole().startsWith("ROLE_")) {
                existingUser.setRole("ROLE_" + updatedUser.getRole().toUpperCase());
            } else {
                existingUser.setRole(updatedUser.getRole().toUpperCase());
            }
        }

        if (updatedUser.getPassword() != null && !updatedUser.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        User savedUser = userRepository.save(existingUser);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/api/users/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/api/users/my-instruments")
    public List<Instrument> getUserInstruments(@RequestParam UUID userId) {
        return userRepository.findById(userId).map(User::getInstruments).orElse(List.of());
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