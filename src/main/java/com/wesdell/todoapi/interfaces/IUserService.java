package com.wesdell.todoapi.interfaces;

import com.wesdell.todoapi.dto.CreateUserDto;
import com.wesdell.todoapi.dto.LoginUserDto;
import com.wesdell.todoapi.dto.UpdateUserDto;
import com.wesdell.todoapi.entities.User;

import java.util.Optional;

public interface IUserService {
    User register(CreateUserDto dto);
    Optional<User> login(LoginUserDto dto);
    User update(Long userId, UpdateUserDto dto);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
