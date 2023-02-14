package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    protected final Map<Integer, Film> listOfEntities;
    private final Map<Integer, Set<Integer>> filmsLikes;

    public InMemoryFilmStorage() {

        this.listOfEntities = new HashMap<>();
        this.filmsLikes = new HashMap<>();
    }

    @Override
    public Collection<Film> findAll() {
        log.info(Messages.message(Messages.CURRENT_CONDITION) + listOfEntities.size());
        return listOfEntities.values();
    }

    @Override
    public Film create(Film film) {
        listOfEntities.put(film.getId(), film);
        filmsLikes.put(film.getId(), new HashSet<>());
        log.info(Messages.message(Messages.SUCCESS_ADDED) + film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (listOfEntities.containsKey(film.getId())) {
            listOfEntities.put(film.getId(), film);
            log.info(Messages.message(Messages.SUCCESS_UPDATED) + film);
        } else {
            log.debug(Messages.message(Messages.IS_NOT_IN_LIST));
            throw new NotFoundException(Messages.message(Messages.IS_NOT_IN_LIST));
        }
        return film;
    }

    @Override
    public Film getById(Integer id) {
        Film film;
        if (listOfEntities.containsKey(id)) {
            film = listOfEntities.get(id);
        } else {
            throw new NotFoundException(FilmMessages.
                    filmMessage(FilmMessages.FILM_IS_NOT_IN_LIST));
        }
        return listOfEntities.get(id);
    }

    @Override
    public Map<Integer, Film> getListOfEntities() {
        return listOfEntities;
    }

    @Override
    public List<Film> getSortedFilms() {
        return filmsLikes.entrySet().stream().
                sorted((film1, film2) -> film2.getValue().size() - film1.getValue().size()).
                map(film -> listOfEntities.get(film.getKey())).
                collect(Collectors.toList());
    }

    @Override
    public boolean addLike(Integer filmId, Integer userId) {
        checkFilmInList(filmId);
        filmsLikes.get(filmId).add(userId);
        return true;
    }

    @Override
    public boolean removeLike(Integer filmId, Integer userId) {
        checkFilmInList(filmId);
        filmsLikes.get(filmId).remove(userId);
        return true;
    }

    @Override
    public void checkFilmInList(Integer filmId) {
        if (!listOfEntities.containsKey(filmId) && !filmsLikes.containsKey(filmId)) {
            throw new NotFoundException(FilmMessages.filmMessage(FilmMessages.FILM_IS_NOT_IN_LIST));
        }
    }
}