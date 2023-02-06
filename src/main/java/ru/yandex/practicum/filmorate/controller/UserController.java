package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class UserController {
    private final Map<Integer, User> users= new HashMap<>();
    private int currentId = 1;
    private final static Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/users")
    public List<User> getUsers() {
        log.info("Получен запрос на получение списка пользователей");
        List<User> list = new ArrayList<>(users.values());
        return list;
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        try {
            log.info("Получен запрос на добавление пользователя");
            validateUser(user);
            user.setId(currentId);
            currentId++;
            int id = user.getId();
            users.put(id, user);
            log.info("Пользователь добавлен");
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return user;
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        try {
            log.info("Получен запрос на обновление пользователя");
            validateUser(user);
            int id = user.getId();
            if (users.containsKey(id)) {
                users.put(id, user);
                log.info("Пользователь обновлен");
            } else {
                log.warn("Пользователя с id = " + id + " нет в списке");
                throw new ValidationException("Пользователя с id = " + id + " нет в списке");
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return user;
    }

    public void validateUser (User user) throws ValidationException {
        StringBuilder massage = new StringBuilder("Валидация не прошла!");
        boolean isValid = true;
        String email = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            isValid = false;
            massage.append(" Электронная почта пустая или без @.");
        }
        String login = user.getLogin();
        if (login == null || login.isBlank() || login.contains(" ")) {
            isValid = false;
            massage.append(" Логин пустой или содержит пробел.");
        }
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(login);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            isValid = false;
            massage.append(" Дата рождения не может быть в будущем.");
        }
        if (!isValid) {
            throw new ValidationException(massage.toString());
        }
    }
}