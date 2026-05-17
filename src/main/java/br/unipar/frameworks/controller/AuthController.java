package br.unipar.frameworks.controller;

import br.unipar.frameworks.dto.AuthResponse;
import br.unipar.frameworks.dto.LoginRequest;
import br.unipar.frameworks.dto.RegisterRequest;
import br.unipar.frameworks.dto.UserResponse;
import br.unipar.frameworks.model.User;
import br.unipar.frameworks.repository.UserRepository;
import br.unipar.frameworks.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email ja cadastrado"));
        }

        User user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("USER");
        return ResponseEntity.ok(UserResponse.from(userRepository.save(user)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return userRepository.findByEmail(request.email())
                .filter(user -> passwordEncoder.matches(request.password(), user.getPassword()))
                .<ResponseEntity<?>>map(user -> ResponseEntity.ok(new AuthResponse(
                        "Login realizado com sucesso",
                        jwtService.generateToken(user.getEmail(), user.getRole()),
                        UserResponse.from(user)
                )))
                .orElseGet(() -> ResponseEntity.status(401).body(Map.of(
                        "error", "Email ou senha inválidos"
                )));
    }
}
