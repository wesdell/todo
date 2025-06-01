package com.wesdell.todoapi.services;

import com.wesdell.todoapi.entities.Task;
import com.wesdell.todoapi.entities.User;
import com.wesdell.todoapi.interfaces.ITaskService;
import com.wesdell.todoapi.repositories.TaskRepository;
import com.wesdell.todoapi.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TaskService implements ITaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public List<Task> getTasksByUserEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));
        return taskRepository.findByUser(user);
    }

    @Override
    public Task createTask(String userEmail, Task task) {
        User user = userRepository.findByEmail(userEmail)
            .orElseThrow(() -> new EntityNotFoundException("User not found"));

        task.setUser(user);
        task.setCreatedAt(LocalDateTime.now());
        task.setUpdatedAt(LocalDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id, String userEmail, Task updatedTask) {
        Task existingTask = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!existingTask.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You are not authorized to update this task");
        }

        Optional.ofNullable(updatedTask.getTitle()).ifPresent(existingTask::setTitle);
        Optional.ofNullable(updatedTask.getDescription()).ifPresent(existingTask::setDescription);
        Optional.ofNullable(updatedTask.getStatus()).ifPresent(existingTask::setStatus);

        existingTask.setUpdatedAt(LocalDateTime.now());

        return taskRepository.save(existingTask);
    }

    @Override
    public void deleteTask(Long id, String userEmail) {
        Task task = taskRepository.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Task not found"));

        if (!task.getUser().getEmail().equals(userEmail)) {
            throw new SecurityException("You are not authorized to delete this task");
        }

        taskRepository.delete(task);
    }

    @Override
    public Optional<Task> findTaskByIdAndUserEmail(Long id, String email) {
        Optional<Task> taskOpt = taskRepository.findById(id);
        return taskOpt.filter(task -> task.getUser().getEmail().equals(email));
    }
}
