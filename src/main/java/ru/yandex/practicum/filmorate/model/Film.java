package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film extends HavingId{
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    @JsonIgnore
    private Set<Integer> usersIdWhoLike = new TreeSet<>();

    public void addLike(int userId) {
        usersIdWhoLike.add(userId);
    }

    public void deleteLike(Integer userId) {
        usersIdWhoLike.remove(userId);
    }

    public int getLikesCount() {
        return usersIdWhoLike.size();
    }
}