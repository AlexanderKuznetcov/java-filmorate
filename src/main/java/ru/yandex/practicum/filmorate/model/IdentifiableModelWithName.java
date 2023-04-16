package ru.yandex.practicum.filmorate.model;

import lombok.Data;

@Data
public abstract class IdentifiableModelWithName {
    protected int id;
    protected String name;
}
