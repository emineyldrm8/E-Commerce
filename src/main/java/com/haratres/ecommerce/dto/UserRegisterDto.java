package com.haratres.ecommerce.dto;

import com.haratres.ecommerce.model.Role;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class UserRegisterDto {
    @NotBlank(message = "Firstname can not be null")
    private String firstName;

    @NotBlank(message = "Lastname can not be null")
    private String lastName;

    @NotBlank(message = "Username can not be null")
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
    private String username;

    @NotBlank(message = "Password can not be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be at least 8 characters long, and include at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$%^&+=!)")
    private String password;

    @NotBlank(message = "Phone number can not be null")
    @Pattern(regexp = "^\\+90\\d{10}$", message = "Phone number must start with +90 and be followed by 10 digits")
    private String phone;

    @NotBlank(message = "Email can not be null")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Role can not be null")
    private String role;
}
