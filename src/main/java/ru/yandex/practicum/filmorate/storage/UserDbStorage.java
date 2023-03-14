package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.User;

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
        String sqlQuery = "SELECT * FROM films where id=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            User user = userMap(srs);
            return user;
        }
        return null;
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

    public Optional <User> getUserById(Integer id){
        String sqlQuery = "SELECT * FROM films where id=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            User user = userMap(srs);
            return Optional.of(user);
        }
        return Optional.empty();
    }
}