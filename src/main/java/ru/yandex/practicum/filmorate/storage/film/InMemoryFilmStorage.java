package ru.yandex.practicum.filmorate.storage.film;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage{
    private final Map<Integer, Film> films = new HashMap<>();
    private int currentId = 1;

    @Override
    public List<Film> getFilms() {
        List<Film> list = new ArrayList<>(films.values());
        return list;
    }

    public Film getFilmFromId(int id) {
        return films.get(id);
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(currentId++);
        int id = film.getId();
        films.put(id, film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();
        films.put(id, film);
        return film;
    }

}
