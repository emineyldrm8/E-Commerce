package com.haratres.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "Users")
@Getter
@Setter
public class User extends BaseEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Firstname cannot be null")
    @Column(name = "firstname")
    private String firstName;

    @NotBlank(message = "Lastname cannot be null")
    @Column(name = "lastname")
    private String lastName;

    @NotBlank(message = "Username cannot be null")
    @Size(min = 2, max = 30, message = "Username must be between 2 and 30 characters")
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank(message = "Password cannot be null")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$",
            message = "Password must be at least 8 characters long, and include at least one uppercase letter, one lowercase letter, one digit, and one special character (@#$%^&+=!)")
    @Column(name = "password")
    private String password;

    @ManyToOne
    @JoinColumn(name = "roleId")
    @JsonIgnore
    private Role role;

    @NotBlank(message = "Phone number cannot be null")
    @Pattern(regexp = "^\\+90\\d{10}$", message = "Phone number must start with +90 and be followed by 10 digits")
    @Column(name = "phone", unique = true)
    private String phone;

    @NotBlank(message = "Email cannot be null")
    @Email(message = "Email should be valid")
    @Column(name = "email", unique = true)
    private String email;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getRoleName()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
