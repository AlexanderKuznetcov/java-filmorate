package ru.yandex.practicum.filmorate.storage.genre;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class GenreMapper implements RowMapper<Genre> {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    @Override
    public Genre mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Genre genre = new Genre(resultSet.getInt("genre_id"), resultSet.getString("genre_name"));
        log.info("Найден жанр: id={}, name={}", resultSet.getInt("genre_id"),
                resultSet.getString("genre_name"));
        return genre;
    }
}
