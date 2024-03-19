package com.fiap.hackathon.usecase.misc.token;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.fiap.hackathon.usecase.misc.exception.TokenJwtException;
import com.fiap.hackathon.usecase.misc.secret.SecretUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class TokenJwtUtilsTest {
    private static final String TOKEN_SECRET = "TEST-SECRET";
    private static final String TOKEN_ISSUER = "TEST-ISSUER";


    @Mock
    SecretUtils secretUtils;

    @InjectMocks
    TokenJwtUtils tokenJwtUtils;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void readTokenOkay() {
        var expected = "1";
        String validToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdWRpZW5jZSIsImlzcyI6IlRFU1QtSVNTVUVSIiwiY3VzdG9tZXJJZCI6IjEiLCJjcGYiOiIxMjM0NTY3ODkwMSIsImlhdCI6MTcwNjQxMzQzMiwianRpIjoiZjA0MjNlNGMtN2MxMC00YjM1LThkOTUtZGJmYWZjM2NmZGE0In0.ofm4-HItY30TdyIzbqGgo-KXZf-OIlwhQckryBt52S8";

        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        DecodedJWT result = tokenJwtUtils.readToken(validToken);
        assertEquals(expected, result.getClaim("customerId").asString());
    }

    @Test
    void readTokenExpired() {
        String expiredToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdWRpZW5jZSIsImNsaWVudElkIjoxLCJpc3MiOiJURVNULUlTU1VFUiIsImNwZiI6IjE2NTY1ODI0NzM4IiwiZXhwIjoxNjk4NjI4MjE2LCJpYXQiOjE2OTg2MjgxNTYsImp0aSI6ImRmYjM1OWFjLTI3MGItNDUxMC05YjcxLTZiYjE3YzI5MzQ1NCJ9.iVcSUx-UShDr1rSSJt2lweRDcwtM-AkRX2buCeI5e0E";

        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret(TOKEN_SECRET, TOKEN_ISSUER));

        assertThrows(TokenJwtException.class, () -> tokenJwtUtils.readToken(expiredToken));
    }

    @Test
    void readTokenInvalidSecretAndIssuer() {
        String validToken = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJBdWRpZW5jZSIsImNsaWVudElkIjoxLCJpc3MiOiJURVNULUlTU1VFUiIsImNwZiI6IjE2NTY1ODI0NzM4IiwiaWF0IjoxNjk4NjI1OTYxLCJqdGkiOiJhNzIyYTBiNC1lY2M5LTQ2ZDQtOTRhYy00Mzg1NzI1YTAxOTcifQ.CuXTgz2VE-5ThjQRHMQtZ3iZE5zz3JV0vji5urdqrPI";

        when(secretUtils.getTokenJwtSecret()).thenReturn(new TokenJwtSecret("INVALID-SECRET", "INVALID-ISSUER"));

        assertThrows(TokenJwtException.class, () -> tokenJwtUtils.readToken(validToken));
    }
}
