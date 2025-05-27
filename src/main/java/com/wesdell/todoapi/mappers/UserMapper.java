package com.wesdell.todoapi.mappers;

import com.wesdell.todoapi.dto.LoginUserDto;
import com.wesdell.todoapi.dto.RegisterUserDto;
import com.wesdell.todoapi.dto.UpdateUserDto;
import com.wesdell.todoapi.dto.UserDto;
import com.wesdell.todoapi.entities.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserDto registerUserDto);
    User toEntity(LoginUserDto loginUserDto);
    User toEntity(UpdateUserDto updateUserDto);
}
