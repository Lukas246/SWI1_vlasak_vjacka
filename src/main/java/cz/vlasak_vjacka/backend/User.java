package cz.vlasak_vjacka.backend;

import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false, columnDefinition = "VARCHAR(36)")
    public UUID id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    public String email;

    public String password;

    // Pokud bys chtěl přidat roli (např. ADMIN, USER)
    public String role;

    // Ve třídě User.java
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    public List<Instrument> instruments;
}