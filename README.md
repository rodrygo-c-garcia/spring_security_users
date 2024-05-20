# Ejemplo de Spring Security con Usuarios y Roles

Este repositorio proporciona un ejemplo práctico de cómo implementar seguridad en una aplicación Spring utilizando Spring Security. La aplicación permite la gestión de usuarios, roles y permisos, asegurando el acceso a los recursos de la aplicación (controladores) de manera granular.

## Características

- **Usuarios y Roles**: Cada usuario puede tener uno o varios roles, y cada rol puede tener uno o varios permisos asociados.
- **Control de Acceso**: Utiliza Spring Security para controlar el acceso a los recursos de la aplicación.
- **Cadena de Filtros**: Implementación de una cadena de filtros para procesar las solicitudes entrantes.
- **Proveedor de Autenticación**: Uso de `DaoAuthenticationProvider` para autenticar usuarios mediante su nombre de usuario, con los datos almacenados en una base de datos MySQL.
- **Encriptación de Contraseñas**: Las contraseñas de los usuarios se encriptan utilizando `BCryptPasswordEncoder`.

## Tecnologías Utilizadas

- **Spring Framework**
- **Spring Security**
- **MySQL**
- **BCrypt**

## Descripción Detallada

### Cadena de Filtros

La aplicación utiliza una cadena de filtros (`FilterChain`) en Spring Security para interceptar y procesar las solicitudes HTTP. Los filtros se configuran para autenticar y autorizar las solicitudes basadas en los roles y permisos de los usuarios.

### Proveedor de Autenticación

El `DaoAuthenticationProvider` se encarga de autenticar a los usuarios. Este proveedor obtiene los detalles del usuario (incluyendo roles y permisos) desde la base de datos MySQL mediante el nombre de usuario. El proceso de autenticación se configura en el contexto de seguridad de Spring.

### Encriptación de Contraseñas

Para garantizar la seguridad de las contraseñas, se utiliza `BCryptPasswordEncoder` en la implementación de `PasswordEncoder`. Este enfoque asegura que las contraseñas se almacenen de forma segura en la base de datos, haciendo uso de técnicas de hashing y salting.

## Instalación y Configuración

1. **Clonar el Repositorio**
    ```bash
    git clone git@github.com:rodrygo-c-garcia/spring_security_users.git
    cd tu-repo
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
- **Gestionar Usuarios y Roles**: Utilizar los endpoints proporcionados para gestionar usuarios, roles y permisos.

## Contribuciones

Las contribuciones son bienvenidas. Si deseas contribuir, por favor abre un issue o envía un pull request.

## Licencia

Este proyecto está licenciado bajo la Licencia MIT. Consulta el archivo `LICENSE` para obtener más detalles.
