package com.fiap.hackathon.api.misc.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.hackathon.usecase.misc.secret.SecretUtils;
import com.fiap.hackathon.usecase.misc.secret.TokenJwtSecret;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TokenJwtUtils {

    private final SecretUtils secretUtils;

    public TokenJwtUtils(SecretUtils secretUtils) {
        this.secretUtils = secretUtils;
    }

    public String getEmployeeIdFromToken(String token) {
        return Optional.ofNullable(readToken(token)
                .getClaim("id")
                .asString()).orElseThrow(() -> new JWTVerificationException("Token claim 'id' required"));
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        String type = Optional.ofNullable(readToken(token).getClaim("type").asString())
                .orElseThrow(() -> new JWTVerificationException("Token claim 'type' required"));
        return List.of(new SimpleGrantedAuthority(type));
    }

    public DecodedJWT readToken(String token) {
        try {
            JWTVerifier verifier = buildJwtVerifier();
            return verifier.verify(token);
        } catch (TokenExpiredException exception) {
            throw new JWTVerificationException("Token JWT expired at " + exception.getExpiredOn(), exception);
        } catch (Exception exception) {
            throw new JWTVerificationException("Token JWT Error: " + exception.getMessage(), exception);
        }
    }

    private JWTVerifier buildJwtVerifier() {
        TokenJwtSecret jwtSecret = secretUtils.getTokenJwtSecret();
        return JWT.require(buildAlgorithm(jwtSecret.getSecret()))
            .withIssuer(jwtSecret.getIssuer())
            .build();
    }

    private static Algorithm buildAlgorithm(String secret) {
        return Algorithm.HMAC256(secret);
    }
}
