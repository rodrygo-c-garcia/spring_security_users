package com.springsecurity.controller.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

//para dar orden a nuestro json en el response
@JsonPropertyOrder({"username", "message", "token", "status"})
public record AuthResponse (String username,
                            String message,
                            String token,
                            boolean status) {
}
