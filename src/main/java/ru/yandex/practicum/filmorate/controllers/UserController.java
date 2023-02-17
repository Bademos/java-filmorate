package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User> {
    final private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Override
    public Collection<User> findAll() {
        log.info(UserMessages.userMessage(UserMessages.CURRENT_USERS_CONDITION) + listOfEntities.size());
        return userService.findAll();
    }

    @PostMapping
    @Override
    public User create(@Valid @RequestBody User user) {
        userService.create(user);
        log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_ADDED) + user);
        return user;
    }

    @PutMapping
    @Override
    public User update(@Valid @RequestBody User user) {
        userService.update(user);
        log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_UPDATED));
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.addFriend(id, friendId);
        log.info(UserMessages.userMessage(UserMessages.ADD_USER_FRIEND));
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        userService.removeFriend(id, friendId);
        log.info(UserMessages.userMessage(UserMessages.DELETE_USER_FRIEND));
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        log.info("Друзья пользователя с ID" + id + userService.getAllFriends(id));
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info("Общие друзья пользователей с ID" + id + " и " + otherId + userService.getCommonFriends(id, otherId));
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        log.info("Пользователь с ID " + id + " " + userService.getById(id));
        return userService.getById(id);
    }
}