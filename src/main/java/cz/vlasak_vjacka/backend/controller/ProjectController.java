package cz.vlasak_vjacka.backend.controller;

import cz.vlasak_vjacka.backend.model.Project;
import cz.vlasak_vjacka.backend.model.User;
import cz.vlasak_vjacka.backend.repository.ProjectRepository;
import cz.vlasak_vjacka.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public ProjectController(ProjectRepository projectRepository, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/{projectId}/add-user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','EDITOR')")
    public ResponseEntity<?> addUserToProject(@PathVariable UUID projectId, @PathVariable UUID userId) {
        Project project = projectRepository.findById(projectId).orElse(null);
        if (project == null) {
            return ResponseEntity.notFound().build();
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        // Přidáme uživatele do kolekce projektu
        project.getMembers().add(user);

        // Uložíme projekt, Hibernate automaticky zapíše do project_members
        projectRepository.save(project);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/remove-user/{userId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN','EDITOR')")
    public ResponseEntity<?> removeUserFromProject(
            @PathVariable UUID projectId,
            @PathVariable UUID userId
    ) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projekt nenalezen"));

        // Hibernate díky UUID a správně nastavenému equals/hashCode v entitě User
        // pozná, kterého uživatele má z kolekce vyhodit.
        project.getMembers().removeIf(user -> user.getId().equals(userId));

        projectRepository.save(project);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @PostMapping("/create")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project saved = projectRepository.save(project);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable UUID id) {
        return projectRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable UUID id, @RequestBody Project project) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        project.setId(id);
        Project saved = projectRepository.save(project);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteProject(@PathVariable UUID id) {
        if (!projectRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        projectRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
