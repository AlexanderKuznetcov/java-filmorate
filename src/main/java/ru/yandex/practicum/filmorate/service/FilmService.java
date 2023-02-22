package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@Slf4j
@Service
public class FilmService {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate DATE_OF_FIRST_MOVIE = LocalDate.of(1895, 12, 28);
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public List<Film> getFilms() {
        log.info(LogMessage.GET_FILMS.getLogMassage());
        return inMemoryFilmStorage.getFilms();
    }

    public Film getFilm(int id) throws FilmNotFoundException {
        log.info(LogMessage.GET_FILM.getLogMassage() + id);
        Film film = inMemoryFilmStorage.getFilmFromId(id);
        if (film == null) {
            log.warn(LogMessage.FILM_NOT_FOUND.getLogMassage() + id);
            throw new FilmNotFoundException(LogMessage.FILM_NOT_FOUND.getLogMassage() + id);
        }
        return film;
    }


    public Film addFilm(Film film) {
        log.info(LogMessage.ADD_FILM.getLogMassage());
        validateFilm(film);
        Film addFilm = inMemoryFilmStorage.addFilm(film);
        log.info(LogMessage.ADD_FILM_DONE.getLogMassage() + addFilm.getId());
        return addFilm;
    }

    public void addLike(int filmId, int userId) {
        log.info(LogMessage.ADD_LIKE.getLogMassage() + filmId);
        Film film = inMemoryFilmStorage.getFilmFromId(filmId);
        if (film == null) {
            log.warn(LogMessage.FILM_NOT_FOUND.getLogMassage() + filmId);
            throw new FilmNotFoundException(LogMessage.FILM_NOT_FOUND.getLogMassage() + filmId);
        }
        log.info(LogMessage.ADD_LIKE_DONE.getLogMassage());
        film.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        log.info(LogMessage.DEL_LIKE.getLogMassage() + filmId);
        Film film = inMemoryFilmStorage.getFilmFromId(filmId);
        if (film == null) {
            log.warn(LogMessage.FILM_NOT_FOUND.getLogMassage() + filmId);
            throw new FilmNotFoundException(LogMessage.FILM_NOT_FOUND.getLogMassage() + filmId);
        }
        if (!film.getLikes().contains(userId)) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + userId);
            throw new FilmNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + userId);
        }
        log.info(LogMessage.DEL_LIKE_DONE.getLogMassage());
        film.deleteLike(userId);
    }

    public List<Film> getPopular (int count) {
        log.info(LogMessage.GET_POPULAR.getLogMassage() + count);
        ArrayList<Film> allFilms =(ArrayList<Film>) inMemoryFilmStorage.getFilms();
        allFilms.sort(Comparator.comparing(Film::getLikesCountNeg));
        List<Film> mostPopularFilms = new ArrayList<>();
        int i = 0;
        for (Film film : allFilms) {
            if (i == count) {
                return mostPopularFilms;
            }
            mostPopularFilms.add(film);
            i++;
        }
        return mostPopularFilms;
    }

    public Film updateFilm(Film film) {
        int id = film.getId();
        log.info(LogMessage.UPDATE_FILM.getLogMassage() + id);
        validateFilm(film);
        if (inMemoryFilmStorage.getFilmFromId(id) == null) {
            log.warn(LogMessage.FILM_NOT_FOUND.getLogMassage() + id);
            throw new FilmNotFoundException(LogMessage.FILM_NOT_FOUND.getLogMassage() + id);
        }
        Film updateFilm = inMemoryFilmStorage.updateFilm(film);
        log.info(LogMessage.UPDATE_FILM_DONE.getLogMassage());
        return updateFilm;
    }


    public void validateFilm (Film film) throws ValidationException {
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
