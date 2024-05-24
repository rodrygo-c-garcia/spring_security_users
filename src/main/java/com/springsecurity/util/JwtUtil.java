package com.springsecurity.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${security.jwt.key.private}")
    private String SECRETKEY;

    @Value("${security.jwt.user.generator}")
    private String userGeneratorToken;

    public String generateToken(Authentication authentication) {
//        algoritmo de encriptacion (firma del token)
        Algorithm algorithm = Algorithm.HMAC256(this.SECRETKEY);
//        sujeto del token (usuario)
        String username = (String) authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream()
                .map(grantedAuthority -> grantedAuthority.getAuthority())
                .collect(Collectors.joining(","));

        String token = JWT.create()
//                usuario que genero el token (backend)
                .withIssuer(this.userGeneratorToken)
//                usuario al que esta dirigido el token (frontend)
                .withSubject(username)
//                permisos que tiene el usuario (claims)
                .withClaim("authorities", authorities)
//                momento acuala en el que se genero el token
                .withIssuedAt(new Date())
                .withExpiresAt(new Date (System.currentTimeMillis() + 60000))
                .withJWTId(UUID.randomUUID().toString())
//                apartir de que fecha es valido el token
                .withNotBefore(new Date(System.currentTimeMillis()))
                .sign(algorithm);
        return token;
    }
}