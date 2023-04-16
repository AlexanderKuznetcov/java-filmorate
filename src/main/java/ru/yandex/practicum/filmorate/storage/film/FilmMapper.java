package ru.yandex.practicum.filmorate.storage.film;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.genre.GenreDBStorage;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;


public class FilmMapper implements RowMapper<Film> {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    private final JdbcTemplate jdbcTemplate;
    private final GenreDBStorage genreDBStorage;

    public FilmMapper(JdbcTemplate jdbcTemplate, GenreDBStorage genreDBStorage) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreDBStorage = genreDBStorage;
    }

    @Override
    public Film mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        List<Genre> genreList = new ArrayList<>();
        SqlRowSet genreIdR = jdbcTemplate.queryForRowSet("SELECT genre_id FROM films_genres WHERE film_id=?",
                resultSet.getInt("film_id"));
        while (genreIdR.next()) {
            Genre genre = genreDBStorage.getFromId(genreIdR.getInt("genre_id"));
            genreList.add(genre);
        }
        Film film = new Film(resultSet.getInt("film_id"),
                resultSet.getString("name"), resultSet.getString("description"),
                resultSet.getDate("release_date").toLocalDate(), resultSet.getInt("duration"),
                new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name")),
                genreList, resultSet.getInt("rate"));
        log.info("Найден фильм: id={}, name={}, description={}", resultSet.getInt("film_id"),
                resultSet.getString("name"), resultSet.getString("description"));
        return film;
    }
}
