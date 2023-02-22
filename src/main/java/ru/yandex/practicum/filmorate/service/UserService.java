package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.UserNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.InMemoryUserStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class UserService {
    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public List<User> getUsers() {
        log.info(LogMessage.GET_USERS.getLogMassage());
        return inMemoryUserStorage.getUsers();
    }

    public User getUser(int id) {
        log.info(LogMessage.GET_USER.getLogMassage() + id);
        User user = inMemoryUserStorage.getUserFromId(id);
        if (user == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
        }
        return user;
    }

    public User addUser(User user) {
        log.info(LogMessage.ADD_USER.getLogMassage());
        validateUser(user);
        User addUser = inMemoryUserStorage.addUser(user);
        log.info(LogMessage.ADD_USER_DONE.getLogMassage() + addUser.getId());
        return addUser;
    }

    public User updateUser(User user) {
        int id = user.getId();
        log.info(LogMessage.UPDATE_USER.getLogMassage() + id);
        validateUser(user);
        if (inMemoryUserStorage.getUserFromId(id) == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
        }
        User updateUser = inMemoryUserStorage.updateUser(user);
        log.info(LogMessage.UPDATE_USER_DONE.getLogMassage());
        return updateUser;
    }

    public void addFriend(int id, int idFriend) {
        log.info(LogMessage.ADD_FRIEND.getLogMassage() + idFriend);
        User user = inMemoryUserStorage.getUserFromId(id);
        if (user == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
        }
        User friend = inMemoryUserStorage.getUserFromId(idFriend);
        if (friend == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + idFriend);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + idFriend);
        }
        user.addFriend(idFriend);
        friend.addFriend(id);
        log.info(LogMessage.ADD_FRIEND_DONE.getLogMassage());
    }

    public void deleteFriend(int id, int idNotFriend) {
        log.info(LogMessage.DEL_FRIEND.getLogMassage() + idNotFriend);
        User user = inMemoryUserStorage.getUserFromId(id);
        if (user == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
        }
        User notFriend = inMemoryUserStorage.getUserFromId(idNotFriend);
        if (notFriend == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + idNotFriend);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + idNotFriend);
        }
        user.deleteFriend(idNotFriend);
        notFriend.deleteFriend(id);
        log.info(LogMessage.DEL_FRIEND_DONE.getLogMassage());
    }

    public List<User> getUserFriends(int id) {
        log.info(LogMessage.GET_FRIENDS.getLogMassage() + id);
        User user = inMemoryUserStorage.getUserFromId(id);
        if (user == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
        }
        List<User> userFriends = new ArrayList<>();
        for (int i : user.getFriends()) {
            userFriends.add(inMemoryUserStorage.getUserFromId(i));
        }
        return userFriends;
    }

    public List<User> getCommonFriends(int id, int otherId) {
        log.info(LogMessage.GET_COMMON_FRIENDS.getLogMassage() + id + " и " + otherId);
        User user = inMemoryUserStorage.getUserFromId(id);
        if (user == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + id);
        }
        User otherUser = inMemoryUserStorage.getUserFromId(otherId);
        if (otherUser == null) {
            log.warn(LogMessage.USER_NOT_FOUND.getLogMassage() + otherId);
            throw new UserNotFoundException(LogMessage.USER_NOT_FOUND.getLogMassage() + otherId);
        }
        List<User> commonUsers = new ArrayList<>();
        List<User> userFriends = this.getUserFriends(id);
        List<User> otherUserFriends = this.getUserFriends(otherId);
        for (User userFriend : userFriends) {
            for (User otherUserFriend : otherUserFriends) {
                if (userFriend.equals(otherUserFriend)) {
                    commonUsers.add(userFriend);
                    break;
                }
            }
        }
        return commonUsers;
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