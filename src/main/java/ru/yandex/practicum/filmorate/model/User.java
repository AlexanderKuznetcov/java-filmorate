package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class User extends IdentifiableModelWithName{
    private String email;
    private String login;
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends = new TreeSet<>();

    public User() {
    }

    public User(int id, String email, String login, String name, LocalDate birthday) {
        this.id = id;
        this.email = email;
        this.login = login;
        this.name = name;
        this.birthday = birthday;
    }

    public void addFriend (Integer userId) {
            friends.add(userId);
    }

    public void deleteFriend (Integer userId) {
        if (friends.contains(userId)) {
            friends.remove(userId);
        }
    }
}