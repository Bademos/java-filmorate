package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.*;
import java.util.stream.Collectors;

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
            throw new NotFoundException(Messages.message(Messages.IS_NOT_IN_LIST));
        }
        return user;
    }

    @Override
    public User getById(Integer id) {
        User user;
        if (listOfEntities.containsKey(id)) {
            user = listOfEntities.get(id);
        } else {
            throw new NotFoundException(UserMessages.
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

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        if (friends.containsKey(userId) && friends.containsKey(friendId)) {
            friends.get(userId).remove(friendId);
            friends.get(friendId).remove(userId);
        }
    }

    public List<User> getCommonFriends(Integer userId1, Integer userId2) {
        Set<User> resultSet = new HashSet<>();
        Set<User> usSet1 = getAllFriends(userId1);
        Set<User> usSet2 = getAllFriends(userId2);

        for (User user : usSet1) {
            if (usSet2.contains(user)) {
                resultSet.add(user);
            }
        }
        return new ArrayList<>(resultSet);
    }

    @Override
    public Set<User> getAllFriends(Integer user) {
        Set<User> usersFriends = new HashSet<>();
        Set<Integer> userFriendsId = friends.get(user);
        if (userFriendsId == null) {
            return Collections.emptySet();
        }
        for (Integer id : userFriendsId) {
            if (listOfEntities.containsKey(id)) {
                usersFriends.add(listOfEntities.get(id));
            }
        }
        return usersFriends;
    }
}