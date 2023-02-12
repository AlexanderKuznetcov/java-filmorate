package ru.yandex.practicum.filmorate.controller;

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
        log.info(LogMessage.ADD_USER.getLogMassage());
        validate(user);
        user.setId(currentId++);
        int id = user.getId();
        users.put(id, user);
        log.info(LogMessage.ADD_USER_DONE.getLogMassage());
        return user;
    }

    @Override
    @PutMapping
    public User update(@RequestBody User user) {
        log.info(LogMessage.UPDATE_USER.getLogMassage());
        validate(user);
        int id = user.getId();
        if (users.containsKey(id)) {
            users.put(id, user);
            log.info(LogMessage.UPDATE_USER_DONE.getLogMassage());
        } else {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
            throw new ValidationException(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
        }
        return user;
    }

    @Override
    public void validate (User user) throws ValidationException {
        StringBuilder message = new StringBuilder(LogMessage.VALIDATION_FAIL.getLogMassage());
        String email = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            message.append(LogMessage.NOT_VALID_EMAIL.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
        String login = user.getLogin();
        if (login == null || login.isBlank() || login.contains(" ")) {
            message.append(LogMessage.NOT_VALID_LOGIN.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(login);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            message.append(LogMessage.NOT_VALID_BIRTHDAY.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
    }
}