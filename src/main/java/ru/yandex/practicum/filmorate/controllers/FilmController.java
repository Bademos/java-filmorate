package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.*;

import javax.validation.Valid;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/films")
public class FilmController extends Controller<Film> {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    @Override
    public Collection<Film> findAll() {
        log.info(FilmMessages.filmMessage(FilmMessages.CURRENT_CONDITION) + listOfEntities.size());
        return filmService.findAll();
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        filmService.create(film);
        log.info(FilmMessages.filmMessage(FilmMessages.FILM_SUCCESS_ADDED) + film);
        return film;
    }

    @PutMapping
    @Override
    public Film update(@Valid @RequestBody Film film) {
        filmService.update(film);
        log.info(FilmMessages.filmMessage(FilmMessages.FILM_SUCCESS_UPDATED));
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
        log.info(FilmMessages.filmMessage(FilmMessages.LIKE_SUCCESS_ADDED));
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
        log.info(FilmMessages.filmMessage(FilmMessages.LIKE_SUCCESS_DELETED));
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info(FilmMessages.filmMessage(FilmMessages.POPPULAR_FILMS_REQUEST));
        return filmService.getCountOfSortedFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        log.info("Запрос на фильм с id:" + id);
        return filmService.getById(id);
    }
}