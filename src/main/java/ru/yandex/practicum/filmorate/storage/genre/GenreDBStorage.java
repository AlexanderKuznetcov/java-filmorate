package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

@Component
public class GenreDBStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> get() {
       return jdbcTemplate.query("SELECT * FROM genres", new GenreMapper());
    }

    public Genre getFromId(int id) {
        return jdbcTemplate.query("SELECT * FROM genres WHERE genre_id=?", new Object[]{id},
                new GenreMapper()).stream().findAny().orElse(null);
    }
}
