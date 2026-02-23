package cz.vlasak_vjacka.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InstrumentRepository instrumentRepository;

    @GetMapping("/api/users/add")
    public String addUser(@RequestParam String username, @RequestParam String email) {
        User u = new User();
        u.username = username;
        u.email = email;
        userRepository.save(u);
        return "User " + username + " was successfully created!";
    }

    @GetMapping("/api/users/all")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/api/users/my-instruments")
    public List<Instrument> getUserInstruments(@RequestParam Long userId) {
        // Najdeme uživatele
        User user = userRepository.findById(userId).orElse(null);

        if (user != null) {
            // Vrátíme seznam jeho nástrojů
            return user.instruments;
        }

        return null; // Nebo prázdný seznam List.of()
    }

    @GetMapping("/api/users/add-instrument")
    public String addInstrumentToUser(@RequestParam Long userId, @RequestParam String name, @RequestParam Double price) {
        // Najdeme uživatele
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) return "Chyba: Uživatel s ID " + userId + " neexistuje!";

        // Vytvoříme a propojíme nástroj
        Instrument i = new Instrument();
        i.name = name;
        i.price = price;
        i.owner = user; // Toto nastaví cizí klíč v databázi

        instrumentRepository.save(i);
        return "Nástroj '" + name + "' byl přiřazen uživateli " + user.username;
    }


}