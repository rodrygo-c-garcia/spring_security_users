package com.springsecurity.config.filter;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.springsecurity.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
// Filtro de JWT por cada solicitud
public class JwtTokenValidator extends OncePerRequestFilter {
    private JwtUtil jwtUtil;

    public JwtTokenValidator(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION); // obtener el jwt token del header (request)

        if (jwtToken != null ) {
//            eliminamos el prefijo Bearer
            jwtToken = jwtToken.substring(7);

            DecodedJWT decodedJWT = jwtUtil.validateToken(jwtToken);

            String username = jwtUtil.extractUsername(decodedJWT);
            String stringAuthorities = jwtUtil.getSpecificClaim(decodedJWT, "authorities").toString();

            // lista de autorizaciones por comas
            Collection<? extends GrantedAuthority> listAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(stringAuthorities);

            // setear el usuario en el Context Holder
            SecurityContext securityContext = SecurityContextHolder.getContext(); // extraer el contexto de spring
            // instanciamos el nuevo usuario verificado correctamente
            Authentication userAuthentication = new UsernamePasswordAuthenticationToken(username, null, listAuthorities);

            // dar acceso al usuario (agregar en el context holder)
            securityContext.setAuthentication(userAuthentication);
            SecurityContextHolder.setContext(securityContext);
        }
        // si el token no es enviado, pasa al siguiente filtro, por defecto el token es rechazado
        filterChain.doFilter(request, response);
    }
}
