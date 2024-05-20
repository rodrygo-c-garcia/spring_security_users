# Ejemplo de Spring Security con Usuarios y Roles

Este repositorio proporciona un ejemplo práctico de cómo implementar seguridad en una aplicación Spring utilizando Spring Security. La aplicación permite la gestión de usuarios, roles y permisos, asegurando el acceso a los recursos de la aplicación (controladores) de manera granular.

## Características

- **Usuarios y Roles**: Cada usuario puede tener uno o varios roles, y cada rol puede tener uno o varios permisos asociados.
- **Control de Acceso**: Utiliza Spring Security para controlar el acceso a los recursos de la aplicación mediante anotaciones y configuración de seguridad.
- **Cadena de Filtros**: Implementación de una cadena de filtros para procesar las solicitudes entrantes.
- **Proveedor de Autenticación**: Uso de `DaoAuthenticationProvider` para autenticar usuarios mediante su nombre de usuario, con los datos almacenados en una base de datos MySQL.
- **Encriptación de Contraseñas**: Las contraseñas de los usuarios se encriptan utilizando `BCryptPasswordEncoder`.

## Tecnologías Utilizadas

- **Spring Boot**
- **Spring Security**
- **MySQL**
- **BCrypt**

## Descripción Detallada

### Entidades

- **UserEntity**: Representa a un usuario en la aplicación. Cada usuario tiene un conjunto de roles, que se representan mediante la entidad `RolesEntity`.
- **RolesEntity**: Representa un rol que puede tener un usuario. Cada rol tiene un conjunto de permisos, que se representan mediante la entidad `PermissionEntity`.
- **PermissionEntity**: Representa un permiso que puede tener un rol.

### Repositorios

- **UserRepository**: Proporciona métodos para interactuar con la base de datos y buscar usuarios por su nombre de usuario.

### Servicios

- **UserDetailServiceImpl**: Implementa la interfaz `UserDetailsService` de Spring Security, y se utiliza para cargar los detalles de un usuario durante el proceso de autenticación.

### Controladores

- **TestController**: Maneja las solicitudes HTTP y utiliza anotaciones de Spring Security como `@PreAuthorize` para restringir el acceso a las rutas basándose en los roles y permisos del usuario.

### Configuración de Seguridad

- **SecurityConfigure**: Contiene la configuración de seguridad de la aplicación. Define un `SecurityFilterChain` que especifica cómo se deben manejar las solicitudes HTTP, un `AuthenticationManager` que se utiliza para autenticar las solicitudes, y un `AuthenticationProvider` que utiliza `UserDetailServiceImpl` para cargar los detalles del usuario durante el proceso de autenticación.

### Inicialización de Datos

- **SpringSecurityApplication**: Define un `CommandLineRunner` que se ejecuta al iniciar la aplicación. Este `CommandLineRunner` crea varios usuarios, roles y permisos y los guarda en la base de datos.

## Instalación y Configuración

1. **Clonar el Repositorio**
    ```bash
    git clone git@github.com:rodrygo-c-garcia/spring_security_users.git
    cd spring_security_users
    ```

2. **Configurar la Base de Datos**
    - Crear una base de datos en MySQL.
    - Actualizar las credenciales de la base de datos en el archivo `application.properties`.
      
3. **Otorgar Permisos al Script de Maven Wrapper**
    ```bash
    chmod +x mvnw
    ```

4. **Construir y Ejecutar la Aplicación**
    ```bash
    ./mvnw spring-boot:run
    ```

## Uso

- **Acceder a la Aplicación**: Navegar a `http://localhost:8080`.
- **Gestionar Usuarios y Roles**: Usando la herramienta`Postman` utilizar los endpoints proporcionados para gestionar usuarios, roles y permisos.

## Contribuciones

Las contribuciones son bienvenidas. Si deseas contribuir, por favor abre un issue o envía un pull request.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo `LICENSE` para obtener más detalles.
