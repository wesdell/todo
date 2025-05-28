package com.wesdell.todoapi.controllers;

import com.wesdell.todoapi.dto.UpdateUserDto;
import com.wesdell.todoapi.dto.UserDto;
import com.wesdell.todoapi.entities.User;
import com.wesdell.todoapi.mappers.UserMapper;
import com.wesdell.todoapi.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @PutMapping()
    public ResponseEntity<UserDto> updateUser(Authentication authentication, @Validated @RequestBody UpdateUserDto updateUserDto) {
        String email = authentication.getName();
        User user = userService.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return ResponseEntity.ok(
            userMapper.toDto(
                userService.update(user.getId(), userMapper.toEntity(updateUserDto))
            )
        );
    }
}
