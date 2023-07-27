package com.tesis.proyecto_tesis.controllers;

import com.tesis.proyecto_tesis.dto.LoginDTO;
import com.tesis.proyecto_tesis.response.LoginResponse;
import com.tesis.proyecto_tesis.services.AuthenticationService;
import com.tesis.proyecto_tesis.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping(path = "/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO)
    {
        System.out.println("login");
        LoginResponse loginResponse = authenticationService.login(loginDTO);
        System.out.println(loginResponse);
        return ResponseEntity.ok(loginResponse);
    }
}
