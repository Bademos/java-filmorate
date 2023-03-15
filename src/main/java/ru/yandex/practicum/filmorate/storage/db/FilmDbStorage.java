package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.StorageAbs;

import java.time.LocalDate;
import java.util.*;

@Repository
@Qualifier("dataBase")
public class FilmDbStorage extends StorageAbs<Film> implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        System.out.println(film.getGenres());
        String sqlQuery = "INSERT INTO films(name,description, duration,releaseDate,ratingID)" +
                "values(?,?,?,?,?)";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa().getId());
        addGenre(film.getId(), film.getGenres());
        return film;
    }

    @Override
    public void delete(Film film) {
        String sqlQuery = "DELETE FROM films where name=?";
        jdbcTemplate.update(sqlQuery, film.getName());
    }

    @Override
    public Film getById(Integer id) {
        String sqlQuery = "SELECT * FROM films where id=? ";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            Film film = filmMap(srs);
            if (!getGenres(id).isEmpty()) {
                film.setGenres(getGenres(film.getId()));
            }
            return film;
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }
    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT * FROM FILMS";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Film> films = new ArrayList<>();
        while (srs.next()) {
            Film film = filmMap(srs);
            setGenres(film);
            films.add(film);
        }
        return films;
    }

    @Override
    public Film update(Film film) {
        getById(film.getId());
        String sqlQuery = "UPDATE films " +
                "SET name=?," +
                "description=?, " +
                "duration=?, " +
                "releaseDate=?," +
                " ratingId=? " +
                "WHERE id=?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getDuration(),
                film.getReleaseDate(),
                film.getMpa().getId(),
                film.getId());
        addGenre(film.getId(), film.getGenres());
        for (Genre genre : getGenres(film.getId())) {
            System.out.println(genre.getId() + " " + genre.getName());
        }
        int filmId = film.getId();
        film.setGenres(getGenres(filmId));
        return getById(film.getId());
    }

    public void addGenre(int filmID, Set<Genre> genres) {
        deleteAllGenresByID(filmID);
        if (genres == null || genres.isEmpty()) {
            return;
        }
        String sqlQuery = "INSERT INTO GENRE_FILM(FILM_ID,GENREID) " +
                "VALUES(?,?)";
        for (Genre genre : genres) {
            jdbcTemplate.update(sqlQuery,
                    filmID, genre.getId());
        }
    }

    public Set<Genre> getGenres(int filmID) {
        Set<Genre> genres = new TreeSet<Genre>(new Comparator<Genre>() {
            public int compare(Genre o1, Genre o2) {
                return o1.getId() - o2.getId();
            }
        });
        String sqlQuery = "SELECT GENREID from GENRE_FILM " +
                "WHERE FILM_ID=? order by GENREID ASC;";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, filmID);
        while (srs.next()) {
            genres.add(new Genre(srs.getInt("genreid")));
        }
        return genres;
    }

    private void setGenres(Film film) {
        List<Genre> genres = new ArrayList<>(getGenres(film.getId()));
        genres.sort(Comparator.comparingInt(ListModel::getId));
        for (Genre genre : genres) {
            film.addGenre(genre);
        }
    }

    public void deleteAllGenresByID(int filmID) {
        String sglQuery = "DELETE FROM GENRE_FILM WHERE film_ID=?;";
        jdbcTemplate.update(sglQuery, filmID);
    }

    public void addLike(int filmID, int userID) {
        String sqlQuery = "INSERT INTO likes(FILMID,USERID) " +
                "VALUES(?,?)";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    public void removeLike(int filmID, int userID) {
        String sqlQuery = "DELETE  likes " +
                "WHERE filmId=? and userID=?";
        jdbcTemplate.update(sqlQuery, filmID, userID);
    }

    public List<Film> getSortedFilms() {
        List<Film> films = new ArrayList<>();
        String sqlQuery = "SELECT Films.* from FILMS " +
                "LEFT JOIN likes on LIKES.FILMID=FILMS.ID " +
                "GROUP BY FILMS.ID " +
                "ORDER BY COUNT(LIKES.FILMID) DESC";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        while (srs.next()) {
            films.add(filmMap(srs));
        }
        return films;
    }

    public Film filmMap(SqlRowSet srs) {
        int id = srs.getInt("id");
        String name = srs.getString("name");
        String description = srs.getString("description");
        int duration = srs.getInt("duration");
        LocalDate releaseDate = srs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        Mpa mpa = new Mpa(srs.getInt("ratingID"));
        Set<Genre> genres = getGenres(id);
        return Film.builder().id(id).name(name).description(description).
                duration(duration).mpa(mpa).genres(genres).releaseDate(releaseDate).build();
    }
}