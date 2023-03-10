package ru.yandex.practicum.filmorate.enums;

public enum UserControllerMessages {
    USER_CREATE_RESPONSE,
    USER_UPDATE_RESPONSE,
    USER_ADD_FRIENDS,
    USER_REMOVE_FRIEND,
    USER_ALL_FRIENDS,
    USER_GET_COMMON_FRIENDS;

    public static String UserControllerMessage(UserControllerMessages USM){
        switch (USM){
            case USER_ADD_FRIENDS:
                return "Получен запрос на добаление в друзья";
            case USER_ALL_FRIENDS:
                return "Получен запрос на вывод списка всех друзей";
            case USER_REMOVE_FRIEND:
                return "Получен запрос на удаление из мписка друзей";
            case USER_CREATE_RESPONSE:
                return "Получен запрос на создание нового пользователя";
            case USER_UPDATE_RESPONSE:
                return "Получен запрос на обновление данных пользрователя";
            case USER_GET_COMMON_FRIENDS:
                return "Получен запрос на вывод списка общих друзей";
            default:
                return "Sorry, a kind of error";
        }
    }
}