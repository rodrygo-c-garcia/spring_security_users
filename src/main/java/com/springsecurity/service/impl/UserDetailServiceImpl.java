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
    private final JwtUtil jwtUtil;

    // inyeccion de dependencia
    public UserDetailServiceImpl(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + username));

        List<SimpleGrantedAuthority> listAuthorities = new ArrayList<>();
        // Se agregan los roles
        userEntity.getRoles().forEach(role -> listAuthorities.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name()))));
        // Se agregan los permisos
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> listAuthorities.add(new SimpleGrantedAuthority(permission.getName())));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnable(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                listAuthorities);
    }
}
/*
* SELECT u.username, r.role_name, p.name AS permission_name
* FROM users u
* INNER JOIN users_roles ur ON u.id = ur.user_id
* INNER JOIN roles r ON ur.role_id = r.id
* INNER JOIN roles_permissions rp ON r.id = rp.role_id
* INNER JOIN permissions p ON rp.permission_id = p.id;
*
*
* */