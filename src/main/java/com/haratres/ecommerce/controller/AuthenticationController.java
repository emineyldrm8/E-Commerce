package com.haratres.ecommerce.controller;

import com.haratres.ecommerce.dto.UserLoginDto;
import com.haratres.ecommerce.dto.UserRegisterDto;
import com.haratres.ecommerce.responses.AuthenticationResponse;
import com.haratres.ecommerce.service.AuthenticationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody UserRegisterDto userRegisterDto) {
        return ResponseEntity.ok(authenticationService.register(userRegisterDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody UserLoginDto userLoginDto) {
        return ResponseEntity.ok(authenticationService.authenticate(userLoginDto));
    }
}
