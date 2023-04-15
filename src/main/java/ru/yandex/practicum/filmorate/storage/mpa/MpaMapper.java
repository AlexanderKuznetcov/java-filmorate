package ru.yandex.practicum.filmorate.storage.mpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.sql.ResultSet;
import java.sql.SQLException;

public class MpaMapper implements RowMapper<Mpa> {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    @Override
    public Mpa mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        Mpa mpa = new Mpa(resultSet.getInt("mpa_id"), resultSet.getString("mpa_name"));
        log.info("Найден MPA: id={}, name={}", resultSet.getInt("mpa_id"),
                resultSet.getString("mpa_name"));
        return mpa;
    }
}
