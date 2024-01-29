package com.fiap.burger.gateway.order.gateway;

import com.fiap.burger.gateway.misc.CustomerBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)

public class DefaultCustomerGatewayTest {
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    DefaultCustomerGateway gateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindById() {
        var expected = new CustomerBuilder().build();

        org.springframework.test.util.ReflectionTestUtils.setField(gateway, "apiCustomerHost", "http://localhost:8083");
        when(restTemplate.exchange(eq("http://localhost:8083/customers/1"), eq(HttpMethod.GET),
            eq(null), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok().body(expected));

        var actual = gateway.findById("1");

        assertEquals(expected, actual);
    }

    @Test
    void shouldReturnNullWhenNotFound() {
        org.springframework.test.util.ReflectionTestUtils.setField(gateway, "apiCustomerHost", "http://localhost:8083");
        when(restTemplate.exchange(eq("http://localhost:8083/customers/1"), eq(HttpMethod.GET),
            eq(null), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.notFound().build());

        var actual = gateway.findById("1");

        assertNull(actual);
    }
}
