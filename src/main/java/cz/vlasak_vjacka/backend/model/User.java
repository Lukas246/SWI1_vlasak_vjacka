package cz.vlasak_vjacka.backend.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id; // Změněno na private

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column()
    private String role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Instrument> instruments;

    // User.java

    @ManyToMany(fetch = FetchType.EAGER) // EAGER zajistí, že se projekty načtou hned s uživatelem
    @JoinTable(
            name = "project_members",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    // Pomocná metoda pro synchronizaci obou stran
    public void addProject(Project project) {
        this.projects.add(project);
        project.getMembers().add(this);
    }

    // DŮLEŽITÉ: Přidej Getter, jinak se pole v JSONu neobjeví
    public Set<Project> getProjects() {
        return projects;
    }

    public void setProjects(Set<Project> projects) {
        this.projects = projects;
    }

    // Prázdný konstruktor (povinný pro JPA)
    public User() {}

    // Gettery a Settery (Povinné pro Spring/Jackson JSON mapování)
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public List<Instrument> getInstruments() { return instruments; }
    public void setInstruments(List<Instrument> instruments) { this.instruments = instruments; }
}