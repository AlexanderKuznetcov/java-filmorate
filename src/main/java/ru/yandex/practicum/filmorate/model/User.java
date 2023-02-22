package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private List<Integer> friends = new ArrayList<>();

    public void addFriend (Integer userId) {
        if (!friends.contains(userId)) {
            friends.add(userId);
        }
    }

    public void deleteFriend (Integer userId) {
        if (friends.contains(userId)) {
            friends.remove(userId);
        }
    }

    public List<Integer> getFriends () {
        return friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id && Objects.equals(email, user.email) && Objects.equals(login, user.login)
                && Objects.equals(name, user.name) && Objects.equals(birthday, user.birthday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, login, name);
    }
}