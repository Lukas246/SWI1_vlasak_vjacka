package cz.osu.backend.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api") // Všechny cesty v tomto kontroleru budou začínat na /api
public class TestController {

    @GetMapping("/hello") // Tahle metoda se spustí při volání localhost:8080/api/hello
    public String sayHello() {
        return "Ahoj z backendu! Spojeni funguje.";
    }
}
