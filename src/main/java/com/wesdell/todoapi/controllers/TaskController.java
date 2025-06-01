package com.wesdell.todoapi.controllers;


import com.wesdell.todoapi.dto.CreateTaskDto;
import com.wesdell.todoapi.dto.UpdateTaskDto;
import com.wesdell.todoapi.dto.TaskDto;
import com.wesdell.todoapi.entities.Task;
import com.wesdell.todoapi.entities.User;
import com.wesdell.todoapi.mappers.TaskMapper;
import com.wesdell.todoapi.services.TaskService;
import com.wesdell.todoapi.services.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final TaskMapper taskMapper;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<List<TaskDto>> getTasks(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        List<TaskDto> tasks = taskService.getTasksByUserEmail(user.getEmail())
            .stream()
            .map(taskMapper::toDto)
            .toList();
        return ResponseEntity.ok(tasks);
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(
        Authentication authentication,
        @RequestBody CreateTaskDto createTaskDto) {
        User user = getAuthenticatedUser(authentication);
        Task task = taskMapper.toEntity(createTaskDto);
        task.setUser(user);
        Task created = taskService.createTask(user.getEmail(), task);
        return ResponseEntity.ok(taskMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskDto> updateTask(
        Authentication authentication,
        @PathVariable Long id,
        @RequestBody UpdateTaskDto updateTaskDto) {
        User user = getAuthenticatedUser(authentication);
        Task task = taskMapper.toEntity(updateTaskDto);
        task.setId(id);
        task.setUser(user);
        Task updated = taskService.updateTask(id, user.getEmail(), task);
        return ResponseEntity.ok(taskMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(Authentication authentication, @PathVariable Long id) {
        User user = getAuthenticatedUser(authentication);
        Task task = taskService.findTaskByIdAndUserEmail(id, user.getEmail())
            .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getUser().getId().equals(user.getId())) {
            return ResponseEntity.status(403).build();
        }

        taskService.deleteTask(id, user.getEmail());
        return ResponseEntity.noContent().build();
    }

    private User getAuthenticatedUser(Authentication authentication) {
        String email = authentication.getName();
        return userService.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
