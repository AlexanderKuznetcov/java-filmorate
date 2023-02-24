package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.message.LogMessage;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController implements  Controller<User>{
    private final UserService userService;

    public UserController (UserService userService) {
        this.userService = userService;
    }

    @Override
    @GetMapping
    public List<User> getAll() {
        log.info(LogMessage.GET_USERS.getLogMassage());
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable int id) {
        log.info(LogMessage.GET_USER.getLogMassage() + id);
        return userService.getUser(id);
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable int id) {
        log.info(LogMessage.GET_FRIENDS.getLogMassage() + id);
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable int id, @PathVariable int otherId) {
        log.info(LogMessage.GET_COMMON_FRIENDS.getLogMassage() + id + " и " + otherId);
        return userService.getCommonFriends(id, otherId);
    }

    @Override
    @PostMapping
    public User add(@RequestBody User user) {
        log.info(LogMessage.ADD_USER.getLogMassage());
        return userService.addUser(user);
    }

    @Override
    @PutMapping
    public User update(@RequestBody User user) {
        int id = user.getId();
        log.info(LogMessage.UPDATE_USER.getLogMassage() + id);
        return userService.updateUser(user);
    }

    @PutMapping ("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(LogMessage.ADD_FRIEND.getLogMassage() + friendId);
        userService.addFriend(id, friendId);
    }

    @DeleteMapping ("/{id}/friends/{friendId}")
    public void deleteFriend(@PathVariable int id, @PathVariable int friendId) {
        log.info(LogMessage.DEL_FRIEND.getLogMassage() + friendId);
        userService.deleteFriend(id, friendId);
    }
}