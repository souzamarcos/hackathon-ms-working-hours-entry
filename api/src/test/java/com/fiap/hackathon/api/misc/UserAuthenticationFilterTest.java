package com.fiap.hackathon.api.misc;

import com.fiap.hackathon.api.misc.token.TokenJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

import static com.fiap.hackathon.api.misc.SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_REQUIRED;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;


class UserAuthenticationFilterTest {

    @Mock
    HttpServletRequest request;
    @Mock
    HttpServletResponse response;

    @Mock
    HandlerExceptionResolver resolver;

    @Mock
    TokenJwtUtils jwtTokenService;

    @Mock
    FilterChain chain;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.getContext().setAuthentication(null);
    }

    @Test
    void shouldFilterRequestWithTokenCorrectly() throws ServletException, IOException {
        var token = "token";
        var employeeId = "123";
        var type = "USER";
        var authorities = List.of(new SimpleGrantedAuthority(type));
        var expected = new UsernamePasswordAuthenticationToken(employeeId, null, authorities);


        when(request.getRequestURI()).thenReturn(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED[0]);
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtTokenService.getEmployeeIdFromToken(token)).thenReturn(employeeId);
        when(jwtTokenService.getAuthoritiesFromToken(token)).thenReturn(authorities);

        var userFilter = new UserAuthenticationFilter(resolver, jwtTokenService);

        userFilter.doFilter(request, response, chain);

        assertEquals(expected, SecurityContextHolder.getContext().getAuthentication());

        verify(chain).doFilter(request, response);
        verifyNoMoreInteractions(chain);
        verify(jwtTokenService, times(1)).getEmployeeIdFromToken(token);
        verify(jwtTokenService, times(1)).getAuthoritiesFromToken(token);
    }

    @Test
    void shouldFilterRequestWithoutTokenAndSendExceptionToResolver() throws ServletException, IOException {
        UsernamePasswordAuthenticationToken expected = null;

        when(resolver.resolveException(eq(request), eq(response), eq(null), any())).thenReturn(new ModelAndView());
        when(request.getRequestURI()).thenReturn(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED[0]);
        when(request.getHeader("Authorization")).thenReturn(null);

        var userFilter = new UserAuthenticationFilter(resolver, jwtTokenService);

        userFilter.doFilter(request, response, chain);

        assertEquals(null, SecurityContextHolder.getContext().getAuthentication());

        verify(resolver).resolveException(eq(request), eq(response), eq(null), any());
        verify(chain, times(0)).doFilter(request, response);
    }
}