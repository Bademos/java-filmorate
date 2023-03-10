package ru.yandex.practicum.filmorate.enums;

public enum FilmControllerMessages {
        FILM_CREATE_RESPONSE,
        FILM_UPDATE_RESPONSE,
        FILM_ADD_LIKE,
        FILM_REMOVE_LIKE,
        FILM_GET_POPULAR,
        USER_GET_COMMON_FRIENDS;

        public static String    FilmControllerMessage(FilmControllerMessages FSM){
            switch (FSM){
                case FILM_ADD_LIKE:
                    return "Получен запрос на добаление лайка";
                case FILM_GET_POPULAR:
                    return "Получен запрос на вывод списка популярных фильмов";
                case FILM_CREATE_RESPONSE:
                    return "Получен запрос на добавление фильма";
                case FILM_REMOVE_LIKE:
                    return "Получен запрос на удаление лайка";
                case FILM_UPDATE_RESPONSE:
                    return "Получен запрос на обновление данных фидьма";
                default:
                    return "Sorry, a kind of error";
            }
        }
}
