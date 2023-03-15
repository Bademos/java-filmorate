package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;

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
    public void addGenre(int filmID, Set<Genre> genres) {

    }

    @Override
    public Set<Genre> getGenres(int filmID) {
        return null;
    }
}