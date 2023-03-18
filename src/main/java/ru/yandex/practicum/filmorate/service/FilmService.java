
package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    final private UserStorage userStorage;
    final private FilmStorage storage;
    final protected IdGenerator idGenerator;
    private static final LocalDate LIMIT_DATE = LocalDate.from(LocalDateTime.of(1895, 12, 28, 0, 0));
    private static final int LIMIT_LENGTH_OF_DESCRIPTION = 200;

    @Autowired
    public FilmService(@Qualifier("dataBase") FilmStorage filmStorage, @Qualifier("dataBase") UserStorage userStorage) {
        this.userStorage = userStorage;
        this.storage = filmStorage;
        this.idGenerator = new IdGenerator();
    }
    public Film create(Film film) {
        validate(film, FilmMessages.filmMessage(FilmMessages.INCORRECT_FILM_FORM));
        film.setId(idGenerator.getId());
        Film result = storage.create(film);
        log.info(FilmMessages.filmMessage(FilmMessages.FILM_SUCCESS_ADDED) + film);
        return result;
    }

    public void addGenres(Film film){
        storage.addGenre(film.getId(),film.getGenres());
    }

    public Film update(Film film) {
        validate(film, FilmMessages.filmMessage(FilmMessages.INCORRECT_UPDATE_FILM_FORM));
        Film result = storage.update(film);
        log.info(FilmMessages.filmMessage(FilmMessages.FILM_SUCCESS_UPDATED) + film);
        return result;
    }

    public void addLike(Integer filmId, Integer userId) {
        checkLikeAction(filmId, userId);
        storage.addLike(filmId,userId);
        log.info(FilmMessages.filmMessage(FilmMessages.LIKE_SUCCESS_ADDED));
    }

    public Film getById(Integer id) {
        log.info(Messages.message(Messages.ID_REQUEST) + id);
        return storage.getById(id);
    }

        public void removeLike(Integer filmId, Integer userId) {
        checkLikeAction(filmId, userId);
        storage.removeLike(filmId,userId);
        log.info(FilmMessages.filmMessage(FilmMessages.LIKE_SUCCESS_DELETED));
    }

    private void checkLikeAction(Integer filmId, Integer userId) {
       //storage.getById(filmId);
       //userStorage.getById(userId);
        storage.checkLikeAction(filmId, userId);
    }

    public List<Film> getCountOfSortedFilms(Integer count) {
        List<Film> result = getSortedFilms().stream().limit(count).collect(Collectors.toList());
        log.info(FilmMessages.filmMessage(FilmMessages.POPPULAR_FILMS_REQUEST));
        return result;
    }

    public List<Film> getSortedFilms() {
        return storage.getSortedFilms();
    }

    public Collection<Film> findAll() {
        log.info(FilmMessages.filmMessage(FilmMessages.CURRENT_CONDITION) + storage.getListOfEntities().size());
        return storage.findAll();
    }

    protected void validate(Film film, String message) {
        if (film.getDescription().length() > LIMIT_LENGTH_OF_DESCRIPTION ||
                film.getReleaseDate().isBefore(LIMIT_DATE)) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }
}