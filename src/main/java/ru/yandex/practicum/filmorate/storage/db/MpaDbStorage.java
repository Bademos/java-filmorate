package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.List;

@Repository
public class MpaDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public MpaDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Mpa getMpaById(int id) {
        String sqlQuery = "SELECT * FROM RATING WHERE RATEID=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            return new Mpa(id, srs.getString("rate"));
        }
        return null;
    }

    public List<Mpa> getListOfMpa() {
        List<Mpa> listOfMpa = new ArrayList<>();
        String sqlQuery = "SELECT * FROM rating ";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (srs.next()) {
            listOfMpa.add(new Mpa(srs.getInt("rateid"), srs.getString("rate")));
        }
        return listOfMpa;
    }
}