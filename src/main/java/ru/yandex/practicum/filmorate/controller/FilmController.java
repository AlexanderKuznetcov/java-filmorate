package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping("/films")
    public List<Film> getFilms() {
        log.info("Получен запрос на получение списка фильмов");
        List<Film> list = new ArrayList<>(films.values());
        return list;
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        try {
            log.info("Получен запрос на добавление фильма");
            validateFilm(film);
            film.setId(currentId);
            currentId++;
            int id = film.getId();
            films.put(id, film);
            log.info("Фильм добавлен");
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return film;
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        try {
            log.info("Получен запрос на обновление фильма");
            validateFilm(film);
            int id = film.getId();
            if (films.containsKey(id)) {
                films.put(id, film);
                log.info("Фильм обновлен");
            } else {
                log.warn("Фильма с id = " + id + " нет в списке");
                throw new ValidationException("Фильма с id = " + id + " нет в списке");
            }
        } catch (ValidationException e) {
            log.warn(e.getMessage());
            throw e;
        }
        return film;
    }

    public void validateFilm (Film film) throws ValidationException{
        StringBuilder massage = new StringBuilder("Валидация не прошла!");
        boolean isValid = true;
        String name = film.getName();
        if (name == null || name.isBlank()) {
            isValid = false;
            massage.append(" Пустое имя фильма.");
        }
        if (film.getDescription().length() > 200) {
            isValid = false;
            massage.append(" Описание более 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            isValid = false;
            massage.append(" Слишком ранний релиз.");
        }
        if (film.getDuration() <= 0) {
            isValid = false;
            massage.append(" Продолжительность отрицательная или 0.");
        }
        if (!isValid) {
            throw new ValidationException(massage.toString());
        }
    }


}