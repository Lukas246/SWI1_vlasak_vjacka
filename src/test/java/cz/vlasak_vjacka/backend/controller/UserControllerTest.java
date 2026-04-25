package cz.vlasak_vjacka.backend.controller;

import cz.vlasak_vjacka.backend.model.Instrument;
import cz.vlasak_vjacka.backend.model.User;
import cz.vlasak_vjacka.backend.repository.InstrumentRepository;
import cz.vlasak_vjacka.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private InstrumentRepository instrumentRepository;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setRole("USER");
    }

    @Test
    void addUser_Success() throws Exception {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"pass\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    void addUser_EmailExists() throws Exception {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        mockMvc.perform(post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"pass\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email už je v systému!"));
    }

    @Test
    void addUser_UsernameExists() throws Exception {
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        mockMvc.perform(post("/api/users/add")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"testuser\",\"email\":\"test@example.com\",\"password\":\"pass\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Uživatelské jméno už je v systému!"));
    }

    @Test
    void getAllUsers() throws Exception {
        when(userRepository.findAll()).thenReturn(List.of(testUser));

        mockMvc.perform(get("/api/users/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testuser"));
    }

    @Test
    void getUserInstruments_UserExists() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setName("Guitar");
        testUser.setInstruments(List.of(instrument));

        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        mockMvc.perform(get("/api/users/my-instruments")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Guitar"));
    }

    @Test
    void getUserInstruments_UserNotFound() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/my-instruments")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("[]")); // Now returns empty list
    }

    @Test
    void addInstrumentToUser_Success() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(instrumentRepository.save(any(Instrument.class))).thenReturn(new Instrument());

        mockMvc.perform(get("/api/users/add-instrument")
                .param("userId", userId.toString())
                .param("name", "Piano")
                .param("price", "1000.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Nástroj 'Piano' byl přiřazen uživateli testuser"));
    }

    @Test
    void addInstrumentToUser_UserNotFound() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/users/add-instrument")
                .param("userId", userId.toString())
                .param("name", "Piano")
                .param("price", "1000.0"))
                .andExpect(status().isOk())
                .andExpect(content().string("Chyba: Uživatel s ID " + userId + " neexistuje!"));
    }
}
