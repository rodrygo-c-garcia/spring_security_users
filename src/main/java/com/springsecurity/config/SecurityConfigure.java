package com.springsecurity.config;

import com.springsecurity.config.filter.JwtTokenValidator;
import com.springsecurity.service.impl.UserDetailServiceImpl;
import com.springsecurity.util.JwtUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfigure {

    private JwtUtil jwtUtil;

    public SecurityConfigure(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    // filtros de seguridad
    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httppSecurity) throws Exception {
        // condiciones de seguridad
        return httppSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // recursos publicos
                    http.requestMatchers(HttpMethod.GET, "/api/v1/test/greeting").permitAll();
                    // recursos protegidos
                    http.requestMatchers(HttpMethod.GET, "/api/v1/test/data").hasAuthority("write");
                    http.requestMatchers(HttpMethod.PATCH, "/api/v1/test/refactor").hasAnyAuthority("WRITE", "REFACTOR");
                    // acceder por roles
                    http.requestMatchers(HttpMethod.PATCH, "/api/v1/test/refactor").hasRole("DEVELOPER");
                    // recursos no especificados
                    http.anyRequest().denyAll();
                })
                .build();
    }*/

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httppSecurity) throws Exception {
        // condiciones de seguridad
        return httppSecurity
                .csrf(csrf -> csrf.disable())
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // obtenemos el administrador de autenticacion
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
/*
    public static void main(String[] args) {
        List<Integer> numeros = Arrays.asList(1, 2, 3, 4, 5);

        int sumaCuadradosPares = numeros.stream()
                .filter(numero -> numero % 2 == 0) // Filtra los números pares
                .map(numero -> numero * numero) // Calcula el cuadrado de cada número
                .reduce(0, Integer::sum); // Suma los cuadrados
        System.out.println("La suma de los cuadrados de los números pares es: " + sumaCuadradosPares);

    }
    */
}
