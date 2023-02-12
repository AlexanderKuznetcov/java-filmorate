package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController implements Controller<Film>{
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate DATE_OF_FIRST_MOVIE = LocalDate.of(1895, 12, 28);

    @Override
    @GetMapping
    public List<Film> get() {
        log.info(LogMessage.GET_FILMS.getLogMassage());
        List<Film> list = new ArrayList<>(films.values());
        return list;
    }

    @Override
    @PostMapping
    public Film add(@RequestBody Film film) {
        log.info(LogMessage.ADD_FILM.getLogMassage());
        validate(film);
        film.setId(currentId++);
        int id = film.getId();
        films.put(id, film);
        log.info(LogMessage.ADD_FILM_DONE.getLogMassage());
        return film;
    }

    @Override
    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info(LogMessage.UPDATE_FILM.getLogMassage());
        validate(film);
        int id = film.getId();
        if (films.containsKey(id)) {
            films.put(id, film);
            log.info(LogMessage.UPDATE_FILM_DONE.getLogMassage());
        } else {
            log.warn(LogMessage.FILM_NOT_FOUND.getLogMassage() + id);
            throw new ValidationException(LogMessage.FILM_NOT_FOUND.getLogMassage() + id);
        }
        return film;
    }

    @Override
    public void validate (Film film) throws ValidationException{
        StringBuilder message = new StringBuilder(LogMessage.VALIDATION_FAIL.getLogMassage());
        String name = film.getName();
        if (name == null || name.isBlank()) {
            message.append(LogMessage.NOT_VALID_FILM_NAME.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
        if (film.getDescription().length() > MAX_DESCRIPTION_LENGTH) {
            message.append(LogMessage.NOT_VALID_DESCRIPTION.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
        if (film.getReleaseDate().isBefore(DATE_OF_FIRST_MOVIE)) {
            message.append(LogMessage.NOT_VALID_RELEASE_DATE.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
        if (film.getDuration() <= 0) {
            message.append(LogMessage.NOT_VALID_DURATION.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
    }
}