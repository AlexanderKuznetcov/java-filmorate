package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

@Data
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private TreeSet<Integer> likes = new TreeSet<>();

    public void addLike(int userId) {
        likes.add(userId);
    }

    public void deleteLike(Integer userId) {
        likes.remove(userId);
    }

    public int getLikesCountNeg() {
        return -likes.size();
    }
}