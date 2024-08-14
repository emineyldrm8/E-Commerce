package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.UserRegisterDto;
import com.haratres.ecommerce.dto.UserLoginDto;
import com.haratres.ecommerce.exception.DuplicateEntryException;
import com.haratres.ecommerce.exception.InvalidRoleException;
import com.haratres.ecommerce.exception.NotFoundExc;
import com.haratres.ecommerce.mapper.UserMapper;
import com.haratres.ecommerce.model.User;
import com.haratres.ecommerce.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private static Logger logger= LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserRegisterDto saveUser(UserRegisterDto userRegisterDto) {
        User user = userMapper.toEntity(userRegisterDto);
        if (!"USER".equals(user.getRole()) && !"ADMIN".equals(user.getRole())) {
            logger.error("Invalid role: {}. Roll must be USER or ADMIN. Error registering user: {}", user.getRole(), userRegisterDto.getUsername());
            throw new InvalidRoleException("Invalid role. Role must be USER or ADMIN.");
        }
        try {
            User savedUser = userRepository.save(user);
            logger.info("{} registered successfully.",userRegisterDto.getUsername());
            return userMapper.toRegisterDTO(savedUser);
        } catch (DataIntegrityViolationException e) {
            logger.error("Error registering user: {}. User with the same username, email, or phone already exists.", userRegisterDto.getUsername());
            throw new DuplicateEntryException("User with the same username, email, or phone already exists.");
        }
    }

    public UserLoginDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundExc("User not found with username: " + username));
        logger.info("User found with username {}",username);
        return userMapper.toLoginDTO(user);
    }
}
