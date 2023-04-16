package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<User> {
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    @Override
    public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        User user = new User(resultSet.getInt("user_id"),
                resultSet.getString("email"), resultSet.getString("login"),
                resultSet.getString("name"), resultSet.getDate("birthday").toLocalDate());
        log.info("Найден пользователь: id={}, name={}, login={}", resultSet.getInt("user_id"),
                resultSet.getString("name"), resultSet.getString("login"));
        return user;
    }
}
