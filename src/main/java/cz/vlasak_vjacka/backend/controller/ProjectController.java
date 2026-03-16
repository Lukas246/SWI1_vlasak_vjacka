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
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> addUserToProject(@PathVariable UUID projectId, @PathVariable UUID userId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Projekt nenalezen"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Uživatel nenalezen"));

        // Přidáme uživatele do kolekce projektu
        project.getMembers().add(user);

        // Uložíme projekt, Hibernate automaticky zapíše do project_members
        projectRepository.save(project);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{projectId}/remove-user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
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
}
