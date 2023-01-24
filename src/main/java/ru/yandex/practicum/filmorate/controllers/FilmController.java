package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
public class FilmController {
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);
    private Map<Integer, Film> films = new HashMap<>();
    int id = 0;

    @GetMapping("/films")
    public Collection<Film> findAll() {
        log.info("Текущее количество фильмов: " + films.size());
        return films.values();
    }

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) {
        if (!filmValidation(film)) {
            log.info("Некоректно заполнена форма  фильма");
            throw new ValidationException("Некоректно заполнена форма  фильма");
        }
        if ((film.getId() <= id)) {
            film.setId(setId());
        }
        films.put(film.getId(), film);
        log.info("Успешно добавлен фильм: " + film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) {
        if (!filmValidation(film)) {
            log.info("Некоректно заполнена форма  фильма");

            throw new ValidationException("Некоректно заполнена форма");
        }
        if (films.containsKey(film.getId())) {

            films.put(film.getId(), film);
            log.info("Успешно добавлен фильм: " + film);

        } else {
            log.info("Фильма нет в списке");
            throw new ValidationException("Фильма нет в списке");
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
}