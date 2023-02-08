package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
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
public class UserController extends Controller<User> {

    @GetMapping
    @Override
    public Collection<User> findAll() {
        log.info(UserMessages.userMessage(UserMessages.CURRENT_USERS_CONDITION) + listOfEntities.size());
        return listOfEntities.values();
    }

    @PostMapping
    @Override
    public User create(@Valid @RequestBody User user) {
        super.create(user);
        fixVoidName(user);
        log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_ADDED) + user);
        return user;
    }

    @PutMapping
    @Override
    public User update(@Valid @RequestBody User user) {
        super.update(user);
        fixVoidName(user);
        return user;
    }

    @Override
    public boolean validation(User user) {
        return !(user.getBirthday().isAfter(LocalDate.now()));
    }

    public void fixVoidName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            listOfEntities.put(user.getId(), user);
        }
    }

    public Map<Integer, User> getUsers() {
        return listOfEntities;
    }
}