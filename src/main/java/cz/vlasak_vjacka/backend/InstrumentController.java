package cz.vlasak_vjacka.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api") // Přidá /api před všechny cesty v tomto controlleru
public class InstrumentController {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private UserRepository userRepository;

    // Cesta sjednocená s Reactem: /api/users/{userId}/instruments
    @PostMapping("/users/{userId}/instruments")
    public ResponseEntity<?> addInstrument(@PathVariable UUID userId, @RequestBody Instrument newInstrument) {

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Uživatel s ID " + userId + " nebyl nalezen!"));
        }

        User owner = userOpt.get();
        newInstrument.setOwner(owner);
        Instrument savedInstrument = instrumentRepository.save(newInstrument);

        return ResponseEntity.ok(savedInstrument);
    }

    // Teď bude dostupná na /api/status
    @GetMapping("/status")
    public String getStatus() {
        return "Backend is running!";
    }

    // Teď bude dostupná na /api/instruments/all (přidal jsem instruments pro přehlednost)
    @GetMapping("/instruments/all")
    public List<Instrument> getAll() {
        return instrumentRepository.findAll();
    }
}