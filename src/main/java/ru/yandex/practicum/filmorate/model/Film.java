package ru.yandex.practicum.filmorate.model;
import lombok.Data;

import java.time.LocalDate;
import java.util.*;

@Data
public class Film extends IdentifiableModelWithName {
    private String name;
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Mpa mpa;
    private List<Genre> genres;
    private int rate;

    public Film() {
    }

    public Film(int id, String name, String description, LocalDate releaseDate, int duration,
                Mpa mpa, List<Genre> genres, int rate) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.mpa = mpa;
        this.genres = genres;
        this.rate = rate;
    }
}