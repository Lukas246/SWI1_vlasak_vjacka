package cz.vlasak_vjacka.backend.service;

import cz.vlasak_vjacka.backend.model.User;
import cz.vlasak_vjacka.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 1. Místo findAll().stream() použijeme novou efektivní metodu
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Uživatel nenalezen: " + username));

        // Ladící výpisy (později je můžeš smazat)
        System.out.println("Hledám uživatele: " + username);
        System.out.println("Heslo v DB (hash): " + user.getPassword());

        // 2. Dynamické zpracování role z databáze
        String userRole = user.getRole();
        if (userRole == null) userRole = "USER"; // Defaultní role, pokud by v DB chyběla

        if (!userRole.startsWith("ROLE_")) {
            userRole = "ROLE_" + userRole;
        }

        // 3. Sestavení UserDetails pro Spring Security
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Musí to být BCrypt hash!
                .authorities(userRole)        // Použije roli z DB (např. ROLE_ADMIN)
                .build();
    }
}