package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.StorageAbs;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;
import java.util.*;

@Repository
@Qualifier("dataBase")
public class UserDbStorage extends StorageAbs<User> implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User create(User user) {
        super.create(user);
        String sqlQuery = "INSERT INTO users(name,login, email,birthday)" +
                "values(?,?,?,?)";
        jdbcTemplate.update(sqlQuery, user.getName(),
                user.getLogin(), user.getEmail(), user.getBirthday());
        return user;
    }

    @Override
    public void delete(User user) {
        String sqlQuery = "DELETE FROM users where name=?";
        jdbcTemplate.update(sqlQuery, user.getName());
    }

    @Override
    public User update(User user) {
        getById(user.getId());
        String sqlQuery = "UPDATE users " +
                "SET name=?," +
                "login=?, " +
                "email=?, " +
                "birthday=? " +
                "WHERE id=?";
        jdbcTemplate.update(sqlQuery,
                user.getName(),
                user.getLogin(),
                user.getEmail(),
                user.getBirthday(),
                user.getId());
        return user;
    }

    @Override
    public Collection<User> findAll() {
        String sqlQuery = "SELECT * FROM USERS";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        List<User> users = new ArrayList<>();
        while (srs.next()) {
            users.add(userMap(srs));
        }
        return users;
    }

    @Override
    public User getById(Integer id) {
        String sqlQuery = "SELECT * FROM users where id=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            return userMap(srs);
        } else {
            throw new NotFoundException("Пользователь с данным id не найден");
        }
    }

    public static User userMap(SqlRowSet srs) {
        Integer id = srs.getInt("id");
        String name = srs.getString("name");
        String login = srs.getString("login");
        String email = srs.getString("email");
        LocalDate birthday = Objects.requireNonNull(srs.getTimestamp("birthday")).toLocalDateTime().toLocalDate();

        return User.builder().id(id).name(name).login(login).
                email(email).birthday(birthday).build();
    }

    public Optional<User> getUserById(Integer id) {
        String sqlQuery = "SELECT * FROM films where id=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            User user = userMap(srs);
            return Optional.of(user);
        }
        return Optional.empty();
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
                "WHERE userID=?)";
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
                "AND friendID NOT in (?, ?))";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, friend1, friend2, friend1, friend2);
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