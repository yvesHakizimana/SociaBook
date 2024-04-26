package com.code.socialbook.backendapi.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthenticationRequest {
    @NotEmpty(message = "Email is required")
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not well formatted")
    private String email;
    @NotEmpty(message = "Password is required")
    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, message = "Password should be eight characters minimum.")
    private String password;
}
