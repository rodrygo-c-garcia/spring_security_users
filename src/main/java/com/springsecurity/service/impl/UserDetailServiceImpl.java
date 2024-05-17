package com.springsecurity.service.impl;

import com.springsecurity.persistence.entity.UserEntity;
import com.springsecurity.persistence.repository.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;

    // inyeccion de dependencia
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUserEntityName(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));

        List<SimpleGrantedAuthority> listAuthorities = new ArrayList<>();
        // Se agregan los roles
        userEntity.getRoles().forEach(role -> listAuthorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name()))));
        // Se agregan los permisos
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> listAuthorities.add(new SimpleGrantedAuthority(permission.getName())));
        return null;
    }
}
