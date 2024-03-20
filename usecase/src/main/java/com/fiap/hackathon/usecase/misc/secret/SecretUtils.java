package com.fiap.hackathon.usecase.misc.secret;


import com.fiap.hackathon.usecase.misc.token.TokenJwtSecret;

public interface SecretUtils {

    TokenJwtSecret getTokenJwtSecret();
}
