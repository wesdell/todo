package com.wesdell.todoapi.controllers;


import com.wesdell.todoapi.dto.AuthenticationDto;
import com.wesdell.todoapi.dto.LoginUserDto;
import com.wesdell.todoapi.dto.RegisterUserDto;
import com.wesdell.todoapi.services.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor()
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationDto> register(@Valid @RequestBody RegisterUserDto registerUserDto) {
        return ResponseEntity.ok(authenticationService.register(registerUserDto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationDto> login(@Valid @RequestBody LoginUserDto dto) {
        return ResponseEntity.ok(authenticationService.login(dto));
    }
}
