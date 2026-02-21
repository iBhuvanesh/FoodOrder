package com.foodorder.auth.security;

import com.foodorder.auth.entity.Role;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;

@Service
public class JwtService {

    private final String secret;
    private final long expirationSeconds;

    public JwtService(@Value("${security.jwt.secret:foodorder-secret-key}") String secret,
                      @Value("${security.jwt.expiration-seconds:3600}") long expirationSeconds) {
        this.secret = secret;
        this.expirationSeconds = expirationSeconds;
    }

    public String generateToken(String email, Role role) {
        long exp = Instant.now().plusSeconds(expirationSeconds).getEpochSecond();
        String payload = email + ":" + role.name() + ":" + exp;
        String signature = hmac(payload);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(payload.getBytes(StandardCharsets.UTF_8)) + "." + signature;
    }

    public Map<String, String> parseAndValidate(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid token format");
        }
        String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
        String expectedSignature = hmac(payload);
        if (!expectedSignature.equals(parts[1])) {
            throw new IllegalArgumentException("Invalid token signature");
        }
        String[] payloadParts = payload.split(":");
        if (payloadParts.length != 3) {
            throw new IllegalArgumentException("Invalid token payload");
        }
        long exp = Long.parseLong(payloadParts[2]);
        if (Instant.now().getEpochSecond() > exp) {
            throw new IllegalArgumentException("Token expired");
        }
        return Map.of("email", payloadParts[0], "role", payloadParts[1]);
    }

    private String hmac(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to generate token signature", ex);
        }
    }
}
