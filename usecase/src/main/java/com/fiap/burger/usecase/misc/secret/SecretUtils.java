package com.fiap.burger.usecase.misc.secret;


import com.fiap.burger.usecase.misc.exception.SecretAwsException;
import com.fiap.burger.usecase.misc.token.TokenJwtSecret;
import com.google.gson.Gson;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

public interface SecretUtils {

    TokenJwtSecret getTokenJwtSecret();
}
