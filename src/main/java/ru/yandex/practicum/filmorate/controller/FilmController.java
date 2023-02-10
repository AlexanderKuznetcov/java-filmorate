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
        try {
            log.info(LogMessage.ADD_FILM.getLogMassage());
            validate(film);
            film.setId(currentId);
            currentId++;
            int id = film.getId();
            films.put(id, film);
            log.info(LogMessage.ADD_FILM_DONE.getLogMassage());
        } catch (ValidationException e) {
            String message = e.getMessage();
            log.warn(message);
            throw new ValidationException(message);
        }
        return film;
    }

    @Override
    @PutMapping
    public Film update(@RequestBody Film film) {
        try {
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
        } catch (ValidationException e) {
            String message = e.getMessage();
            log.warn(message);
            throw new ValidationException(message);
        }
        return film;
    }

    @Override
    public void validate (Film film) throws ValidationException{
        StringBuilder message = new StringBuilder("Валидация не прошла!");
        boolean isValid = true;
        String name = film.getName();
        if (name == null || name.isBlank()) {
            isValid = false;
            message.append(" Пустое имя фильма.");
        }
        if (film.getDescription().length() > 200) {
            isValid = false;
            message.append(" Описание более 200 символов.");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            isValid = false;
            message.append(" Слишком ранний релиз.");
        }
        if (film.getDuration() <= 0) {
            isValid = false;
            message.append(" Продолжительность отрицательная или 0.");
        }
        if (!isValid) {
            throw new ValidationException(message.toString()); //это исключение содержит сообщение о всех ошибках,
            // а не только о первой, а флаг isValid сигнализирует, что хотя бы одна ошибка есть
        }
    }


}