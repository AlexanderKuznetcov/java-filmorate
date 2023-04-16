package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController implements Controller<Film> {
    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Override
    @GetMapping
    public List<Film> getAll() {
        log.info(LogMessage.GET_FILMS.getLogMassage());
        return filmService.getFilms();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info(LogMessage.GET_FILM.getLogMassage(), id);
        return filmService.getFilm(id);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(value = "count", defaultValue = "10") int count) {
        log.info(LogMessage.GET_POPULAR.getLogMassage(), count);
        return filmService.getPopular(count);
    }

    @Override
    @PostMapping
    public Film add(@RequestBody Film film) {
        log.info(LogMessage.ADD_FILM.getLogMassage());
        return filmService.addFilm(film);
    }

    @Override
    @PutMapping
    public Film update(@RequestBody Film film) {
        int id = film.getId();
        log.info(LogMessage.UPDATE_FILM.getLogMassage(), id);
        return filmService.updateFilm(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info(LogMessage.ADD_LIKE.getLogMassage(), id);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info(LogMessage.DEL_LIKE.getLogMassage(), id);
        filmService.deleteLike(id, userId);
    }
}