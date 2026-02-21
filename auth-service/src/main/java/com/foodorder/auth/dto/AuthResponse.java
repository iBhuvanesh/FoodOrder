package com.foodorder.auth.dto;

public record AuthResponse(Long userId, String token, String email, String role) {
}
