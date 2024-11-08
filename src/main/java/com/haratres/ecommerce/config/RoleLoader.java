package com.haratres.ecommerce.config;

import com.haratres.ecommerce.model.Role;
import com.haratres.ecommerce.repository.RoleRepository;
import com.haratres.ecommerce.service.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class RoleLoader {

    private final RoleRepository roleRepository;
    private final RoleService roleService;

    public RoleLoader(RoleRepository roleRepository, RoleService roleService) {
        this.roleRepository = roleRepository;
        this.roleService = roleService;
    }

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            List<String> roleNames = Arrays.asList("USER", "ADMIN");
            for (String roleName : roleNames) {
                if (!roleService.roleExists(roleName)) {
                    Role role = new Role(roleName);
                    roleRepository.save(role);
                }
            }
        };
    }
}
