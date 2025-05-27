package com.wesdell.todoapi.services;

import com.wesdell.todoapi.entities.User;
import com.wesdell.todoapi.interfaces.IUserService;
import com.wesdell.todoapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncrypter;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncrypter) {
        this.userRepository = userRepository;
        this.passwordEncrypter = passwordEncrypter;
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public User create(User user) {
        boolean emailExists = userRepository.findByEmail(user.getEmail()).isPresent();
        if (emailExists) {
            throw new RuntimeException("Email already exists");
        }
        String encryptedPassword = passwordEncrypter.encode(user.getPassword());
        User newUser = User.builder()
            .name(user.getName())
            .email(user.getEmail())
            .password(encryptedPassword)
            .build();
        return userRepository.save(newUser);
    }

    @Override
    public User update(Long userId, User updatedUser) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User newUpdatedUser = existingUser.get();
        String newEncryptedPassword = passwordEncrypter.encode(updatedUser.getPassword());
        newUpdatedUser.setName(
            Objects.requireNonNullElse(updatedUser.getName(), existingUser.get().getName())
        );
        newUpdatedUser.setPassword(
            Objects.requireNonNullElse(newEncryptedPassword, existingUser.get().getPassword())
        );
        return userRepository.save(newUpdatedUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }
}
