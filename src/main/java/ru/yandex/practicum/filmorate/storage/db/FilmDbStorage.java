package ru.yandex.practicum.filmorate.storage.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.StorageAbs;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Repository
@Qualifier("dataBase")
public class FilmDbStorage extends StorageAbs<Film> implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
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
        String sqlQuery = "SELECT * from FILMS " +
                "join RATING on FILMS.RATINGID = RATING.RATEID " +
                "where id =?; ";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, id);
        if (srs.next()) {
            return filmMap(srs);
        } else {
            throw new NotFoundException("Фильм не найден");
        }
    }

    @Override
    public Collection<Film> findAll() {
        String sqlQuery = "SELECT * FROM FILMS " +
                "JOIN RATING on FILMS.RATINGID = RATING.RATEID " +
                "LEFT JOIN  GENRE_FILM on GENRE_FILM.film_id = films.id " +
                "LEFT JOIN GENRE on Genre.GENREID = GENRE_FILM.GENREID;";
        List<Film> films = jdbcTemplate.query(sqlQuery, this::makeFilm);
        return addGenreForList(films);
    }

    private List<Film> addGenreForList(List<Film> films) {
        Map<Integer, Film> filmsMap = films.stream()
                .collect(Collectors.toMap(Film::getId, film -> film));
        String inSql = String.join(", ", Collections.nCopies(filmsMap.size(), "?"));
        final String sqlQuery = "select * " +
                "from GENRE_FILM " +
                "left outer join GENRE on GENRE_FILM.GENREID = GENRE.GENREID " +
                "where GENRE_FILM.FILM_ID in (" + inSql + ") " +
                "order by GENRE_FILM.GENREID";
        jdbcTemplate.query(sqlQuery, (rs) -> {
            filmsMap.get(rs.getInt("FILM_ID")).
                    addGenre(new Genre(rs.getInt("genreid"),
                            rs.getString("genre")));
        }, filmsMap.keySet().toArray());
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
        int filmId = film.getId();
        film.setGenres(getGenres(filmId));
        return getById(filmId);
    }

    public void addGenre(int filmID, Set<Genre> genres) {
        deleteAllGenresByID(filmID);
        if (genres == null || genres.isEmpty()) {
            return;
        }
        String sqlQuery = "INSERT INTO GENRE_FILM(FILM_ID,GENREID) " +
                "VALUES(?,?)";
        List<Genre> genresList = new ArrayList<>(genres);
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

    public Set<Genre> getGenres(int filmID) {
        Comparator<Genre> compId = Comparator.comparing(Genre::getId);
        Set<Genre> genres = new TreeSet<Genre>(compId);
        String sqlQuery = "SELECT genre_film.GENREID, genre.genre as gnr from GENRE_FILM " +
                "JOIN genre on genre.genreid = genre_film.genreid " +
                "WHERE FILM_ID=? order by GENREID ASC;";
        genres.addAll(jdbcTemplate.query(sqlQuery, this::makeGenre, filmID));
        return genres;
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
        String sqlQuery = "SELECT * from FILMS " +
                "LEFT JOIN likes on LIKES.FILMID=FILMS.ID " +
                "JOIN rating on films.ratingid=rating.rateid " +
                "GROUP BY FILMS.ID " +
                "ORDER BY COUNT(LIKES.FILMID) DESC";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery);
        return jdbcTemplate.query(sqlQuery, this::makeFilm);
    }

    @Override
    public void checkLikeAction(Integer filmId, Integer userId) {
        getById(filmId);
        String sqlQuery = "SELECT * FROM users where id=?";
        SqlRowSet srs = jdbcTemplate.queryForRowSet(sqlQuery, userId);
        if (srs.next()) {
            log.info("Проверка перед лайком пройдена");
        } else {
            throw new NotFoundException("Пользователь с данным id не найден");
        }
    }

    private Genre makeGenre(ResultSet rs, int id) throws SQLException {
        int genreId = rs.getInt("genreId");
        String genreName = rs.getString("genre");
        return new Genre(genreId, genreName);
    }

    private Film makeFilm(ResultSet rs, int id) throws SQLException {
        int filmId = rs.getInt("id");
        String name = rs.getString("name");
        String description = rs.getString("description");
        int duration = rs.getInt("duration");
        LocalDate releaseDate = rs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        int mpaId = rs.getInt("ratingID");
        String mpaName = rs.getString("rate");
        Mpa mpa = new Mpa(mpaId, mpaName);
        Set<Genre> genres = new HashSet<>();
        return Film.builder().id(filmId).name(name).description(description).
                duration(duration).genres(genres).mpa(mpa).releaseDate(releaseDate).build();
    }

    public Film filmMap(SqlRowSet srs) {
        int id = srs.getInt("id");
        String name = srs.getString("name");
        String description = srs.getString("description");
        int duration = srs.getInt("duration");
        LocalDate releaseDate = srs.getTimestamp("releaseDate").toLocalDateTime().toLocalDate();
        int mpaId = srs.getInt("ratingID");
        String mpaName = srs.getString("rate");
        Mpa mpa = new Mpa(mpaId, mpaName);
        Set<Genre> genres = getGenres(id);
        return Film.builder().id(id).name(name).description(description).
                duration(duration).mpa(mpa).genres(genres).releaseDate(releaseDate).build();
    }
}