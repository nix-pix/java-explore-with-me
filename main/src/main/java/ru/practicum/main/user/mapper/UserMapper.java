package ru.practicum.main.user.mapper;

import org.mapstruct.Mapper;
import ru.practicum.main.user.dto.UserDto;
import ru.practicum.main.user.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toUser(UserDto userModelDto);

    UserDto toUserDto(User user);

    List<UserDto> toUserDtos(List<User> usersList);
}
