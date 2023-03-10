package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.enums.UserControllerMessages;
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
        log.info(UserControllerMessages.UserControllerMessage(UserControllerMessages.USER_CREATE_RESPONSE));
        userService.create(user);
        return user;
    }

    @PutMapping
    @Override
    public User update(@Valid @RequestBody User user) {
        log.info(UserControllerMessages.UserControllerMessage(UserControllerMessages.USER_UPDATE_RESPONSE));
        userService.update(user);
        return user;
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info(UserControllerMessages.UserControllerMessage(UserControllerMessages.USER_ADD_FRIENDS));
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Integer id, @PathVariable Integer friendId) {
        log.info(UserControllerMessages.UserControllerMessage(UserControllerMessages.USER_REMOVE_FRIEND));
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getAllFriends(@PathVariable Integer id) {
        log.info(UserControllerMessages.UserControllerMessage(UserControllerMessages.USER_ALL_FRIENDS));
        return userService.getAllFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable Integer id, @PathVariable Integer otherId) {
        log.info(UserControllerMessages.UserControllerMessage(UserControllerMessages.USER_GET_COMMON_FRIENDS));
        return userService.getCommonFriends(id, otherId);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable Integer id) {
        return userService.getById(id);
    }
}