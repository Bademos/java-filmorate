package ru.yandex.practicum.filmorate.storage;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;

@Repository
public class FriendsDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public FriendsDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void sendInvite(int userID, int friendID) {
        String sqlQuery = "INSERT INTO friends(userID,friendID,statusId) " +
                "values(?,?,?)";
        jdbcTemplate.update(sqlQuery, userID, friendID, 1);
    }

    public void affirmFriendship(int userID, int friendID) {
        String sqlQuery = "UPDATE friends SET statusID=2 " +
                "where userID = ? and friendID=?";
        jdbcTemplate.update(sqlQuery, userID, friendID);
    }

    public void removeFriend(int userID, int friendID) {
        String sqlQuery = "DELETE friends  " +
                "where userID = ? and friendID = ?";
        jdbcTemplate.update(sqlQuery, userID, friendID);
    }

    public List<User> getListOfFriends(int userId) {
        List<User> listOfFriends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM users " +
                "WHERE users.id in (SELECT friendID from FRIENDS " +
                "WHERE userID=?";

        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        while (srs.next()) {
            listOfFriends.add(UserDbStorage.userMap(srs));
        }
        return listOfFriends;
    }

    public List<User> getCommonFriends(int friend1, int friend2) {
        List<User> commonFriends = new ArrayList<>();
        String sqlQuery = "SELECT * FROM USERS " +
                "WHERE users.id in ( SELECT friendId from FRIENDS " +
                "WHERE userID in (?,?) " +
                "AND friendID NOT in (?, ?)";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, friend1, friend2);
        while (srs.next()) {
            commonFriends.add(UserDbStorage.userMap(srs));
        }
        return commonFriends;
    }

    public boolean isFriend(int userId, int friendId) {
        String sqlQuery = "Select * from friends where " +
                "userID = ? and friendsID = ?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, userId, friendId);
        return srs.next();
    }

}