package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.persistence.criteria.CriteriaBuilder;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    protected final Map<Integer, User> listOfEntities;
    private final Map<Integer, Set<Integer>> friends;

    public InMemoryUserStorage() {
        this.listOfEntities = new HashMap<>();
        this.friends = new HashMap<>();
    }

    @Override
    public Collection<User> findAll() {
        log.info(Messages.message(Messages.CURRENT_CONDITION) + listOfEntities.size());
        return listOfEntities.values();
    }

    @Override
    public User create(User user) {
        listOfEntities.put(user.getId(), user);
        log.info(Messages.message(Messages.SUCCESS_ADDED) + user);
        return user;
    }

    @Override
    public User update(User user) {
        if (listOfEntities.containsKey(user.getId())) {
            listOfEntities.put(user.getId(), user);
            log.info(Messages.message(Messages.SUCCESS_UPDATED) + user);
        } else {
            log.debug(Messages.message(Messages.IS_NOT_IN_LIST));
            throw new ValidationException(Messages.message(Messages.IS_NOT_IN_LIST));
        }
        return user;
    }

    @Override
    public User getById(Integer id) {
        User user;
        if (listOfEntities.containsKey(id)) {
            user = listOfEntities.get(id);
        } else {
            throw new ValidationException(UserMessages.
                    userMessage(UserMessages.USER_IS_NOT_IN_LIST));
        }
        return listOfEntities.get(id);
    }

    @Override
    public Map<Integer, User> getListOfEntities() {
        return listOfEntities;
    }

    public void addFriend(Integer userId, Integer friendId) {
        if (!friends.containsKey(userId)) {
            friends.put(userId, new HashSet<>());
        }
        friends.get(userId).add(friendId);
    }


    public void removeFriend(Integer userId, Integer friendId) {
        if (friends.containsKey(userId)) {
            friends.get(userId).remove(friendId);
            friends.get(friendId).remove(userId);
        }
    }

    public Set<User> getCommonFriends(Integer userId1, Integer userId2) {
        Set<User> resultSet = new HashSet<>();
        Set<User> usSet1 = getAllFriends(listOfEntities.get(userId1));
        Set<User> usSet2 = getAllFriends(listOfEntities.get(userId2));

        for (User user : usSet1) {
            if (usSet2.contains(user)) {
                resultSet.add(user);
            }
        }
        return resultSet;
    }

    public Set<User> getAllFriends(User user) {
        Set<User> usersFriends = new HashSet<>();
        Set<Integer> userId = friends.get(user.getId());
        for (Integer id : userId) {
            usersFriends.add(listOfEntities.get(id));
        }
        return usersFriends;
    }

    public void put(Integer id, User user) {
        listOfEntities.put(id, user);
    }
}