package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public class Genre {
    private Integer id;

    public Genre () {
    }

    public Genre (int id) {
        this.id =id;
    }
}
