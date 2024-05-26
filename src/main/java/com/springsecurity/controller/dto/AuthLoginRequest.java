package com.springsecurity.controller.dto;

import jakarta.validation.constraints.NotBlank;

//para dar orden a nuestro json en el response, no pueden ser blancos estos campos
public record AuthLoginRequest(@NotBlank String username,
                               @NotBlank String password) {
}
