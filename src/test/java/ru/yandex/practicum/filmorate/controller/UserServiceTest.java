package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {
    private static final UserService userService = new UserService(new InMemoryUserStorage());
    static UserController userController = new UserController(new UserService(new InMemoryUserStorage()));

    @Test
    void isValidUser () {
        User validUser = new User();
        validUser.setName("User Name");
        validUser.setLogin("UserLogin");
        validUser.setEmail("user@mail.ru");
        validUser.setBirthday(LocalDate.of(1990,1,1));
        userService.validateUser(validUser);
    }

    @Test
    void isNotValidUserLogin() {
        User notValidUser = new User();
        notValidUser.setName("User Name");
        notValidUser.setLogin(null);
        notValidUser.setEmail("user@mail.ru");
        notValidUser.setBirthday(LocalDate.of(1990,1,1));
        Exception exception = assertThrows(ValidationException.class, () ->
                userService.validateUser(notValidUser));
        assertEquals("Валидация не прошла!Логин пустой или содержит пробел", exception.getMessage());
        notValidUser.setLogin("");
        exception = assertThrows(ValidationException.class, () ->
                userService.validateUser(notValidUser));
        assertEquals("Валидация не прошла!Логин пустой или содержит пробел", exception.getMessage());
        notValidUser.setLogin("User Login");
        exception = assertThrows(ValidationException.class, () ->
                userService.validateUser(notValidUser));
        assertEquals("Валидация не прошла!Логин пустой или содержит пробел", exception.getMessage());
    }

    @Test
    void isNotValidUserEmail() {
        User notValidUser = new User();
        notValidUser.setName("User Name");
        notValidUser.setLogin("UserLogin");
        notValidUser.setEmail(null);
        notValidUser.setBirthday(LocalDate.of(1990,1,1));
        Exception exception = assertThrows(ValidationException.class, () ->
                userService.validateUser(notValidUser));
        assertEquals("Валидация не прошла!Электронная почта пустая или без @", exception.getMessage());
        notValidUser.setEmail("");
        exception = assertThrows(ValidationException.class, () ->
                userService.validateUser(notValidUser));
        assertEquals("Валидация не прошла!Электронная почта пустая или без @", exception.getMessage());
        notValidUser.setEmail("usermail.ru");
        exception = assertThrows(ValidationException.class, () ->
                userService.validateUser(notValidUser));
        assertEquals("Валидация не прошла!Электронная почта пустая или без @", exception.getMessage());
    }

    @Test
    void isNotValidUserBirthday() {
        User notValidUser = new User();
        notValidUser.setName("User Name");
        notValidUser.setLogin("UserLogin");
        notValidUser.setEmail("user@mail.ru");
        notValidUser.setBirthday(LocalDate.of(2100,1,1));
        Exception exception = assertThrows(ValidationException.class, () ->
                userService.validateUser(notValidUser));
        assertEquals("Валидация не прошла!Дата рождения не может быть в будущем", exception.getMessage());
    }

    @Test
    void replaceEmptyName() {
        User notValidUser = new User();
        notValidUser.setName(null);
        notValidUser.setLogin("UserLogin");
        notValidUser.setEmail("user@mail.ru");
        notValidUser.setBirthday(LocalDate.of(1990,1,1));
        userService.validateUser(notValidUser);
        assertEquals("UserLogin", notValidUser.getName());
        notValidUser.setName("    ");
        userService.validateUser(notValidUser);
        assertEquals("UserLogin", notValidUser.getName());
    }
}