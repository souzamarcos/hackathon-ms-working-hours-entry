package com.fiap.burger.gateway.order.gateway;

import com.fiap.burger.gateway.misc.ProductBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.ParameterizedTypeReference;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
@RunWith(SpringJUnit4ClassRunner.class)
class DefaultProductGatewayTest {
    @Mock
    RestTemplate restTemplate;
    @InjectMocks
    DefaultProductGateway gateway;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldFindByIds() {
        var expected = Arrays.asList(new ProductBuilder().withId(2L), new ProductBuilder().withId(20L));

        org.springframework.test.util.ReflectionTestUtils.setField(gateway, "apiProductHost", "http://localhost:8080");
        when(restTemplate.exchange(eq("http://localhost:8080/products?id=2,20"), eq(HttpMethod.GET),
            eq(null), any(ParameterizedTypeReference.class))).thenReturn(ResponseEntity.ok().body(expected));

        var actual = gateway.findByIds(Arrays.asList(2L, 20L));

        assertEquals(expected, actual);
    }
}
