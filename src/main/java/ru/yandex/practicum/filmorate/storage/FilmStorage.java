package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage extends Storage<Film> {
    public List<Film> getSortedFilms();

    public boolean addLike(Integer filmId, Integer userId);

    public boolean removeLike(Integer filmId, Integer userId);

    public void checkFilmInList(Integer filmId);
}
