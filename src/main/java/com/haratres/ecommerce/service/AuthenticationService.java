package com.haratres.ecommerce.service;

import com.haratres.ecommerce.dto.UserLoginDto;
import com.haratres.ecommerce.dto.UserRegisterDto;
import com.haratres.ecommerce.mapper.UserMapper;
import com.haratres.ecommerce.repository.UserRepository;
import com.haratres.ecommerce.responses.AuthenticationResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    public AuthenticationResponse register(UserRegisterDto userRegisterDto) {
        userRegisterDto.setPassword(passwordEncoder.encode(userRegisterDto.getPassword()));
        UserRegisterDto savedUserDto = userService.saveUser(userRegisterDto);
        String token = jwtService.generateToken(userMapper.toEntity(savedUserDto));
        return new AuthenticationResponse(token);
    }

    public AuthenticationResponse authenticate(UserLoginDto userLoginDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginDto.getUsername(), userLoginDto.getPassword()));
        userService.getUserByUsername(userLoginDto.getUsername());
        String token = jwtService.generateToken(userMapper.toEntity(userLoginDto));
        return new AuthenticationResponse(token);
    }
}
