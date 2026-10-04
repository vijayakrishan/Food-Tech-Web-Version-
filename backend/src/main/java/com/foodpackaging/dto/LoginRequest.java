package com.foodpackaging.dto;
import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank(message = "Username or email is required")
    @JsonAlias({"usernameOrEmail", "email"})
    private String username;

    @NotBlank(message = "Password is required")
    private String password;
}

