package com.wesdell.todoapi.interfaces;

import com.wesdell.todoapi.entities.User;

import java.util.List;
import java.util.Optional;

public interface IUserService {
    List<User> getUsers();
    User register(User user);
    Optional<User> login(User userAttemptedToLog);
    User update(Long userId, User updatedUser);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long id);
}
