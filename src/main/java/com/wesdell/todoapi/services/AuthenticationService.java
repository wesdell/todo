package com.wesdell.todoapi.services;

import com.wesdell.todoapi.dto.AuthenticationDto;
import com.wesdell.todoapi.dto.LoginUserDto;
import com.wesdell.todoapi.dto.RegisterUserDto;
import com.wesdell.todoapi.entities.User;
import com.wesdell.todoapi.mappers.UserMapper;
import com.wesdell.todoapi.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import com.wesdell.todoapi.configuration.PasswordEncrypter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;
    private final PasswordEncrypter passwordEncrypter;
    private final AuthenticationManager authenticationManager;

    public AuthenticationDto register(RegisterUserDto registerUserDto) {
        User user = userService.create(userMapper.toEntity(registerUserDto));
        String token = jwtUtil.generateJwtToken(user);
        return AuthenticationDto.builder().token(token).build();
    }

    public AuthenticationDto login(LoginUserDto loginUserDto) {
        User userAttempt = userMapper.toEntity(loginUserDto);
        Optional<User> existingUser = userService.findByEmail(userAttempt.getEmail());
        if (existingUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password");
        }

        boolean validPassword = passwordEncrypter.passwordEncoder().matches(userAttempt.getPassword(), existingUser.get().getPassword());
        if (!validPassword) {
            throw new RuntimeException("Invalid email or password");
        }

        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginUserDto.getEmail(),
                loginUserDto.getPassword()
            )
        );

        String token = jwtUtil.generateJwtToken(existingUser.get());
        return AuthenticationDto.builder().token(token).build();
    }
}
