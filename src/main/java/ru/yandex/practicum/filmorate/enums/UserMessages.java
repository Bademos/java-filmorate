package ru.yandex.practicum.filmorate.enums;

public enum UserMessages {
    INCORRECT_USER_FORM,
    INCORRECT_UPDATE_USER_FORM,
    USER_IS_NOT_IN_LIST,
    CURRENT_USERS_CONDITION,
    USER_SUCCESS_ADDED,
    USER_SUCCESS_UPDATED,
    ADD_USER_FRIEND,
    DELETE_USER_FRIEND;

    public static String userMessage(UserMessages um) {
        switch (um) {
            case CURRENT_USERS_CONDITION:
                return "Текущее количество пользоваелей: ";
            case USER_IS_NOT_IN_LIST:
                return "Такого пользователя нет в списке";
            case INCORRECT_USER_FORM:
                return "Некоректно заполнена форма регистрации учетной записи";
            case INCORRECT_UPDATE_USER_FORM:
                return "Некоректно заполнена форма обновления учетной записи";
            case USER_SUCCESS_ADDED:
                return "Успешно добавлен пользователь: ";
            case USER_SUCCESS_UPDATED:
                return "Успешно обновлена учетная запись пользователя: ";
            case ADD_USER_FRIEND:
                return "Новый друг успешно добавлен";
            case DELETE_USER_FRIEND:
                return "Удален друг";
            default:
                return "Ошибка неизвестного рода";
        }
    }
}