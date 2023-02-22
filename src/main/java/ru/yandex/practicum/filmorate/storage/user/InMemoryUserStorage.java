package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage{
    private final Map<Integer, User> users= new HashMap<>();
    private int currentId = 1;

    @Override
    public List<User> getUsers() {
        List<User> list = new ArrayList<>(users.values());
        return list;
    }

    public User getUserFromId(int id) {
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        user.setId(currentId++);
        int id = user.getId();
        users.put(id, user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        int id = user.getId();
        users.put(id, user);
        return user;
    }


}
