package com.haratres.ecommerce.service;

import com.haratres.ecommerce.exception.NotFoundException;
import com.haratres.ecommerce.model.Role;
import com.haratres.ecommerce.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RoleService {
    private static Logger logger= LoggerFactory.getLogger(RoleService.class);
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getByRoleName(String roleName){
        logger.error("User not found with username:  {}",roleName);
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new NotFoundException("Role not found: " + roleName));
    }
}
