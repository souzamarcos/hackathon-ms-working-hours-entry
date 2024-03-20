package com.fiap.hackathon.usecase.misc.secret;


import com.fiap.hackathon.usecase.misc.exception.SecretAwsException;
import com.fiap.hackathon.usecase.misc.profiles.Production;
import com.google.gson.Gson;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.secretsmanager.SecretsManagerClient;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueRequest;
import software.amazon.awssdk.services.secretsmanager.model.GetSecretValueResponse;

@Production
@Primary
@Service
public class DefaultSecretUtils implements SecretUtils {
    private static final String TOKEN_JWT_SECRET_NAME = "token_jwt";

    private static final SecretsManagerClient secretClient = SecretsManagerClient.builder()
        .region(Region.US_EAST_1)
        .build();

    private DefaultSecretUtils() {

    }

    public TokenJwtSecret getTokenJwtSecret() {
        TokenJwtSecret token = new Gson().fromJson(DefaultSecretUtils.getSecret(TOKEN_JWT_SECRET_NAME), TokenJwtSecret.class);
        token.isValid();
        return token;
    }

    public static String getSecret(String secretName) {
        try {

            GetSecretValueRequest getSecretValueRequest = GetSecretValueRequest.builder()
                .secretId(secretName)
                .build();

            GetSecretValueResponse getSecretValueResponse;
                getSecretValueResponse = secretClient.getSecretValue(getSecretValueRequest);

                return getSecretValueResponse.secretString();
        } catch (Exception e) {
            throw new SecretAwsException("Error while trying to read secret: " + secretName, e);
        }
    }
}
