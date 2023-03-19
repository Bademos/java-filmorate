package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface FilmStorage extends Storage<Film>,FilmLikeStorage{
    public void addGenre(int filmID, Set<Genre> genres);
    public Set<Genre> getGenres(int filmID);
}
