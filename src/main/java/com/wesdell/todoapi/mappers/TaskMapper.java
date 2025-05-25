package com.wesdell.todoapi.mappers;

import com.wesdell.todoapi.dto.CreateTaskDto;
import com.wesdell.todoapi.dto.TaskDto;
import com.wesdell.todoapi.dto.UpdateTaskDto;
import com.wesdell.todoapi.entities.Task;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    Task toEntity(CreateTaskDto createTaskDto);
    Task toEntity(UpdateTaskDto updateTaskDto);
}
