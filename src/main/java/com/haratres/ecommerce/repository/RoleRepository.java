package com.haratres.ecommerce.repository;

import com.haratres.ecommerce.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role,Long> {
    Optional<Role> findByRoleName(String rolename);
    boolean existsByRoleName(String roleName);
}
