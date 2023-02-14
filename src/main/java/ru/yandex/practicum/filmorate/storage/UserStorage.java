package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface UserStorage extends Storage<User> {
    public void addFriend(Integer userId, Integer friendId);

    public void removeFriend(Integer userId, Integer friendId);

    public List<User> getCommonFriends(Integer userId1, Integer userId2);

    public Set<User> getAllFriends(Integer user);
}
