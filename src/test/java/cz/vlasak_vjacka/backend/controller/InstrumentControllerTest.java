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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.context.ActiveProfiles;

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
class InstrumentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private InstrumentRepository instrumentRepository;

    @MockBean
    private UserRepository userRepository;

    private User testUser;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        testUser = new User();
        testUser.setId(userId);
        testUser.setUsername("testuser");
    }

    @Test
    void addInstrument_Success() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
        when(instrumentRepository.save(any(Instrument.class))).thenReturn(new Instrument());

        mockMvc.perform(post("/api/users/{userId}/instruments", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Guitar\",\"price\":1000.0,\"description\":\"Test\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void addInstrument_UserNotFound() throws Exception {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/users/{userId}/instruments", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Guitar\",\"price\":1000.0,\"description\":\"Test\"}"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Uživatel s ID " + userId + " nebyl nalezen!"));
    }

    @Test
    void getStatus() throws Exception {
        mockMvc.perform(get("/api/status"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Backend is running!"));
    }

    @Test
    void getAllInstruments() throws Exception {
        Instrument instrument = new Instrument();
        instrument.setName("Guitar");
        when(instrumentRepository.findAll()).thenReturn(List.of(instrument));

        mockMvc.perform(get("/api/instruments/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Guitar"));
    }
}
