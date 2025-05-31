package com.wesdell.todoapi.interfaces;

import com.wesdell.todoapi.entities.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskService {
    List<Task> getTasksByUserEmail(String email);
    Task createTask(String userEmail, Task task);
    Task updateTask(Long id, String userEmail, Task updatedTask);
    void deleteTask(Long id, String userEmail);
    Optional<Task> findTaskByIdAndUserEmail(Long id, String email);
}
