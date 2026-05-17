package br.unipar.frameworks.dto;

public record AuthResponse(String message, String token, UserResponse user) {
}
