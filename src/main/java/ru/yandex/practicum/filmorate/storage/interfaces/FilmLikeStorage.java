package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmLikeStorage {
    public void addLike(int filmID,int userID);
    public void removeLike(int filmID,int userID);
    public List<Film> getSortedFilms();
}