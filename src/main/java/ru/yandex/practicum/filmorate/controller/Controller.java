package ru.yandex.practicum.filmorate.controller;

import java.util.List;

interface Controller<T> {

    List<T> get();

    T add(T t);

    T update(T t);

    void validate(T t);
}