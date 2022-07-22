package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exceptions.UserIdNotValidException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.model.UserMapper;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto createUser(@RequestBody UserDto userDto) {
        emailValidation(userDto);

        return UserMapper.toUserDto(userRepository.save(UserMapper.toUser(userDto)));
    }

    @Override
    public UserDto getUserById(int userId) {

        Optional<User> user = userRepository.findById(userId);

        if (user.isEmpty()) {
            throw new UserIdNotValidException("Пользователь с id " + userId + " не найден.");
        }

        return user.map(UserMapper::toUserDto).orElse(null);
    }

    @Override
    public List<UserDto> getUsers() {

        return userRepository.findAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto updateById(int id, UserDto userDto) {

        if (userRepository.getUserByEmail(userDto.getEmail()).isPresent()) {
            throw new ValidationException("Пользователь с таким email уже существует.");
        }

        User user = new User();

        if (userRepository.findById(id).isEmpty()) {
            throw new NullPointerException("Пользователь с id " + id + " не найден.");
        } else {
            user = userRepository.findById(id).get();
        }

        String email = userDto.getEmail() == null ?
                user.getEmail()
                : userDto.getEmail();

        String name = userDto.getName() == null ?
                user.getName() :
                userDto.getName();

        userRepository.setUserInfoById(name, email, id);

        userDto.setId(id);
        userDto.setName(name);
        userDto.setEmail(email);

        return userDto;
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    private void emailValidation(UserDto userDto) {
        if (userDto.getEmail() == null) {
            throw new NullPointerException("Нужно указать email.");
        }

        if (!userDto.getEmail().matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}")) {
            throw new NullPointerException("Не правильно указан email.");
        }
    }
}
