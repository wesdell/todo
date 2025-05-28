package com.wesdell.todoapi.services;

import com.wesdell.todoapi.entities.User;
import com.wesdell.todoapi.interfaces.IUserService;
import com.wesdell.todoapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService implements IUserService, UserDetailsService {
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
        User existingUser = userRepository.findById(userId).orElseThrow();
        String newEncryptedPassword = passwordEncrypter.encode(updatedUser.getPassword());

        existingUser.setName(
            Objects.requireNonNullElse(updatedUser.getName(), existingUser.getName())
        );
        existingUser.setPassword(
            Objects.requireNonNullElse(newEncryptedPassword, existingUser.getPassword())
        );

        return userRepository.save(existingUser);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return org.springframework.security.core.userdetails.User
            .withUsername(user.getEmail())
            .password(user.getPassword())
            .build();
    }
}
