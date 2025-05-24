package com.wesdell.todoapi.repositories;

import com.wesdell.todoapi.entities.Task;
import com.wesdell.todoapi.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(User user);
}
