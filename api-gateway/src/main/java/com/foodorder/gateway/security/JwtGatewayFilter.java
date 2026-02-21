package com.foodorder.gateway.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Component
public class JwtGatewayFilter implements GlobalFilter, Ordered {

    private final String secret;

    public JwtGatewayFilter(@Value("${security.jwt.secret:foodorder-local-dev-secret}") String secret) {
        this.secret = secret;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();

        if (path.startsWith("/api/auth/login") || path.startsWith("/api/auth/register") || path.startsWith("/actuator")) {
            return chain.filter(exchange);
        }

        String authHeader = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        String token = authHeader.substring(7);
        try {
            String[] claims = parse(token);
            String email = claims[0];
            String role = claims[1];

            if (isRestaurantManagementRequest(exchange) && !"RESTAURANT_ADMIN".equals(role)) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            if ((path.startsWith("/api/orders") || path.startsWith("/api/cart") || path.startsWith("/api/payments")) && !role.equals("USER") && !role.equals("RESTAURANT_ADMIN")) {
                exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                return exchange.getResponse().setComplete();
            }

            ServerHttpRequest mutated = exchange.getRequest().mutate()
                    .header("X-Auth-Email", email)
                    .header("X-Auth-Role", role)
                    .build();

            return chain.filter(exchange.mutate().request(mutated).build());
        } catch (Exception ex) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }
    }

    private boolean isRestaurantManagementRequest(ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        String method = exchange.getRequest().getMethod() != null ? exchange.getRequest().getMethod().name() : "GET";

        if ("GET".equals(method)) {
            return false;
        }
        return path.startsWith("/api/restaurants") || path.startsWith("/api/menu");
    }

    private String[] parse(String token) {
        String[] parts = token.split("\\.");
        if (parts.length != 2) throw new IllegalArgumentException("Invalid token");

        String payload = new String(Base64.getUrlDecoder().decode(parts[0]), StandardCharsets.UTF_8);
        String expectedSig = hmac(payload);
        if (!expectedSig.equals(parts[1])) throw new IllegalArgumentException("Invalid signature");

        String[] payloadParts = payload.split(":");
        if (payloadParts.length != 3) throw new IllegalArgumentException("Invalid payload");

        long exp = Long.parseLong(payloadParts[2]);
        if (Instant.now().getEpochSecond() > exp) throw new IllegalArgumentException("Expired");

        return payloadParts;
    }

    private String hmac(String payload) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
        } catch (Exception ex) {
            throw new IllegalStateException("Failed signing", ex);
        }
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
