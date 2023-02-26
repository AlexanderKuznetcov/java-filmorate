package ru.yandex.practicum.filmorate.storage;

import java.util.List;

public interface Storage<T> {
    List<T> get();

    T add(T t);

    T update(T t);

    T delete(T t);
}
