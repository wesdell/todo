package com.wesdell.todoapi.controllers;

import com.wesdell.todoapi.dto.UpdateUserDto;
import com.wesdell.todoapi.dto.UserDto;
import com.wesdell.todoapi.mappers.UserMapper;
import com.wesdell.todoapi.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping()
    public List<UserDto> getAllUsers() {
        return userService.getUsers().stream().map(userMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable long id) {
        return userService.findById(id)
            .map(userMapper::toDto)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable long id, @Validated @RequestBody UpdateUserDto updateUserDto) {
        return ResponseEntity.ok(userMapper.toDto(userService.update(id, userMapper.toEntity(updateUserDto))));
    }
}
