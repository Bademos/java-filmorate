package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private int id = 0;
    private final Map<Integer, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        log.info(userMessage(UserMessages.CURRENT_USERS_CONDITION) + users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (!userValidation(user)) {
            log.debug(userMessage(UserMessages.INCORRECT_USER_FORM));
            throw new ValidationException(userMessage(UserMessages.INCORRECT_USER_FORM));
        }
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getId() == 0) {
            user.setId(setId());
        }
        users.put(user.getId(), user);
        log.info(userMessage(UserMessages.USER_SUCCESS_ADDED) + user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (!userValidation(user)) {
            log.debug(userMessage(UserMessages.INCORRECT_UPDATE_USER_FORM));
            throw new ValidationException(userMessage(UserMessages.INCORRECT_UPDATE_USER_FORM));
        }
        if (user.getId() == 0) {
            user.setId(setId());
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        if (users.containsKey(user.getId())) {
            users.put(user.getId(), user);
            log.info(userMessage(UserMessages.USER_SUCCESS_UPDATED)+ user);
        } else {
            log.debug(userMessage(UserMessages.USER_IS_NOT_IN_LIST));
            throw new ValidationException(userMessage(UserMessages.USER_IS_NOT_IN_LIST));
        }
        return user;
    }

    public int setId() {
        id += 1;
        return id;
    }

    public boolean userValidation(User user) {
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

    public String userMessage(UserMessages um){
        switch (um){
            case CURRENT_USERS_CONDITION:return "Текущее количество пользоваелей: ";
            case USER_IS_NOT_IN_LIST:return "Такого пользователя нет в списке";
            case INCORRECT_USER_FORM:return "Некоректно заполнена форма регистрации учетной записи";
            case INCORRECT_UPDATE_USER_FORM:return "Некоректно заполнена форма обновления учетной записи";
            case USER_SUCCESS_ADDED:return "Успешно добавлен пользователь: ";
            case USER_SUCCESS_UPDATED:return "Успешно обновлена учетная запись пользователя: ";
            default:return "Ошибка неизвестного рода";
        }
    }
}
enum UserMessages {
    INCORRECT_USER_FORM,
    INCORRECT_UPDATE_USER_FORM,
    USER_IS_NOT_IN_LIST,
    CURRENT_USERS_CONDITION,
    USER_SUCCESS_ADDED,
    USER_SUCCESS_UPDATED
}