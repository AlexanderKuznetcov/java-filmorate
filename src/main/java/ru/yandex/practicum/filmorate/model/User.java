package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class User extends IdentifiableModel{
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    @JsonIgnore
    private Set<Integer> friends = new TreeSet<>();

    public void addFriend (Integer userId) {
            friends.add(userId);
    }

    public void deleteFriend (Integer userId) {
        if (friends.contains(userId)) {
            friends.remove(userId);
        }
    }
}