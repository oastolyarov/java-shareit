package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

public interface UserService {
    public UserDto createUser(UserDto userDto);

    public UserDto getUserById(int userId);

    public List<UserDto> getUsers();

    public UserDto updateById(int id, UserDto userDto);

    public void deleteUserById(int id);
}
