
package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService extends ServiceAbs<Film> {
    final private InMemoryUserStorage userStorage;
    private static final LocalDate LIMIT_DATE = LocalDate.from(LocalDateTime.of(1895, 12, 28, 0, 0));
    private static final int LIMIT_LENGTH_OF_DESCRIPTION = 200;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        super(filmStorage);
        this.userStorage = userStorage;
    }

    @Override
    public Film create(Film film) {
        validate(film, FilmMessages.filmMessage(FilmMessages.INCORRECT_FILM_FORM));
        Film result = super.create(film);
        log.info(FilmMessages.filmMessage(FilmMessages.FILM_SUCCESS_ADDED) + film);
        return result;
    }

    @Override
    public Film update(Film film) {
        validate(film, FilmMessages.filmMessage(FilmMessages.INCORRECT_UPDATE_FILM_FORM));
        Film result = super.update(film);
        log.info(FilmMessages.filmMessage(FilmMessages.FILM_SUCCESS_UPDATED) + film);
        return result;
    }

    public void addLike(Integer filmId, Integer userId) {
        checkLikeAction(filmId, userId);
        getById(filmId).addLike(userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        checkLikeAction(filmId, userId);
        getById(filmId).deleteLike(userId);
    }

    public void checkLikeAction(Integer filmId, Integer userId) {
        if (!storage.getListOfEntities().containsKey(filmId)) {
            throw new NotFoundException(FilmMessages.filmMessage(FilmMessages.FILM_IS_NOT_IN_LIST));
        }
        if (!userStorage.getListOfEntities().containsKey(userId)) {
            throw new NotFoundException(UserMessages.userMessage(UserMessages.USER_IS_NOT_IN_LIST));
        }
    }

    public List<Film> getCountOfSortedFilms(Integer count) {
        return getSortedFilms().stream().limit(count).collect(Collectors.toList());
    }

    public List<Film> getSortedFilms() {
        return storage.getListOfEntities().values().stream().
                sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size()).
                collect(Collectors.toList());
    }

    @Override
    protected boolean validation(Film film) {
        return !(film.getDescription().length() > LIMIT_LENGTH_OF_DESCRIPTION ||
                film.getReleaseDate().isBefore(LIMIT_DATE));
    }
}