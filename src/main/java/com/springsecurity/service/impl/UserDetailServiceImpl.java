package com.springsecurity.service.impl;

import com.springsecurity.controller.dto.AuthCreateUserRequest;
import com.springsecurity.controller.dto.AuthLoginRequest;
import com.springsecurity.controller.dto.AuthResponse;
import com.springsecurity.persistence.entity.RoleEntity;
import com.springsecurity.persistence.entity.UserEntity;
import com.springsecurity.persistence.repository.RoleRepository;
import com.springsecurity.persistence.repository.UserRepository;
import com.springsecurity.util.JwtUtil;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    // inyeccion de dependencia
    public UserDetailServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
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

    public AuthResponse loginUser (AuthLoginRequest authLoginRequest) {
        String username = authLoginRequest.username();
        String password = authLoginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        String accessToken = jwtUtil.generateToken(authentication);
        return new AuthResponse(username, "User authenticated", accessToken, true);
    }

    public Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);

        if(userDetails == null) {
            throw new BadCredentialsException("Invalid username or password.");
        }

        if(!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid password.");
        }

        return new UsernamePasswordAuthenticationToken(username, userDetails.getPassword(), userDetails.getAuthorities());
    }

    public AuthResponse createUser(AuthCreateUserRequest authCreateUserRequest){
        String username = authCreateUserRequest.username();
        String password = authCreateUserRequest.password();
        List<String> rolesList = authCreateUserRequest.roleCreateUser().roleListName();

        Set<RoleEntity> roleEntityList = roleRepository.findRoleEntitiesByRoleIn(rolesList)
                .stream()
                .collect(Collectors.toSet());

        if(roleEntityList.isEmpty()){
            throw new IllegalArgumentException("Roles not found");
        }

        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntityList)
                .isEnable(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userCreated = userRepository.save(userEntity);
        ArrayList<SimpleGrantedAuthority> authorityList = new ArrayList<>();

        userCreated.getRoles().forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRole().name()))));

        userCreated.getRoles()
                .stream()
                .flatMap(role -> role.getPermissions().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));


        Authentication authentication = new UsernamePasswordAuthenticationToken(userCreated, null, authorityList);
        String token = jwtUtil.generateToken(authentication);
        return new AuthResponse(userCreated.getUsername(), "User created", token, true);
    }

}
//SELECT u.username, r.role_name, p.name AS permission_name FROM users u INNER JOIN users_roles ur ON u.id = ur.user_id INNER JOIN roles r ON ur.role_id = r.id INNER JOIN roles_permissions rp ON r.id = rp.role_id INNER JOIN permissions p ON rp.permission_id = p.id;
