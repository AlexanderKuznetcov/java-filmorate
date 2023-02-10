package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserControllerTest {
    static UserController userController = new UserController();

    @Test
    void isValidUser () {
        User validUser = new User();
        validUser.setName("User Name");
        validUser.setLogin("UserLogin");
        validUser.setEmail("user@mail.ru");
        validUser.setBirthday("1990-01-01");
        userController.validate(validUser);
    }

    @Test
    void isNotValidUserLogin() {
        User notValidUser = new User();
        notValidUser.setName("User Name");
        notValidUser.setLogin(null);
        notValidUser.setEmail("user@mail.ru");
        notValidUser.setBirthday("1990-01-01");
        Exception exception = assertThrows(ValidationException.class, () -> userController.validate(notValidUser));
        assertEquals("Валидация не прошла! Логин пустой или содержит пробел.", exception.getMessage());
        notValidUser.setLogin("");
        exception = assertThrows(ValidationException.class, () -> userController.validate(notValidUser));
        assertEquals("Валидация не прошла! Логин пустой или содержит пробел.", exception.getMessage());
        notValidUser.setLogin("User Login");
        exception = assertThrows(ValidationException.class, () -> userController.validate(notValidUser));
        assertEquals("Валидация не прошла! Логин пустой или содержит пробел.", exception.getMessage());
    }

    @Test
    void isNotValidUserEmail() {
        User notValidUser = new User();
        notValidUser.setName("User Name");
        notValidUser.setLogin("UserLogin");
        notValidUser.setEmail(null);
        notValidUser.setBirthday("1990-01-01");
        Exception exception = assertThrows(ValidationException.class, () -> userController.validate(notValidUser));
        assertEquals("Валидация не прошла! Электронная почта пустая или без @.", exception.getMessage());
        notValidUser.setEmail("");
        exception = assertThrows(ValidationException.class, () -> userController.validate(notValidUser));
        assertEquals("Валидация не прошла! Электронная почта пустая или без @.", exception.getMessage());
        notValidUser.setEmail("usermail.ru");
        exception = assertThrows(ValidationException.class, () -> userController.validate(notValidUser));
        assertEquals("Валидация не прошла! Электронная почта пустая или без @.", exception.getMessage());
    }

    @Test
    void isNotValidUserBirthday() {
        User notValidUser = new User();
        notValidUser.setName("User Name");
        notValidUser.setLogin("UserLogin");
        notValidUser.setEmail("user@mail.ru");
        notValidUser.setBirthday("2100-01-01");
        Exception exception = assertThrows(ValidationException.class, () -> userController.validate(notValidUser));
        assertEquals("Валидация не прошла! Дата рождения не может быть в будущем.", exception.getMessage());
    }

    @Test
    void replaceEmptyName() {
        User notValidUser = new User();
        notValidUser.setName(null);
        notValidUser.setLogin("UserLogin");
        notValidUser.setEmail("user@mail.ru");
        notValidUser.setBirthday("1990-01-01");
        userController.validate(notValidUser);
        assertEquals("UserLogin", notValidUser.getName());
        notValidUser.setName("    ");
        userController.validate(notValidUser);
        assertEquals("UserLogin", notValidUser.getName());
    }
}