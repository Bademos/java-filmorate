package ru.yandex.practicum.filmorate.enums;

public enum FilmMessages {
    INCORRECT_FILM_FORM,
    INCORRECT_UPDATE_FILM_FORM,
    FILM_IS_NOT_IN_LIST,
    CURRENT_CONDITION,
    FILM_SUCCESS_ADDED,
    FILM_SUCCESS_UPDATED;

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
                return "Успешно добавлен фильм: ";
            case FILM_SUCCESS_UPDATED:
                return "Успешно обновлен фильм: ";
            default:
                return "Ошибка неизвестного рода";
        }
    }
}