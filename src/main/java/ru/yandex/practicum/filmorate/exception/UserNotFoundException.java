package ru.yandex.practicum.filmorate.exception;

public class UserNotFoundException extends ObjectNotFoundException{
    public UserNotFoundException(final String message) {
        super(message);
    }
}
