package com.fiap.burger.usecase.misc.secret;


import com.fiap.burger.usecase.misc.token.TokenJwtSecret;

public interface SecretUtils {

    TokenJwtSecret getTokenJwtSecret();
}
