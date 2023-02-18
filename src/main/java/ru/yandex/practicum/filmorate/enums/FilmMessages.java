package ru.yandex.practicum.filmorate.enums;

import com.sun.net.httpserver.Authenticator;

public enum FilmMessages {
    INCORRECT_FILM_FORM,
    INCORRECT_UPDATE_FILM_FORM,
    FILM_IS_NOT_IN_LIST,
    CURRENT_CONDITION,
    FILM_SUCCESS_ADDED,
    FILM_SUCCESS_UPDATED,
    LIKE_SUCCESS_ADDED,
    LIKE_SUCCESS_DELETED,
    POPPULAR_FILMS_REQUEST;

    public static String filmMessage(FilmMessages fm) {
        switch (fm) {
            case CURRENT_CONDITION:
                return "Текущее количество фильмов: ";
            case FILM_IS_NOT_IN_LIST:
                return "Фильма нет в списке";
            case INCORRECT_FILM_FORM:
                return "Некоректно заполнена форма  фильма";
            case INCORRECT_UPDATE_FILM_FORM:
                return "Некоректно заполнена форма обновления фильма";
            case FILM_SUCCESS_ADDED:
                return "POST: Успешно добавлен фильм: ";
            case FILM_SUCCESS_UPDATED:
                return "PUT: Успешно обновлен фильм: ";
            case LIKE_SUCCESS_ADDED:
                return "PUT: Успешно добавлен лайк";
            case LIKE_SUCCESS_DELETED:
                return "DELETE: Успешно удален лайк";
            case POPPULAR_FILMS_REQUEST:
                return "GET:Запрошен список популярных фильмов";
            default:
                return "Ошибка неизвестного рода";
        }
    }
}