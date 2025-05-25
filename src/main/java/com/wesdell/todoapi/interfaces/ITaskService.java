package com.wesdell.todoapi.interfaces;

import com.wesdell.todoapi.entities.Task;

import java.util.List;
import java.util.Optional;

public interface ITaskService {
    List<Task> getTasks();
    List<Task> getTasksByUserId(Long id);
    Task createTask(Task task);
    Task updateTask(Task task);
    void deleteTask(Long id);
    Optional<Task> findTaskById(Long id);
}
