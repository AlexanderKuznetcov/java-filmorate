package ru.yandex.practicum.filmorate.storage.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final Logger log = LoggerFactory.getLogger(UserDbStorage.class);

    public UserDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<User> get(){
        List<User> usersList = new ArrayList<>();
        SqlRowSet idR = jdbcTemplate.queryForRowSet("SELECT user_id FROM users");
        while(idR.next()) {
            User user = this.getFromId(idR.getInt("user_id"));
            usersList.add(user);
        }
        return usersList;
    }

    public User add(User user){
        jdbcTemplate.execute("INSERT INTO users (email, login, name, birthday) " +
                "VALUES('"+ user.getEmail() +"', '" + user.getLogin() + "', '" + user.getName() + "', '"
                + user.getBirthday() + "')");
        SqlRowSet iR = jdbcTemplate.queryForRowSet("SELECT MAX(user_id) AS max_id FROM users");
        if(iR.next()) {
            return this.getFromId(iR.getInt("max_id"));
        } else {
            return null;
        }
    }

    public User update(User user){
        jdbcTemplate.execute("UPDATE users SET email='" + user.getEmail() + "', login='" + user.getLogin() +
                "', name ='" + user.getName() + "', birthday='"+ user.getBirthday() + "' WHERE user_id="
                + user.getId());
        return this.getFromId(user.getId());
    }

    public User delete(User user){
        jdbcTemplate.execute("DELETE FROM users WHERE user_id=" + user.getId());
        jdbcTemplate.execute("DELETE FROM friends WHERE user_id=" + user.getId());
        jdbcTemplate.execute("DELETE FROM films_likes WHERE user_id=" + user.getId());
        return user;
    }

    public void addFriend(int id, int idFriend) {
        jdbcTemplate.execute("INSERT INTO friends (user_id, friend_id) " +
                "VALUES("+ id +", " + idFriend + ")");
    }

    public void deleteFriend(int id, int idNotFriend) {
        jdbcTemplate.execute("DELETE FROM friends WHERE user_id=" + id + " AND friend_id=" + idNotFriend);
    }

    public List<User> getFriends(int id) {
        List<User> friends = new ArrayList<>();
        SqlRowSet fR = jdbcTemplate.queryForRowSet("SELECT friend_id FROM friends WHERE user_id=?", id);
        while(fR.next()) {
            friends.add(this.getFromId(fR.getInt("friend_id")));
        }
        return friends;
    }

    public User getFromId(int id) {
        SqlRowSet uR = jdbcTemplate.queryForRowSet("SELECT * FROM users WHERE user_id=?", id);
        if(uR.next()) {
            log.info("Найден пользователь: id = {}, login = {}, name = {}", uR.getString("user_id"),
                    uR.getString("login"), uR.getString("name"));
            User user = new User(uR.getInt("user_id"),
                    uR.getString("email"), uR.getString("login"),
                    uR.getString("name"), uR.getDate("birthday").toLocalDate());
            return user;
        } else {
            return null;
        }
    }
}
