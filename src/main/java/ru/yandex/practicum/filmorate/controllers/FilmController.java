package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film>{
    private final Map<Integer, Film> films = getListOfEntities();
    private final LocalDate LIMIT_DATE = LocalDate.from(LocalDateTime.of(1895, 12, 28, 0, 0));
    private final int LIMIT_LENGTH_OF_DESCRIPTION = 200;

    @GetMapping
    @Override
    public Collection<Film> findAll() {
        log.info(FilmMessages.filmMessage(FilmMessages.CURRENT_CONDITION) + films.size());
        return films.values();
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        validate(film,FilmMessages.filmMessage(FilmMessages.INCORRECT_FILM_FORM));
        film.setId(setId());
        films.put(film.getId(), film);
        log.info(FilmMessages.filmMessage(FilmMessages.FILM_SUCCESS_ADDED) + film);
        return film;
    }

    @PutMapping
    @Override
    public Film update(@Valid @RequestBody Film film) {
        validate(film,FilmMessages.filmMessage(FilmMessages.INCORRECT_UPDATE_FILM_FORM));
        if (films.containsKey(film.getId())) {
            films.put(film.getId(), film);
            log.info(FilmMessages.filmMessage(FilmMessages.FILM_SUCCESS_UPDATED) + film);
        } else {
            log.debug(FilmMessages.filmMessage(FilmMessages.FILM_IS_NOT_IN_LIST));
            throw new ValidationException(FilmMessages.filmMessage(FilmMessages.FILM_IS_NOT_IN_LIST));
        }
        return film;
    }

    @Override
    public boolean validation(Film film) {
        return  !(film.getName() == null || film.getName().isBlank() ||
                film.getDescription().length() > LIMIT_LENGTH_OF_DESCRIPTION ||
                film.getReleaseDate().isBefore(LIMIT_DATE)||
                film.getDuration() < 0);
    }
}