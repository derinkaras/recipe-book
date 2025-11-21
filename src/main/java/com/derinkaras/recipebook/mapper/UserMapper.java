package com.derinkaras.recipebook.mapper;

import com.derinkaras.recipebook.dto.user.UserDto;
import com.derinkaras.recipebook.model.User;

public class UserMapper {
    public static UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();

        userDto.setId(user.getId());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        return userDto;
    }
}
