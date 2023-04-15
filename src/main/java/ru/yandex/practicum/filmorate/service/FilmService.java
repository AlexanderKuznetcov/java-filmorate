package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class FilmService {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate DATE_OF_FIRST_MOVIE = LocalDate.of(1895, 12, 28);
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Autowired
    public FilmService(FilmDbStorage filmDbStorage, UserDbStorage userDbStorage) {
        this.filmDbStorage = filmDbStorage;
        this.userDbStorage = userDbStorage;
    }

    public List<Film> getFilms() {
        log.info(LogMessage.GET_FILMS.getLogMassage());
        return filmDbStorage.get();
    }

    public Film getFilm(int id) {
        checkFilmInDb(id);
        Film film = filmDbStorage.getFromId(id);
        return film;
    }


    public Film addFilm(Film film) {
        validateFilm(film);
        Film addFilm = filmDbStorage.add(film);
        log.info(LogMessage.ADD_FILM_DONE.getLogMassage(), addFilm.getId());
        return addFilm;
    }

    public void addLike(int filmId, int userId) {
        checkFilmInDb(filmId);
        User user = userDbStorage.getFromId(userId);
        checkNullUser(user, userId);
        filmDbStorage.addLike(filmId);
        log.info(LogMessage.ADD_LIKE_DONE.getLogMassage(), filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        checkFilmInDb(filmId);
        User user = userDbStorage.getFromId(userId);
        checkNullUser(user, userId);
        filmDbStorage.deleteLike(userId);
        log.info(LogMessage.DEL_LIKE_DONE.getLogMassage(), filmId, userId);
    }

    public List<Film> getPopular(int count) {
        return filmDbStorage.getPopular(count);
    }

    public Film updateFilm(Film film) {
        int id = film.getId();
        validateFilm(film);
        checkFilmInDb(id);
        Film updateFilm = filmDbStorage.update(film);
        log.info(LogMessage.UPDATE_FILM_DONE.getLogMassage(), id);
        return updateFilm;
    }

    private void checkFilmInDb(int filmId) {
        if (filmDbStorage.getFromId(filmId) == null) {
            log.warn(LogMessage.FILM_NOT_FOUND.getLogMassage(), filmId);
            throw new ObjectNotFoundException(LogMessage.FILM_NOT_FOUND_EXC.getLogMassage() + filmId);
        }
    }

    private void checkNullUser(User user, int userId) {
        if (user == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage(), userId);
            throw new ObjectNotFoundException(LogMessage.USER_NOT_FOUND_EXC.getLogMassage() + userId);
        }
    }

    public void validateFilm(Film film) throws ValidationException {
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
