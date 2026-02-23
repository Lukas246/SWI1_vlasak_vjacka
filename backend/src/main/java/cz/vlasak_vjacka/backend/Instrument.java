package cz.vlasak_vjacka.backend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "instruments")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;
    public Double price;

    @ManyToOne
    @JoinColumn(name = "user_id") // Název sloupce v DB tabulce instruments
    @JsonIgnore
    public User owner;
}