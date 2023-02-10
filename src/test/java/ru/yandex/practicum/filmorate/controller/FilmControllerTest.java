package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;

public class FilmControllerTest {
    static FilmController filmController = new FilmController();

    @Test
    void isValidFilm() {
        Film validFilm = new Film();
        validFilm.setName("Film name");
        validFilm.setDescription("Film description");
        validFilm.setReleaseDate("2000-01-01");
        validFilm.setDuration(100);
        filmController.validate(validFilm);
    }

    @Test
    void isNotValidFilmName() {
        Film notValidFilm = new Film();
        notValidFilm.setName(null);
        notValidFilm.setDescription("Film description");
        notValidFilm.setReleaseDate("2000-01-01");
        notValidFilm.setDuration(100);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validate(notValidFilm));
        assertEquals("Валидация не прошла! Пустое имя фильма.", exception.getMessage());
        notValidFilm.setName(" ");
        exception = assertThrows(ValidationException.class, () -> filmController.validate(notValidFilm));
        assertEquals("Валидация не прошла! Пустое имя фильма.", exception.getMessage());
    }

    @Test
    void isNotValidFilmDescription() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Film name");
        notValidFilm.setDescription("Film description. Film description. Film description. Film description. " +
                "Film description. Film description. Film description. Film description. Film description." +
                " Film description. Film description. Film description. Film description. Film description.");
        notValidFilm.setReleaseDate("2000-01-01");
        notValidFilm.setDuration(100);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validate(notValidFilm));
        assertEquals("Валидация не прошла! Описание более 200 символов.", exception.getMessage());
    }

    @Test
    void isNotValidFilmReleaseDate() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Film name");
        notValidFilm.setDescription("Film description");
        notValidFilm.setReleaseDate("1895-12-27");
        notValidFilm.setDuration(100);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validate(notValidFilm));
        assertEquals("Валидация не прошла! Слишком ранний релиз.", exception.getMessage());
    }

    @Test
    void isNotValidFilmDuration() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Film name");
        notValidFilm.setDescription("Film description");
        notValidFilm.setReleaseDate("2000-01-01");
        notValidFilm.setDuration(0);
        Exception exception = assertThrows(ValidationException.class, () -> filmController.validate(notValidFilm));
        assertEquals("Валидация не прошла! Продолжительность отрицательная или 0.", exception.getMessage());
        notValidFilm.setDuration(-10);
        exception = assertThrows(ValidationException.class, () -> filmController.validate(notValidFilm));
        assertEquals("Валидация не прошла! Продолжительность отрицательная или 0.", exception.getMessage());
    }
}