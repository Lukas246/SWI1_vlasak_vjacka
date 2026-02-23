package cz.vlasak_vjacka.backend;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @Column(nullable = false, unique = true)
    public String username;

    @Column(nullable = false)
    public String email;

    public String password;

    // Pokud bys chtěl přidat roli (např. ADMIN, USER)
    public String role;
}