package cz.vlasak_vjacka.backend.controller;

import cz.vlasak_vjacka.backend.model.Project;
import cz.vlasak_vjacka.backend.model.User;
import cz.vlasak_vjacka.backend.repository.ProjectRepository;
import cz.vlasak_vjacka.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<?> addUserToProject(@PathVariable String projectId, @PathVariable String userId) {
        Project project = projectRepository.findById(projectId).orElseThrow();
        User user = userRepository.findById(UUID.fromString(userId)).orElseThrow();

        project.addMember(user);
        projectRepository.save(project);

        return ResponseEntity.ok("Uživatel přidán do projektu");
    }

    @GetMapping("/all")
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
}
