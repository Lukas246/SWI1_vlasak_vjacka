package cz.vlasak_vjacka.backend.controller;

import cz.vlasak_vjacka.backend.model.Instrument;
import cz.vlasak_vjacka.backend.model.User;
import cz.vlasak_vjacka.backend.repository.InstrumentRepository;
import cz.vlasak_vjacka.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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
    public Map<String, String> getStatus() {
        // Vrátíme JSON: {"message": "Backend is running!"}
        return Collections.singletonMap("message", "Backend is running!");
    }

    // Teď bude dostupná na /api/instruments/all (přidal jsem instruments pro přehlednost)
    @GetMapping("/instruments/all")
    public List<Instrument> getAll() {
        return instrumentRepository.findAll();
    }

    @GetMapping("/instruments/{id}")
    public ResponseEntity<Instrument> getInstrument(@PathVariable String id) {
        return instrumentRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/instruments/{id}")
    public ResponseEntity<Instrument> updateInstrument(@PathVariable String id, @RequestBody Instrument instrument) {
        if (!instrumentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        instrument.setId(id);
        Instrument saved = instrumentRepository.save(instrument);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/instruments/{id}")
    public ResponseEntity<?> deleteInstrument(@PathVariable String id) {
        if (!instrumentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        instrumentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}