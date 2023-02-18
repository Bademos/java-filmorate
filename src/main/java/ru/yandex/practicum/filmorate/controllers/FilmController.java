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
        return filmService.findAll();
    }

    @PostMapping
    @Override
    public Film create(@Valid @RequestBody Film film) {
        filmService.create(film);
        return film;
    }

    @PutMapping
    @Override
    public Film update(@Valid @RequestBody Film film) {
        filmService.update(film);
        return film;
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Integer id, @PathVariable Integer userId) {
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopular(@RequestParam(defaultValue = "10") Integer count) {
        log.info(FilmMessages.filmMessage(FilmMessages.POPPULAR_FILMS_REQUEST));
        return filmService.getCountOfSortedFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable Integer id) {
        return filmService.getById(id);
    }
}