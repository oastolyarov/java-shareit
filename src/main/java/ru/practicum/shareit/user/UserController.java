package ru.practicum.shareit.user;

import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;
import java.util.*;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private Map<Integer, User> users = new HashMap<>();
    private int id = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        emailValidation(user);
        user.setId(id);
        id++;
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        if (users.get(userId) == null) {
            throw new NullPointerException("Пользователь с id " + userId + " не найден.");
        }
        return users.get(userId);
    }

    @GetMapping
    public List<User> getUsers() {
        return List.copyOf(users.values());
    }

    @PatchMapping
    public User updateUser(@RequestBody User user) {
        User newUser = new User();
        newUser.setId(user.getId());
        newUser.setName(user.getName() == null ? users.get(user.getId()).getName() : user.getName());
        newUser.setEmail(user.getEmail() == null ? users.get(user.getId()).getEmail() : user.getEmail());

        for (int i : users.keySet()) {
            if (users.get(i).getEmail().equals(user.getEmail()) && users.get(i).getId() != user.getId()) {
                throw new ValidationException("Пользователь с таким email уже существует.");
            }
        }

        users.put(user.getId(), newUser);
        return newUser;
    }

    @PatchMapping("/{id}")
    public User updateById(@PathVariable int id, @RequestBody User user) {
        User newUser = new User();
        newUser.setId(id);
        newUser.setName(user.getName() == null ? getUserById(id).getName() : user.getName());
        newUser.setEmail(user.getEmail() == null ? getUserById(id).getEmail() : user.getEmail());

        for (int i : users.keySet()) {
            if (users.get(i).getEmail().equals(user.getEmail()) && users.get(i).getId() != user.getId()) {
                throw new ValidationException("Пользователь с таким email уже существует.");
            }
        }

        users.put(id, newUser);
        return newUser;
    }

    @DeleteMapping("/{id}")
    public void deleteUserById(@PathVariable int id) {
        users.remove(id);
    }

    private void emailValidation(User user) {
        if (user.getEmail() == null) {
            throw new NullPointerException("Нужно указать email.");
        }

        if (!user.getEmail().matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]{2,4}")) {
            throw new NullPointerException("Не правильно указан email.");
        }

        for (int i : users.keySet()) {
            if (users.get(i).getEmail().equals(user.getEmail())) {
                throw new ValidationException("Пользователь с таким email уже существует.");
            }
        }
    }

    public Map<Integer, User> getAll() {
        return users;
    }
}
