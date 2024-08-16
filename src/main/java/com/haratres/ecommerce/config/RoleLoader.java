package com.haratres.ecommerce.config;

import com.haratres.ecommerce.model.Role;
import com.haratres.ecommerce.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class RoleLoader {
    private final RoleRepository roleRepository;

    public RoleLoader(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    CommandLineRunner initData(RoleRepository roleRepository){
        return args -> {
            roleRepository.saveAll(Arrays.asList(new Role("USER"),new Role("ADMIN")));
        };
    }
}
