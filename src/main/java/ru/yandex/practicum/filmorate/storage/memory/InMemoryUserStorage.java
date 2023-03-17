package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageAbs;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("memory")
public class InMemoryUserStorage extends StorageAbs<User> implements UserStorage {
    @Override
    public void addFriend(int userID, int friendID) {
        getById(friendID).addFriend(userID);
    }


    @Override
    public void removeFriend(int userID, int friendID) {
        if(isFriend(userID,friendID)){
            getById(userID).deleteFriend(friendID);
            getById(friendID).deleteFriend(userID);
        }
    }

    @Override
    public List<User> getListOfFriends(int userId) {
        User user = getById(userId);
        return user.getFriends().stream().
                map(id -> getListOfEntities().get(id)).
                sorted(Comparator.comparingInt(User::getId)).
                collect(Collectors.toList());    }

    @Override
    public List<User> getCommonFriends(int friend1, int friend2) {
        List<User> usSet1 = getListOfFriends(friend1);
        List<User> usSet2 = getListOfFriends(friend2);
        return usSet1.stream().filter(usSet2::contains).
                sorted(Comparator.comparingInt(User::getId)).
                collect(Collectors.toList());
    }

    @Override
    public boolean isFriend(int userId, int friendId) {
        return getById(userId).getFriends().contains(friendId);
    }
}