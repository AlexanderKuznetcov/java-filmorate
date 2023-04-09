package ru.yandex.practicum.filmorate.storage.genre;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.ArrayList;
import java.util.List;

@Component
public class GenreDBStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDBStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Genre> get() {
        List<Genre> genreList = new ArrayList<>();
        SqlRowSet genreR = jdbcTemplate.queryForRowSet("SELECT * FROM genres");
        while (genreR.next()) {
            Genre genre = new Genre(genreR.getInt("genre_id"), genreR.getString("name"));
            genreList.add(genre);
        }
        return genreList;
    }

    public Genre getFromId(int id) {
        SqlRowSet genreR = jdbcTemplate.queryForRowSet("SELECT * FROM genres WHERE genre_id=?", id);
        if (genreR.next()) {
            return new Genre(genreR.getInt("genre_id"), genreR.getString("name"));
        } else {
            return null;
        }
    }
}
