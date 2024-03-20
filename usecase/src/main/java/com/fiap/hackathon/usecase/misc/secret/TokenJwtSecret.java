package com.fiap.hackathon.usecase.misc.secret;

import com.fiap.hackathon.usecase.misc.exception.SecretAwsException;
import com.google.gson.annotations.SerializedName;

import java.util.Optional;

public class TokenJwtSecret {
    @SerializedName("token_jwt_secret")
    private final String secret;
    @SerializedName("token_jwt_issuer")
    private final String issuer;

    public String getSecret() {
        return secret;
    }

    public String getIssuer() {
        return issuer;
    }

    public TokenJwtSecret(String secret, String issuer) {
        this.secret = secret;
        this.issuer = issuer;
    }

    public void isValid() {
        if(Optional.ofNullable(secret).orElse("").isEmpty() ||
            Optional.ofNullable(issuer).orElse("").isEmpty()) {
            throw new SecretAwsException("Secret is malformed.");
        }
    }
}
