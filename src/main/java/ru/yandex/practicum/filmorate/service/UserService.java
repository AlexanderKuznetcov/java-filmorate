package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> getUsers() {
        return inMemoryUserStorage.get();
    }

    public User getUser(int id) {
        checkUserInStorage(id);
        User user = inMemoryUserStorage.getFromId(id);
        return user;
    }

    public User addUser(User user) {
        validateUser(user);
        User addUser = inMemoryUserStorage.add(user);
        log.info(LogMessage.ADD_USER_DONE.getLogMassage(), addUser.getId());
        return addUser;
    }

    public User updateUser(User user) {
        int id = user.getId();
        checkUserInStorage(id);
        validateUser(user);
        User updateUser = inMemoryUserStorage.update(user);
        log.info(LogMessage.UPDATE_USER_DONE.getLogMassage());
        return updateUser;
    }

    public void addFriend(int id, int idFriend) {
        checkUserInStorage(id);
        checkUserInStorage(idFriend);
        User user = inMemoryUserStorage.getFromId(id);
        User friend = inMemoryUserStorage.getFromId(idFriend);
        user.addFriend(idFriend);
        friend.addFriend(id);
        log.info(LogMessage.ADD_FRIEND_DONE.getLogMassage(), id, idFriend);
    }

    public void deleteFriend(int id, int idNotFriend) {
        checkUserInStorage(id);
        checkUserInStorage(idNotFriend);
        User user = inMemoryUserStorage.getFromId(id);
        User notFriend = inMemoryUserStorage.getFromId(idNotFriend);
        user.deleteFriend(idNotFriend);
        notFriend.deleteFriend(id);
        log.info(LogMessage.DEL_FRIEND_DONE.getLogMassage(), id, idNotFriend);
    }

    public List<User> getUserFriends(int id) {
        User user = inMemoryUserStorage.getFromId(id);
        if (user == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage(), id);
            throw new ObjectNotFoundException(LogMessage.USER_NOT_FOUND_EXC.getLogMassage() + id);
        }
        List<User> userFriends = new ArrayList<>();
        for (int i : user.getFriends()) {
            userFriends.add(inMemoryUserStorage.getFromId(i));
        }
        return userFriends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        checkUserInStorage(id);
        checkUserInStorage(otherId);
        List<User> friendsUser1 = this.getUserFriends(id);
        List<User> friendsUser2 = this.getUserFriends(otherId);
        return friendsUser1.stream().filter(friendsUser2::contains).collect(Collectors.toList());
    }

    private void checkUserInStorage (int id) {
        User user = inMemoryUserStorage.getFromId(id);
        if (user == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage(), id);
            throw new ObjectNotFoundException(LogMessage.USER_NOT_FOUND_EXC.getLogMassage() + id);
        }
    }

    public void validateUser (User user) throws ValidationException {
        StringBuilder message = new StringBuilder(LogMessage.VALIDATION_FAIL.getLogMassage());
        String email = user.getEmail();
        if (email == null || email.isBlank() || !email.contains("@")) {
            message.append(LogMessage.NOT_VALID_EMAIL.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
        String login = user.getLogin();
        if (login == null || login.isBlank() || login.contains(" ")) {
            message.append(LogMessage.NOT_VALID_LOGIN.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(login);
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            message.append(LogMessage.NOT_VALID_BIRTHDAY.getLogMassage());
            log.warn(message.toString());
            throw new ValidationException(message.toString());
        }
    }
}