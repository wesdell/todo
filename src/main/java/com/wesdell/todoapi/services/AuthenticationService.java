package com.wesdell.todoapi.services;

import com.wesdell.todoapi.dto.AuthenticationDto;
import com.wesdell.todoapi.dto.LoginUserDto;
import com.wesdell.todoapi.dto.RegisterUserDto;
import com.wesdell.todoapi.entities.User;
import com.wesdell.todoapi.mappers.UserMapper;
import com.wesdell.todoapi.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import com.wesdell.todoapi.configuration.PasswordEncrypter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final PasswordEncrypter passwordEncrypter;

    @Autowired
    public AuthenticationService(
        UserService userService,
        JwtUtil jwtUtil,
        UserMapper userMapper,
        PasswordEncrypter passwordEncrypter) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
        this.passwordEncrypter = passwordEncrypter;
    }

    public AuthenticationDto register(RegisterUserDto registerUserDto) {
        User user = userService.create(userMapper.toEntity(registerUserDto));
        String token = jwtUtil.generateJwtToken(user);
        return new AuthenticationDto(token);
    }

    public AuthenticationDto login(LoginUserDto loginUserDto) {
        User userAttempt = userMapper.toEntity(loginUserDto);
        Optional<User> existingUser = userService.findByEmail(userAttempt.getEmail());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        boolean validPassword = passwordEncrypter.passwordEncoder().matches(userAttempt.getPassword(), existingUser.get().getPassword());
        if (!validPassword) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwtUtil.generateJwtToken(existingUser.get());
        return new AuthenticationDto(token);
    }
}
