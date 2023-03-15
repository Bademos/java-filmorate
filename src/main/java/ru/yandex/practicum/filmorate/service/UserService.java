package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;


import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
@Slf4j
public class UserService {
    final private UserStorage storage;
    final protected IdGenerator idGenerator;

    @Autowired
    public UserService(@Qualifier("dataBase") UserStorage storage) {
        this.storage = storage;
        this.idGenerator = new IdGenerator();
    }

    public User create(User user) {
        validate(user, UserMessages.userMessage(UserMessages.INCORRECT_USER_FORM));
        fixVoidName(user);
        user.setId(idGenerator.getId());
        User result = storage.create(user);
        log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_ADDED) + user);
        return result;
    }

    public User update(User user) {
        validate(user, UserMessages.userMessage(UserMessages.INCORRECT_UPDATE_USER_FORM));
        fixVoidName(user);
        User result = storage.update(user);
        log.info(UserMessages.userMessage(UserMessages.USER_SUCCESS_UPDATED) + user);
        return result;
    }

    public void addFriend(Integer userId, Integer friendId) {
        checkUser(userId, friendId);
        storage.sendInvite(userId,friendId);
        log.info(UserMessages.userMessage(UserMessages.ADD_USER_FRIEND));
    }

    public void removeFriend(Integer userId, Integer friendId) {
        checkUser(userId, friendId);
        storage.removeFriend(userId,friendId);
        log.info(UserMessages.userMessage(UserMessages.DELETE_USER_FRIEND));
    }

    public List<User> getAllFriends(Integer userId) {
        checkUser(userId, userId);
        User user = getById(userId);
        List<User> result = user.getFriends().stream().
                map(id -> storage.getListOfEntities().get(id)).
                sorted(Comparator.comparingInt(User::getId)).
                collect(Collectors.toList());
        result = storage.getListOfFriends(userId);
        log.info(UserMessages.userMessage(UserMessages.ALL_FRIENDS) + userId + result);
        return result;
    }

    public List<User> getCommonFriends(Integer user1Id, Integer user2Id) {
        checkUser(user1Id, user2Id);
        List<User> result = storage.getCommonFriends(user1Id,user2Id);
        log.info(UserMessages.userMessage(UserMessages.COMMON_FRIENDS) + user1Id + " и " + user2Id + " " + result);
        return result;
    }

    private void checkUser(Integer userId, Integer friendId) {
        storage.getById(userId);
        storage.getById(friendId);
    }

    public User getById(Integer id) {
        log.info(Messages.message(Messages.ID_REQUEST) + id);
        return storage.getById(id);
    }

    public Collection<User> findAll() {
        log.info(UserMessages.userMessage(UserMessages.CURRENT_USERS_CONDITION) + storage.getListOfEntities().size());
        return storage.findAll();
    }

    protected void validate(User user, String message) {
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }

    private void fixVoidName(User user) {
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}