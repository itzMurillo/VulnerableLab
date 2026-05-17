package br.unipar.frameworks.controller;

import br.unipar.frameworks.dto.UserResponse;
import br.unipar.frameworks.model.User;
import br.unipar.frameworks.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable Long id, Authentication authentication) {
        User user = userRepository.findById(id).orElseThrow();
        if (!isAdmin(authentication) && !user.getEmail().equals(authentication.getName())) {
            return ResponseEntity.status(403).build();
        }
        return ResponseEntity.ok(UserResponse.from(user));
    }

    @GetMapping("/search-safe")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> safeSearch(@RequestParam String term) {
        return searchUsers(term);
    }

    @GetMapping("/search-unsafe")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> unsafeSearch(@RequestParam String term) {
        return searchUsers(term);
    }

    private List<UserResponse> searchUsers(String term) {
        return userRepository.safeSearchByName(term).stream()
                .map(UserResponse::from)
                .toList();
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> "ROLE_ADMIN".equals(authority.getAuthority()));
    }
}
