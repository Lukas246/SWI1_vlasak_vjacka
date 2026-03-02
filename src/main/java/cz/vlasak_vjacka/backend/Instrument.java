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
    private UUID id;

    public String name;
    public Double price;
    public String description;

    @ManyToOne
    @JoinColumn(name = "user_id") // Název sloupce v DB tabulce instruments
    @JsonIgnore
    public User owner;

    public void setOwner(User owner) {
        this.owner = owner;
    }
    public void getOwner(User owner) {
        this.owner = owner;
    }
}

