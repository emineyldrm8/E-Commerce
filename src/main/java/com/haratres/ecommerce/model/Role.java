package com.haratres.ecommerce.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "Role")
@Getter
@Setter
@NoArgsConstructor
public class Role extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "roleId")
    private Long id;

    @NotBlank(message = "Role cannot be null")
    @Column(name = "roleName", unique = true)
    private String roleName;

    @OneToMany(mappedBy = "role")
    private List<User> users;

    public Role(String roleName) {
        this.roleName = roleName;
    }
}
