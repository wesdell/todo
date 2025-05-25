package com.wesdell.todoapi.mappers;

import com.wesdell.todoapi.dto.CreateUserDto;
import com.wesdell.todoapi.dto.UpdateUserDto;
import com.wesdell.todoapi.dto.UserDto;
import com.wesdell.todoapi.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(CreateUserDto createUserDto);
    User toEntity(UpdateUserDto updateUserDto);
}
