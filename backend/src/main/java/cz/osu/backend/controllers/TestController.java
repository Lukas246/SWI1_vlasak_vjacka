package cz.osu.backend.controllers;

import cz.osu.backend.entities.User;
import cz.osu.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Všechny cesty v tomto kontroleru budou začínat na /api
public class TestController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/hello")
    public String sayHello() {
        return "Ahoj z backendu! Spojeni funguje.";
    }

    @GetMapping("/create-user")
    public String createUser() {
        User user = new User("johndoe", "john@example.com", "password123", "John", "Doe");
        userRepository.save(user);
        return "Uživatel John Doe byl úspěšně vytvořen a uložen do databáze.";
    }
}
