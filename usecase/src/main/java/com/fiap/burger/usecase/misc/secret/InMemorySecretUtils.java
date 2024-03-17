package com.fiap.burger.usecase.misc.secret;

import com.fiap.burger.usecase.misc.token.TokenJwtSecret;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

@Primary
@Service
public class InMemorySecretUtils implements SecretUtils{
    private static final String TOKEN_SECRET = "TEST-SECRET";
    private static final String TOKEN_ISSUER = "TEST-ISSUER";

    public TokenJwtSecret getTokenJwtSecret() {
        return new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER);
    }

}
