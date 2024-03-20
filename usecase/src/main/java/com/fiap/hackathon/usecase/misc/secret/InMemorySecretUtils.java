package com.fiap.hackathon.usecase.misc.secret;

import com.fiap.hackathon.usecase.misc.profiles.NotProduction;
import org.springframework.stereotype.Service;

@NotProduction
@Service
public class InMemorySecretUtils implements SecretUtils{
    private static final String TOKEN_SECRET = "TEST-SECRET";
    private static final String TOKEN_ISSUER = "TEST-ISSUER";

    public TokenJwtSecret getTokenJwtSecret() {
        return new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER);
    }

}
