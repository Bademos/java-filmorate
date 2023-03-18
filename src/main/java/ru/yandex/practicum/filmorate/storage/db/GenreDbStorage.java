package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class GenreDbStorage {
    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addGenres(Film film, List<Genre> genres){
            int filmID = film.getId();
            deleteAllGenresByID(filmID);
            String sqlQuery = "INSERT INTO GENRE_FILM(FILM_ID,GENREID) "+
                    "VALUES(?,?)";
            List<Genre> genresList = genres;
            this.jdbcTemplate.batchUpdate(
                sqlQuery,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setInt(1, filmID);
                        ps.setInt(2, genresList.get(i).getId());
                    }
                    public int getBatchSize() {
                        return genresList.size();
                    }
                });
        }

        public void deleteAllGenresByID(int filmID){
            String sglQuery = "DELETE GENRE_FILM WHERE genreID=?";
            jdbcTemplate.update(sglQuery,filmID);
        }

        public Genre getGenreById(int id){
            String sqlQuery = "SELECT * FROM GENRE WHERE GENREID=?";
            SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery,id);
            if(srs.next()){
                return new Genre(id,srs.getString("genre"));
            }
            return null;
        }

        public List<Genre> getAllGenres(){
            List<Genre> genres = new ArrayList<>();
            String sqlQuery = "SELECT * FROM GENRE ";
            SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
            while (srs.next()){
                genres.add(new Genre(srs.getInt("genreid"),srs.getString("genre")));
            }
            return genres;
        }
}