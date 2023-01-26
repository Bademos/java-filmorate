package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController {
    private final Map<Integer, Film> films = new HashMap<>();
    private int id = 0;

    @GetMapping
    public Collection<Film> findAll() {
        log.info(filmMessage(FilmMessages.CURRENT_CONDITION) + films.size());
        return films.values();
    }

    @PostMapping
    public Film create(@RequestBody Film film) {
        if (!filmValidation(film)) {
            log.debug(filmMessage(FilmMessages.INCORRECT_FILM_FORM));
            throw new ValidationException(filmMessage(FilmMessages.INCORRECT_FILM_FORM));
        }
        if ((film.getId() <= id)) {
            film.setId(setId());
        }
        films.put(film.getId(), film);
        log.info(filmMessage(FilmMessages.FILM_SUCCESS_ADDED) + film);
        return film;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        if (!filmValidation(film)) {
            log.debug(filmMessage(FilmMessages.INCORRECT_UPDATE_FILM_FORM));

            throw new ValidationException(filmMessage(FilmMessages.INCORRECT_UPDATE_FILM_FORM));
        }
        if (films.containsKey(film.getId())) {

            films.put(film.getId(), film);
            log.info(filmMessage(FilmMessages.FILM_SUCCESS_UPDATED) + film);

        } else {
            log.debug(filmMessage(FilmMessages.FILM_IS_NOT_IN_LIST));
            throw new ValidationException(filmMessage(FilmMessages.FILM_IS_NOT_IN_LIST));
        }
        return film;
    }

    public int setId() {
        id = 1;
        return id;
    }

    public boolean filmValidation(Film film) {
        return !(film.getName() == null || film.getName().isBlank() ||
                film.getDescription().length() > 200 ||
                film.getReleaseDate().isBefore(LocalDate.from(LocalDateTime.of(1895, 12, 28, 0, 0))) ||
                film.getDuration() < 0);
    }

    public String filmMessage(FilmMessages fm){
        switch (fm){
            case CURRENT_CONDITION:return "Текущее количество фильмов: ";
            case FILM_IS_NOT_IN_LIST:return "Фильма нет в списке";
            case INCORRECT_FILM_FORM:return "Некоректно заполнена форма  фильма";
            case INCORRECT_UPDATE_FILM_FORM:return "Некоректно заполнена форма";
            case FILM_SUCCESS_ADDED:return "Успешно добавлен фильм: ";
            case FILM_SUCCESS_UPDATED:return "Успешно добавлен обновлен: ";
            default:return "Ошибка неизвестного рода";
        }
    }
}

enum FilmMessages {
    INCORRECT_FILM_FORM,
    INCORRECT_UPDATE_FILM_FORM,
    FILM_IS_NOT_IN_LIST,
    CURRENT_CONDITION,
    FILM_SUCCESS_ADDED,
    FILM_SUCCESS_UPDATED
}