package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.model.Role;
import com.haratres.ecommerce.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private static final Logger logger = LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseThrow(() -> {
                    logger.error("Role not found with role name: {}", roleName);
                    return new NotFoundException("Role not found: " + roleName);
                });
    }

    public boolean roleExists(String roleName) {
        return roleRepository.existsByRoleName(roleName);
    }
}
