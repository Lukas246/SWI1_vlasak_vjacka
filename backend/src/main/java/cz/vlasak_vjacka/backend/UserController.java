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
}