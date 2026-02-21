package com.foodorder.auth.security;

import com.foodorder.auth.entity.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class JwtServiceTest {

    @Test
    void shouldGenerateAndValidateToken() {
        JwtService jwtService = new JwtService("unit-test-secret", 300);
        String token = jwtService.generateToken(10L, "user@test.com", Role.USER);

        Map<String, String> claims = jwtService.parseAndValidate(token);

        Assertions.assertEquals("10", claims.get("userId"));
        Assertions.assertEquals("user@test.com", claims.get("email"));
        Assertions.assertEquals("USER", claims.get("role"));
    }
}
