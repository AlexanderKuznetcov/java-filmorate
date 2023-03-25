package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.film.InMemoryFilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class FilmService {
    private static final int MAX_DESCRIPTION_LENGTH = 200;
    private static final LocalDate DATE_OF_FIRST_MOVIE = LocalDate.of(1895, 12, 28);
    private static final Comparator<Film> POPULAR_FILM_COMPARATOR =
            (film1, film2) -> film2.getLikesCount() - film1.getLikesCount();
    private final InMemoryFilmStorage inMemoryFilmStorage;

    @Autowired
    public FilmService(InMemoryFilmStorage inMemoryFilmStorage) {
        this.inMemoryFilmStorage = inMemoryFilmStorage;
    }

    public List<Film> getFilms() {
        log.info(LogMessage.GET_FILMS.getLogMassage());
        return inMemoryFilmStorage.get();
    }

    public Film getFilm(int id) {
        checkFilmInStorage(id);
        Film film = inMemoryFilmStorage.getFromId(id);
        return film;
    }


    public Film addFilm(Film film) {
        validateFilm(film);
        Film addFilm = inMemoryFilmStorage.add(film);
        log.info(LogMessage.ADD_FILM_DONE.getLogMassage(), addFilm.getId());
        return addFilm;
    }

    public void addLike(int filmId, int userId) {
        Film film = inMemoryFilmStorage.getFromId(filmId);
        checkFilmInStorage(filmId);
        log.info(LogMessage.ADD_LIKE_DONE.getLogMassage(), filmId, userId);
        film.addLike(userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = inMemoryFilmStorage.getFromId(filmId);
        checkFilmInStorage(filmId);
        checkLikeOfUser(filmId, userId);
        film.deleteLike(userId);
        log.info(LogMessage.DEL_LIKE_DONE.getLogMassage(), filmId, userId);
    }

    public List<Film> getPopular (int count) {
        ArrayList<Film> allFilms =(ArrayList<Film>) inMemoryFilmStorage.get();
        allFilms.sort(POPULAR_FILM_COMPARATOR);
        Stream<Film> mostPopularFilms = allFilms.stream().limit(count);
        return mostPopularFilms.collect(Collectors.toList());
    }

    public Film updateFilm(Film film) {
        int id = film.getId();
        validateFilm(film);
        checkFilmInStorage(id);
        Film updateFilm = inMemoryFilmStorage.update(film);
        log.info(LogMessage.UPDATE_FILM_DONE.getLogMassage(), id);
        return updateFilm;
    }

    private void checkFilmInStorage (int filmId) {
        if (inMemoryFilmStorage.getFromId(filmId) == null) {
            log.warn(LogMessage.FILM_NOT_FOUND.getLogMassage(), filmId);
            throw new ObjectNotFoundException(LogMessage.FILM_NOT_FOUND_EXC.getLogMassage() + filmId);
        }
    }

    private void checkLikeOfUser (int filmId, int userId) {
        Film film = inMemoryFilmStorage.getFromId(filmId);
        if (!film.getUsersIdWhoLike().contains(userId)) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage(), userId);
            throw new ObjectNotFoundException(LogMessage.USER_NOT_FOUND_EXC.getLogMassage() + userId);
        }
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
