package com.fiap.hackathon.api.misc;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JwtTokenService {

    private static final String SECRET_KEY = "4Z^XrroxR@dWxqf$mTTKwW$!@#qGr4P"; // Chave secreta utilizada para gerar e verificar o token

    private static final String ISSUER = "pizzurg-api"; // Emissor do token

    private static final JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(SECRET_KEY)).withIssuer(ISSUER).build();

    public String getSubjectFromToken(String token) {
        try {
            return jwtVerifier
                    .verify(token) // Verifica a validade do token
                    .getSubject(); // Obtém o assunto (neste caso, o nome de usuário) do token
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
    }

    public String getEmployeeIdFromToken(String token) {
        try {
            return jwtVerifier
                    .verify(token)
                    .getClaim("id")
                    .asString();
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromToken(String token) {
        try {
            return List.of(new SimpleGrantedAuthority(
                jwtVerifier
                    .verify(token)
                    .getClaim("type")
                    .asString())
            );
        } catch (JWTVerificationException exception){
            throw new JWTVerificationException("Token inválido ou expirado.");
        }
    }
}