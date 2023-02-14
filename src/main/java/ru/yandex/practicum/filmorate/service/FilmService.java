
package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    final private InMemoryFilmStorage filmStorage;
    final private InMemoryUserStorage userStorage;
    final private IdGenerator idGenerator;
    private static final LocalDate LIMIT_DATE = LocalDate.from(LocalDateTime.of(1895, 12, 28, 0, 0));
    private static final int LIMIT_LENGTH_OF_DESCRIPTION = 200;

    public FilmService(InMemoryFilmStorage filmStorage, InMemoryUserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
        this.idGenerator = new IdGenerator();
    }

    public Film create(Film film) {
        validate(film, FilmMessages.filmMessage(FilmMessages.INCORRECT_FILM_FORM));
        film.setId(idGenerator.getId());

        return filmStorage.create(film);
    }

    public Film update(Film film) {
        validate(film, FilmMessages.filmMessage(FilmMessages.INCORRECT_UPDATE_FILM_FORM));
        return filmStorage.update(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.findAll();
    }

    public Film getById(Integer id) {
        return filmStorage.getById(id);
    }

    public void addLike(Integer filmId, Integer userId) {
        checkLikeAction(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    public void removeLike(Integer filmId, Integer userId) {
        checkLikeAction(filmId, userId);
        filmStorage.remove(filmId, userId);
    }

    public void checkLikeAction(Integer filmId, Integer userId) {
        if (!filmStorage.getListOfEntities().containsKey(filmId)) {
            throw new ValidationException(FilmMessages.filmMessage(FilmMessages.FILM_IS_NOT_IN_LIST));
        }
        if (!userStorage.getListOfEntities().containsKey(userId)) {
            throw new ValidationException(UserMessages.userMessage(UserMessages.USER_IS_NOT_IN_LIST));
        }
    }

    public List<Film> getCountOfSortedFilms(Integer count) {
        if (filmStorage.getSortedFilms().size() <= count) {
            return filmStorage.getSortedFilms();
        }
        return filmStorage.getSortedFilms().stream().limit(count).collect(Collectors.toList());
    }

    public boolean validation(Film film) {
        return !(film.getDescription().length() > LIMIT_LENGTH_OF_DESCRIPTION ||
                film.getReleaseDate().isBefore(LIMIT_DATE));
    }

    public void validate(Film film, String message) {
        if (!validation(film)) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }
}