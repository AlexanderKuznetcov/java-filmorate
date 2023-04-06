package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film extends IdentifiableModel{
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private List<Genre> genres;
    private int rate;

    @JsonIgnore
    private Set<Integer> usersIdWhoLike = new TreeSet<>();


    public Film() {
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration,
                Mpa mpa, List<Genre> genres) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
    }

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