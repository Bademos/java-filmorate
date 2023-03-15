package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FriendsStorage {
    public void sendInvite(int userID, int friendID);
    public void affirmFriendship(int userID, int friendID);
    public void removeFriend(int userID, int friendID);
    public List<User> getListOfFriends(int userId);
    public List<User> getCommonFriends(int friend1, int friend2);
    public boolean isFriend(int userId, int friendId);
}
