package com.springsecurity;

import com.springsecurity.persistence.entity.PermissionEntity;
import com.springsecurity.persistence.entity.RoleEnum;
import com.springsecurity.persistence.entity.RolesEntity;
import com.springsecurity.persistence.entity.UserEntity;
import com.springsecurity.persistence.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

    // injectamos el repositorio de usuarios, este Bean se ejecutará al iniciar la aplicación
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return args -> {
            // creamos los permisos
            PermissionEntity createPermission = PermissionEntity.builder().name("CREATE").build();
            PermissionEntity readPermission = PermissionEntity.builder().name("READ").build();
            PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE").build();
            PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE").build();
            PermissionEntity refactorPermission = PermissionEntity.builder().name("REFACTOR").build();
            // creamos los roles
            RolesEntity adminRole = RolesEntity.builder()
                    .role(RoleEnum.ADMIN)
                    // Agregamos los permisos al rol
                    .permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission))
                    .build();
            RolesEntity userRole = RolesEntity.builder()
                    .role(RoleEnum.USER)
                    .permissions(Set.of(createPermission, readPermission))
                    .build();
            RolesEntity invitedRole = RolesEntity.builder()
                    .role(RoleEnum.INVITED)
                    .permissions(Set.of(readPermission))
                    .build();
            RolesEntity developerRole = RolesEntity.builder()
                    .role(RoleEnum.DEVELOPER)
                    .permissions(Set.of(createPermission, readPermission, updatePermission, deletePermission, refactorPermission))
                    .build();

            // Creamos los usuarios
            UserEntity userSantiago = UserEntity.builder()
                    .username("Santiago Santorini")
                    .password("$2a$10$FeTzWqB8spJPD69PPchpfOgVUWV/egUkdpxvL9WIVh2rhZwjnFXfK") // 12345
                    .isEnable(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(adminRole))
                    .build();
            // Creamos los usuarios
            UserEntity userDaniel = UserEntity.builder()
                    .username("Daniel Campos")
                    .password("$2a$10$7NCJSHE4sk3ycQWWhWvrqu.yVsILVwq1/7RAKJVnUvY1/oApG/0iq") // 4321
                    .isEnable(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(userRole))
                    .build();
            // Creamos los usuarios
            UserEntity userAndrea = UserEntity.builder()
                    .username("Andrea Magalles")
                    .password("$2a$10$FeTzWqB8spJPD69PPchpfOgVUWV/egUkdpxvL9WIVh2rhZwjnFXfK") // 12345
                    .isEnable(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(invitedRole))
                    .build();
            // Creamos los usuarios
            UserEntity userAngie = UserEntity.builder()
                    .username("Angie")
                    .password("$2a$10$CQfUyKLSlnT0evcbHwTo6OkPgwpCd2nz1bgvlpmYX/0ZGSa/GShUK") // 54321
                    .isEnable(true)
                    .accountNoExpired(true)
                    .accountNoLocked(true)
                    .credentialNoExpired(true)
                    .roles(Set.of(developerRole))
                    .build();
            userRepository.saveAll(List.of(userSantiago, userDaniel, userAndrea, userAngie));

            System.out.println(userAngie.getRoles());
        };
    }
}
