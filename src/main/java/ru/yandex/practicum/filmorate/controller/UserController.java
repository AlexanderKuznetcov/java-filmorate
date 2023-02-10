package ru.yandex.practicum.filmorate.controller;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController implements  Controller<User>{
    private final Map<Integer, User> users= new HashMap<>();
    private int currentId = 1;

    @Override
    @GetMapping
    public List<User> get() {
        log.info(LogMessage.GET_USERS.getLogMassage());
        List<User> list = new ArrayList<>(users.values());
        return list;
    }

    @Override
    @PostMapping
    public User add(@RequestBody User user) {
        try {
            log.info(LogMessage.ADD_USER.getLogMassage());
            validate(user);
            user.setId(currentId);
            currentId++;
            int id = user.getId();
            users.put(id, user);
            log.info(LogMessage.ADD_USER_DONE.getLogMassage());
        } catch (ValidationException e) {
            String message = e.getMessage();
            log.warn(message);
            throw new ValidationException(message);
        }
        return user;
    }

    @Override
    @PutMapping
    public User update(@RequestBody User user) {
        try {
            log.info(LogMessage.UPDATE_USER.getLogMassage());
            validate(user);
            int id = user.getId();
            if (users.containsKey(id)) {
                users.put(id, user);
                log.info(LogMessage.UPDATE_USER_DONE.getLogMassage());
            } else {
                log.warn(LogMessage.USER_NOT_FOUNR.getLogMassage() + id);
                throw new ValidationException(LogMessage.USER_NOT_FOUNR.getLogMassage() + id);
            }
        } catch (ValidationException e) {
            String message = e.getMessage();
            log.warn(message);
            throw new ValidationException(message);
        }
        return user;
    }

    @Override
    public void validate (User user) throws ValidationException {
        StringBuilder message = new StringBuilder("Валидация не прошла!");
        boolean isValid = true;
        String email = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            isValid = false;
            message.append(" Электронная почта пустая или без @.");
        }
        String login = user.getLogin();
        if (login == null || login.isBlank() || login.contains(" ")) {
            isValid = false;
            message.append(" Логин пустой или содержит пробел.");
        }
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(login);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            isValid = false;
            message.append(" Дата рождения не может быть в будущем.");
        }
        if (!isValid) {
            throw new ValidationException(message.toString());
        }
    }
}