package com.wesdell.todoapi.mappers;

import com.wesdell.todoapi.dto.CreateTaskDto;
import com.wesdell.todoapi.dto.TaskDto;
import com.wesdell.todoapi.dto.UpdateTaskDto;
import com.wesdell.todoapi.entities.Task;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskDto toDto(Task task);
    Task toEntity(CreateTaskDto createTaskDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Task toEntity(UpdateTaskDto updateTaskDto);
}
