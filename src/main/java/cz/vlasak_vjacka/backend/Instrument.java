package cz.vlasak_vjacka.backend;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "instruments")
public class Instrument {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    public UUID id;

    public String name;
    public Double price;

    @ManyToOne
    @JoinColumn(name = "user_id") // Název sloupce v DB tabulce instruments
    @JsonIgnore
    public User owner;
}