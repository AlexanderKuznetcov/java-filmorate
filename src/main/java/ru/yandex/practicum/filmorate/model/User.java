package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class User {
    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    int id;
    String email;
    String login;
    String name;
    LocalDate birthday;

    public void setBirthday(String releaseDate) {
        this.birthday = LocalDate.parse(releaseDate, DATE_FORMATTER);
    }
}