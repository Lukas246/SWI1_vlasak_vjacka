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
@RequestMapping("/api")
public class InstrumentController {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @Autowired
    private UserRepository userRepository;

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

    @GetMapping("/status")
    public Map<String, String> getStatus() {
        return Collections.singletonMap("message", "Backend is running!");
    }

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

    /*
    @PutMapping("/instruments/{id}")
    public ResponseEntity<?> updateInstrument(@PathVariable String id, @RequestBody Instrument updatedInstrument) {

        Optional<Instrument> existingInstrumentOpt = instrumentRepository.findById(id);

        if (existingInstrumentOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Nástroj nenalezen!"));
        }

        Instrument existingInstrument = existingInstrumentOpt.get();

        if (updatedInstrument.getName() != null && !updatedInstrument.getName().trim().isEmpty()) {
            existingInstrument.setName(updatedInstrument.getName());
        }

        if (updatedInstrument.getPrice() != null) {
            existingInstrument.setPrice(updatedInstrument.getPrice());
        }

        if (updatedInstrument.getDescription() != null) {
            existingInstrument.setDescription(updatedInstrument.getDescription());
        }

        Instrument savedInstrument = instrumentRepository.save(existingInstrument);

        return ResponseEntity.ok(savedInstrument);
    }*/

    @DeleteMapping("/instruments/{id}")
    public ResponseEntity<?> deleteInstrument(@PathVariable String id) {
        if (!instrumentRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        instrumentRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}