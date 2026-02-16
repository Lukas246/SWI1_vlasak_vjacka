package cz.vlasak_vjacka.backend;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("/api/status")
    public String getStatus() {
        return "Backend is running and connected to MariaDB!";
    }
}