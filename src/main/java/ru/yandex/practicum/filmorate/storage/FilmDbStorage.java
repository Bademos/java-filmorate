package ru.yandex.practicum.filmorate.storage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Repository
@Qualifier("dataBase")
public class FilmDbStorage extends StorageAbs<Film> implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        super.create(film);
        String sqlQuery = "INSERT INTO films(name,description, duration,releaseDate,ratingID)"+
                "values(?,?,?,?,?)";
        jdbcTemplate.update(sqlQuery,film.getName(),film.getDescription(),
                film.getDuration(),film.getReleaseDate(),film.getMpa().getId());

        return film;
    }

    @Override
    public void delete(Film film) {
        String sqlQuery = "DELETE FROM films where name=?";
       jdbcTemplate.update(sqlQuery,film.getName());
    }

    @Override
    public Film getById(Integer id) {
        String sqlQuery = "SELECT * FROM films where id=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery,id);
        if (srs.next()){
            Film film = filmMap(srs);
            return film;
        }

            return null;
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Integer  duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();

        return Film.builder().id(id).name(name).description(description).
                duration(duration).releaseDate(releaseDate).build();
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT * FROM FILMS";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Film> films = new ArrayList<>();
        while (srs.next()){
            films.add(filmMap(srs));
        }

        return films;
    }

    @Override
    public Film update(Film film) {
        String sqlQuery = "UPDATE films "+
                "SET name=?,"+
                "description=?, "+
                "duration=?, "+
                "releaseDate=?,"+
                " ratingId=? " +
                "WHERE id=?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        return film;
    }

    public void addGenre(int filmID, List<Genre> genres){
        deleteAllGenresByID(filmID);
        String sqlQuery = "INSERT INTO GENRES_FILM(FILM_ID,GENREID) "+
                "VALUES(?,?)";
        for(Genre genre:genres){
            jdbcTemplate.update(sqlQuery,
                    filmID,genre.getId());
        }
    }

    public List<Genre> getGenres(int filmID){
        List<Genre>genres = new ArrayList<>();
        String sqlQuery = "SELECT GENREID from GENRE_FILM "+
                "WHERE FILMID=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery,filmID);
        while (srs.next()){
            genres.add(new Genre(srs.getInt("genreid")));
        }
        return genres;
    }

    public void deleteAllGenresByID(int filmID){
        String sglQuery = "DELETE GENRES_VALUES WHERE genreID=?";
        jdbcTemplate.update(sglQuery,filmID);
    }

    public void addLike(int filmID, int userID){
        String sqlQuery = "INSERT INTO likes(FILMID,USERID) "+
                "VALUES(?,?)";
        jdbcTemplate.update(sqlQuery,filmID,userID);
    }

    public void removeLike(int filmID, int userID){
        String sqlQuery = "DELETE  likes "+
                "WHERE filmId=? and userID=?";
        jdbcTemplate.update(sqlQuery,filmID,userID);
    }

    public List<Film> getSortedFilms(){
        List<Film> films = new ArrayList<>();
        String sqlQuery = "SELECT * from FILMS " +
                "LEFT JOIN likes on LIKES.FILMID=FILMS.FILMID "+
                "GROUPED BY FILMS.FILMID " +
                "ORDERED BY COUNT(LIKES.FILMID) DESC";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (srs.next()){
            films.add(filmMap(srs));
        }
        return films;
    }


    public static Film filmMap(SqlRowSet srs){
        Integer id = srs.getInt("id");
        String name = srs.getString("name");
        String description = srs.getString("description");
        Integer  duration = srs.getInt("duration");
        LocalDate releaseDate = srs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        Mpa mpa = new Mpa(srs.getInt("ratingID"));
        return Film.builder().id(id).name(name).description(description).
                duration(duration).releaseDate(releaseDate).build();
    }
}