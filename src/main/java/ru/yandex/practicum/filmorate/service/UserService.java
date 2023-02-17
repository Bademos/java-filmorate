package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.Storage;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
@Slf4j
public class UserService extends ServiceAbs<User> {
    @Autowired
    protected UserService(Storage<User> storage) {
        super(storage);
    }

    @Override
    public User create(User user) {
        validate(user, UserMessages.userMessage(UserMessages.INCORRECT_USER_FORM));
        String name = fixVoidName(user);
        user.setName(name);
        User result = super.create(user);
        log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_ADDED) + user);
        return result;
    }

    public User update(User user) {
        validate(user, UserMessages.userMessage(UserMessages.INCORRECT_UPDATE_USER_FORM));
        String name = fixVoidName(user);
        user.setName(name);
        User result = super.update(user);
        log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_UPDATED) + user);
        return result;
    }

    public void addFriend(Integer userId, Integer friendId) {
        checkUser(userId, friendId);
        getById(userId).addFriend(friendId);
        getById(friendId).addFriend(userId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        checkUser(userId, friendId);
        getById(userId).deleteFriend(friendId);
        getById(friendId).deleteFriend(userId);
    }

    public List<User> getAllFriends(Integer userId) {
        checkUser(userId, userId);
        User user = getById(userId);
        return user.getFriends().stream().
                map(id -> storage.getListOfEntities().get(id)).
                sorted(Comparator.comparingInt(User::getId)).
                collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        checkUser(user1Id, user2Id);
        List<User> usSet1 = getAllFriends(user1Id);
        List<User> usSet2 = getAllFriends(user2Id);
        return usSet1.stream().filter(usSet2::contains).
                sorted(Comparator.comparingInt(User::getId)).
                collect(Collectors.toList());
    }

    void checkUser(Integer userId, Integer friendId) {
        if (!storage.getListOfEntities().containsKey(userId) ||
                !storage.getListOfEntities().containsKey(friendId)) {
            throw new NotFoundException(UserMessages.userMessage(UserMessages.USER_IS_NOT_IN_LIST));
        }
    }

    @Override
    protected boolean validation(User user) {
        return !(user.getBirthday().isAfter(LocalDate.now()));
    }

    public String fixVoidName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
            return user.getLogin();
        }
        return user.getName();
    }
}