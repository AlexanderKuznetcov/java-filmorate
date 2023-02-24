package ru.yandex.practicum.filmorate.exception;

public class FilmNotFoundException extends ObjectNotFoundException{
    public FilmNotFoundException(final String message) {
        super(message);
    }
}
