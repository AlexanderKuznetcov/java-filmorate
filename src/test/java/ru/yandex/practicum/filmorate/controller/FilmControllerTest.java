package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class FilmControllerTest {
    static FilmController filmController = new FilmController(new FilmService(new InMemoryFilmStorage()));

    @Test
    void isValidFilm() {
        Film validFilm = new Film();
        validFilm.setName("Film name");
        validFilm.setDescription("Film description");
        validFilm.setReleaseDate(LocalDate.of(2000,1,1));
        validFilm.setDuration(100);
        filmController.getFilmService().validateFilm(validFilm);
    }

    @Test
    void isNotValidFilmName() {
        Film notValidFilm = new Film();
        notValidFilm.setName(null);
        notValidFilm.setDescription("Film description");
        notValidFilm.setReleaseDate(LocalDate.of(2000,1,1));
        notValidFilm.setDuration(100);
        Exception exception = assertThrows(ValidationException.class, () ->
                filmController.getFilmService().validateFilm(notValidFilm));
        assertEquals("Валидация не прошла! Пустое имя фильма.", exception.getMessage());
        notValidFilm.setName(" ");
        exception = assertThrows(ValidationException.class, () ->
                filmController.getFilmService().validateFilm(notValidFilm));
        assertEquals("Валидация не прошла! Пустое имя фильма.", exception.getMessage());
    }

    @Test
    void isNotValidFilmDescription() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Film name");
        notValidFilm.setDescription("Film description. Film description. Film description. Film description. " +
                "Film description. Film description. Film description. Film description. Film description." +
                " Film description. Film description. Film description. Film description. Film description.");
        notValidFilm.setReleaseDate(LocalDate.of(2000,1,1));
        notValidFilm.setDuration(100);
        Exception exception = assertThrows(ValidationException.class, () ->
                filmController.getFilmService().validateFilm(notValidFilm));
        assertEquals("Валидация не прошла! Описание более 200 символов.", exception.getMessage());
    }

    @Test
    void isNotValidFilmReleaseDate() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Film name");
        notValidFilm.setDescription("Film description");
        notValidFilm.setReleaseDate(LocalDate.of(1895,12,27));
        notValidFilm.setDuration(100);
        Exception exception = assertThrows(ValidationException.class, () ->
                filmController.getFilmService().validateFilm(notValidFilm));
        assertEquals("Валидация не прошла! Слишком ранний релиз.", exception.getMessage());
    }

    @Test
    void isNotValidFilmDuration() {
        Film notValidFilm = new Film();
        notValidFilm.setName("Film name");
        notValidFilm.setDescription("Film description");
        notValidFilm.setReleaseDate(LocalDate.of(2000,1,1));
        notValidFilm.setDuration(0);
        Exception exception = assertThrows(ValidationException.class, () ->
                filmController.getFilmService().validateFilm(notValidFilm));
        assertEquals("Валидация не прошла! Продолжительность отрицательная или 0.", exception.getMessage());
        notValidFilm.setDuration(-10);
        exception = assertThrows(ValidationException.class, () ->
                filmController.getFilmService().validateFilm(notValidFilm));
        assertEquals("Валидация не прошла! Продолжительность отрицательная или 0.", exception.getMessage());
    }
}