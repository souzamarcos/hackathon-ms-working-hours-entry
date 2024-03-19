package com.fiap.hackathon.usecase.misc.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.hackathon.usecase.misc.exception.TokenJwtException;
import com.fiap.hackathon.usecase.misc.secret.SecretUtils;
import org.springframework.stereotype.Service;

@Service
public class TokenJwtUtils {

    private final SecretUtils secretUtils;

    public TokenJwtUtils(SecretUtils secretUtils) {
        this.secretUtils = secretUtils;
    }

    public DecodedJWT readToken(String token) {
        try {
            JWTVerifier verifier = buildJwtVerifier();
            return verifier.verify(token);
        } catch (TokenExpiredException exception) {
            throw new TokenJwtException("Token JWT expired at " + exception.getExpiredOn(), exception);
        } catch (Exception exception) {
            throw new TokenJwtException("Token JWT Error: " + exception.getMessage(), exception);
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
