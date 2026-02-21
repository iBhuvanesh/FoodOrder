package com.foodorder.auth.dto;

public record AuthResponse(String token, String email, String role) {
}
