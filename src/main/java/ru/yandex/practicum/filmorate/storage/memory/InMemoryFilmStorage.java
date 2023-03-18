package ru.yandex.practicum.filmorate.storage.memory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.StorageAbs;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@Qualifier("memory")
public class InMemoryFilmStorage extends StorageAbs<Film> implements FilmStorage {
    @Override
    public void addLike(int filmID, int userID) {
        getById(filmID).addLike(userID);
    }

    @Override
    public void removeLike(int filmID, int userID) {
        getById(filmID).deleteLike(userID);
    }

    @Override
    public List<Film> getSortedFilms() {
        return getListOfEntities().values().stream().
                sorted((film1, film2) -> film2.getLikes().size() - film1.getLikes().size()).
                collect(Collectors.toList());
    }

    @Override
    public void checkLikeAction(Integer filmId, Integer userId) {
    }

    @Override
    public void addGenre(int filmID, Set<Genre> genres) {
        getById(filmID).setGenres(genres);
    }

    @Override
    public Set<Genre> getGenres(int filmID) {
        return getById(filmID).getGenres();
    }
}