package com.wesdell.todoapi.services;

import com.wesdell.todoapi.dto.CreateUserDto;
import com.wesdell.todoapi.dto.LoginUserDto;
import com.wesdell.todoapi.dto.UpdateUserDto;
import com.wesdell.todoapi.entities.User;
import com.wesdell.todoapi.interfaces.IUserService;
import com.wesdell.todoapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
    public User register(CreateUserDto createUserDto) {
        boolean emailExists = userRepository.findByEmail(createUserDto.getEmail()).isPresent();
        if (emailExists) {
            throw new RuntimeException("Email already exists");
        }
        String encryptedPassword = passwordEncrypter.encode(createUserDto.getPassword());
        User user = User.builder()
            .name(createUserDto.getName())
            .email(createUserDto.getEmail())
            .password(encryptedPassword)
            .build();
        return userRepository.save(user);
    }

    @Override
    public Optional<User> login(LoginUserDto loginUserDto) {
        Optional<User> user = userRepository.findByEmail(loginUserDto.getEmail());
        boolean validPassword = passwordEncrypter.matches(loginUserDto.getPassword(), loginUserDto.getPassword());
        if (user.isEmpty() || !validPassword) {
            throw new RuntimeException("Invalid password or email");
        }
        return user;
    }

    @Override
    public User update(Long userId, UpdateUserDto updateUserDto) {
        Optional<User> existingUser = userRepository.findById(userId);
        if (existingUser.isEmpty()) {
            throw new RuntimeException("User not found");
        }
        User newUpdatedUser = existingUser.get();
        String newEncryptedPassword = passwordEncrypter.encode(updateUserDto.getPassword());
        newUpdatedUser.setName(
            Objects.requireNonNullElse(updateUserDto.getName(), existingUser.get().getName())
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
