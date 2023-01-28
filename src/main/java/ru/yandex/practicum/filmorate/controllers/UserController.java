package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController extends Controller<User>{
    private final Map<Integer, User> users = getListOfEntities();

    @GetMapping
    @Override
    public Collection<User> findAll() {
        log.info(UserMessages.userMessage(UserMessages.CURRENT_USERS_CONDITION) + users.size());
        return users.values();
    }

    @PostMapping
    @Override
    public User create(@Valid @RequestBody User user) {
        validate(user,UserMessages.userMessage(UserMessages.INCORRECT_USER_FORM));
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(setId());
        users.put(user.getId(), user);
        log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_ADDED) + user);
        return user;
    }

    @PutMapping
    @Override
    public User update( @RequestBody User user) {
        validate(user,UserMessages.userMessage(UserMessages.INCORRECT_UPDATE_USER_FORM));
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_UPDATED)+ user);
        } else {
            log.debug(UserMessages.userMessage(UserMessages.USER_IS_NOT_IN_LIST));
            throw new ValidationException(UserMessages.userMessage(UserMessages.USER_IS_NOT_IN_LIST));
        }
        return user;
    }

    @Override
    public boolean validation(User user) {
        return !(user.getLogin() == null ||
                user.getLogin().isBlank()||
                user.getEmail() == null ||
                user.getEmail().isBlank()||
                !user.getEmail().contains("@") ||
                user.getBirthday().isAfter(LocalDate.now()));
    }

    public Map<Integer, User> getUsers() {
        return users;
    }
}