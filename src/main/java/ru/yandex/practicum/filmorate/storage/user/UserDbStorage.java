package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserDbStorage implements Storage<User> {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> get() {
        return jdbcTemplate.query("SELECT * FROM users",new UserMapper());
    }

    public User add(User user) {
        jdbcTemplate.update("INSERT INTO users (email, login, name, birthday) " +
                "VALUES( ?, ?, ?, ?)", user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
        SqlRowSet iR = jdbcTemplate.queryForRowSet("SELECT MAX(user_id) AS max_id FROM users");
        if (iR.next()) {
            return this.getFromId(iR.getInt("max_id"));
        } else {
            return null;
        }
    }

    public User update(User user) {
        jdbcTemplate.update("UPDATE users SET email=?, login=?, name =?, birthday=? WHERE user_id=?",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return this.getFromId(user.getId());
    }

    public User delete(User user) {
        jdbcTemplate.update("DELETE FROM users WHERE user_id=?", user.getId());
        return user;
    }

    public void addFriend(int id, int idFriend) {
        jdbcTemplate.update("INSERT INTO friends (user_id, friend_id) VALUES( ?, ?)", id, idFriend);
    }

    public void deleteFriend(int id, int idNotFriend) {
        jdbcTemplate.update("DELETE FROM friends WHERE user_id=? AND friend_id=?", id, idNotFriend);
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>();
        SqlRowSet fR = jdbcTemplate.queryForRowSet("SELECT friend_id FROM friends WHERE user_id=?", id);
        while (fR.next()) {
            friends.add(this.getFromId(fR.getInt("friend_id")));
        }
        return friends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        List<User> commonFriends = new ArrayList<>();
        SqlRowSet fR = jdbcTemplate.queryForRowSet("SELECT friend_id FROM friends WHERE user_id=? " +
                "AND friend_id IN (SELECT friend_id FROM friends WHERE user_id=?)", id, otherId);
        while (fR.next()) {
            commonFriends.add(this.getFromId(fR.getInt("friend_id")));
        }
        return commonFriends;
    }

    public User getFromId(int id) {
        return jdbcTemplate.query("SELECT * FROM users WHERE user_id=?", new Object[]{id},
                new UserMapper()).stream().findAny().orElse(null);
    }
}
