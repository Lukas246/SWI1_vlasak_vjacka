package cz.vlasak_vjacka.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
public class InstrumentController {

    @Autowired
    private InstrumentRepository instrumentRepository;

    @GetMapping("/api/add")
    public String addInstrument(@RequestParam String name, @RequestParam Double price) {
        Instrument i = new Instrument();
        i.name = name;
        i.price = price;
        instrumentRepository.save(i);
        return "Saved: " + name + " for " + price;
    }
    @GetMapping("/api/status")
    public String getStatus() {
        return "Backend is running!";
    }

    @GetMapping("/api/all")
    public List<Instrument> getAll() {
        return instrumentRepository.findAll();
    }


}