package com.springsecurity.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.springsecurity.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtTokenValidator extends OncePerRequestFilter {
    private JwtUtil jwtUtil;

    public JwtTokenValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken != null ) {
            jwtToken = jwtToken.substring(7);
            DecodedJWT decodedJWT = jwtUtil.validateToken(jwtToken);
            String username = jwtUtil.extractUsername(decodedJWT);
            String stringAuthorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").toString();
            

        }
        filterChain.doFilter(request, response);
    }
}
