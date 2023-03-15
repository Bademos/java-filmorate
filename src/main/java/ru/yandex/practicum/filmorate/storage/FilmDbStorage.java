package ru.yandex.practicum.filmorate.storage;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Qualifier("dataBase")
public class FilmDbStorage extends StorageAbs<Film> implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        //super.create(film);
        System.out.println(film.getGenres());
        String sqlQuery = "INSERT INTO films(name,description, duration,releaseDate,ratingID)" +
                "values(?,?,?,?,?)";
        jdbcTemplate.update(sqlQuery, film.getName(), film.getDescription(),
                film.getDuration(), film.getReleaseDate(), film.getMpa().getId());

            addGenre(film.getId(), film.getGenres());
            //film.removeAllGenres();
            //setGenres(film);
        for(Genre genre: film.getGenres()){
            System.out.println(genre.getId());}

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
            if(!getGenres(id).isEmpty()){
                for(Genre genre:getGenres(id)){
                    film.addGenre(genre);
                }
            }

            return film;
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        Integer id = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        Integer duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        Film film = Film.builder().id(id).name(name).description(description).
                duration(duration).releaseDate(releaseDate).build();
        return Film.builder().id(id).name(name).description(description).
                duration(duration).releaseDate(releaseDate).build();
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT * FROM FILMS";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        List<Film> films = new ArrayList<>();
        while (srs.next()) {
            Film film =filmMap(srs);
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
        film.removeAllGenres();

        for (Genre genre :getById(film.getId()).getGenres()){
            film.addGenre(genre);
        }


        return film;
    }

        //film.removeAllGenres();
        //setGenres(film);
        /*for (Genre genre :film.getGenres()){
            System.out.println(genre.getId()+ "ccooooo");
        }*/
        //return film;
    //}

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
        Set<Integer> genresID = new HashSet<>();
        //Set<Genre> genres = new HashSet<>();
        Set<Genre> genres= new TreeSet<Genre>(new Comparator<Genre>() {
            public int compare(Genre o1, Genre o2) {
                return o1.getId()-o2.getId();
            }
        });
        String sqlQuery = "SELECT GENREID from GENRE_FILM " +
                "WHERE FILM_ID=? order by GENREID ASC;";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, filmID);
        while (srs.next()) {
            genresID.add(srs.getInt("genreid"));
        }
        System.out.println("Size" + genresID.size());

        for (Integer id : genresID){
            genres.add(new Genre(id));
        }

        System.out.println(genresID);
        for (Genre gn : genres){
            System.out.println(gn.getId());;
        }
        return genres;
    }

        private void setGenres(Film film){
            List<Genre> genres = new ArrayList<>(getGenres(film.getId()));
            genres.sort(Comparator.comparingInt(ListModel::getId));
            for (Genre genre:genres){
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

    public static Film filmMap(SqlRowSet srs) {
        int id = srs.getInt("id");
        String name = srs.getString("name");
        String description = srs.getString("description");
        int duration = srs.getInt("duration");
        LocalDate releaseDate = srs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        Mpa mpa = new Mpa(srs.getInt("ratingID"));
        //Set<Genre> genres = getGenres(id);
        System.out.println("oops? " + mpa);
        return Film.builder().id(id).name(name).description(description).
                duration(duration).mpa(mpa).releaseDate(releaseDate).build();
    }
}