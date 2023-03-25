package ru.yandex.practicum.filmorate.storage;


import ru.yandex.practicum.filmorate.model.IdentifiableModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class InMemoryStorage<T extends IdentifiableModel> {
    private final Map<Integer, T> storage = new HashMap<>();
    private int currentId = 1;

    public List<T> get() {
        List<T> list = new ArrayList<>(storage.values());
        return list;
    }

    public T getFromId(int id) {
        return storage.get(id);
    }

    public T add(T t) {
        t.setId(currentId++);
        int id = t.getId();
        storage.put(id, t);
        return t;
    }

    public T update(T t) {
        int id = t.getId();
        storage.put(id, t);
        return t;
    }

    public T delete(T t) {
        int id = t.getId();
        storage.remove(id);
        return t;
    }
}
