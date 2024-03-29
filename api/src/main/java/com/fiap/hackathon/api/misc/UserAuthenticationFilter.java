package com.fiap.hackathon.api.misc;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fiap.hackathon.api.misc.token.TokenJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    private HandlerExceptionResolver resolver;

    private TokenJwtUtils jwtTokenService;

    public UserAuthenticationFilter(
        @Qualifier("handlerExceptionResolver") HandlerExceptionResolver resolver,
        @Autowired TokenJwtUtils jwtTokenService
    ) {
        this.resolver = resolver;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            // Verifica se o endpoint requer autenticação antes de processar a requisição
            if (checkIfEndpointIsAuthenticated(request)) {
                String token = recoveryToken(request); // Recupera o token do cabeçalho Authorization da requisição
                if (token != null) {
                    String employeeId = jwtTokenService.getEmployeeIdFromToken(token);
                    List<SimpleGrantedAuthority> authorities = jwtTokenService.getAuthoritiesFromToken(token);

                    // Cria um objeto de autenticação do Spring Security
                    Authentication authentication =
                            new UsernamePasswordAuthenticationToken(employeeId, null, authorities);

                    // Define o objeto de autenticação no contexto de segurança do Spring Security
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    throw new JWTVerificationException("Token de authenticação não informado.");
                }
            }
            filterChain.doFilter(request, response); // Continua o processamento da requisição
        } catch (Exception e) {
            resolver.resolveException(request, response, null, new JWTVerificationException(e.getMessage(), e));
        }
    }

    // Recupera o token do cabeçalho Authorization da requisição
    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    // Verifica se o endpoint requer autenticação antes de processar a requisição
    private boolean checkIfEndpointIsAuthenticated(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        return Arrays.asList(SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).contains(requestURI);
    }
}