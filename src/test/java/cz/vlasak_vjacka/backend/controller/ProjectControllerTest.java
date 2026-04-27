package cz.vlasak_vjacka.backend.controller;

import cz.vlasak_vjacka.backend.model.Project;
import cz.vlasak_vjacka.backend.model.User;
import cz.vlasak_vjacka.backend.repository.ProjectRepository;
import cz.vlasak_vjacka.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectRepository projectRepository;

    @MockBean
    private UserRepository userRepository;

    private Project testProject;
    private User testUser;
    private UUID projectId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        projectId = UUID.randomUUID();
        userId = UUID.randomUUID();
        testProject = new Project();
        testProject.setId(projectId);
        testProject.setName("Test Project");

        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("testuser");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addUserToProject_Success() throws Exception {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        mockMvc.perform(post("/api/projects/{projectId}/add-user/{userId}", projectId, userId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void addUserToProject_ProjectNotFound() throws Exception {
        when(projectRepository.findById(projectId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/projects/{projectId}/add-user/{userId}", projectId, userId))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void removeUserFromProject_Success() throws Exception {
        when(projectRepository.findById(projectId)).thenReturn(Optional.of(testProject));
        when(projectRepository.save(any(Project.class))).thenReturn(testProject);

        mockMvc.perform(post("/api/projects/{projectId}/remove-user/{userId}", projectId, userId))
                .andExpect(status().isOk());
    }

    @Test
    void getAllProjects() throws Exception {
        when(projectRepository.findAll()).thenReturn(List.of(testProject));

        mockMvc.perform(get("/api/projects/all"))
                .andExpect(status().isOk());
    }
}
