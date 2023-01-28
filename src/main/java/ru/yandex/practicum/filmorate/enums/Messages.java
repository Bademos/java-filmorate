package ru.yandex.practicum.filmorate.enums;

public  enum Messages {
    INCORRECT_FORM,
    INCORRECT_UPDATE_FORM,
    IS_NOT_IN_LIST,
    CURRENT_CONDITION,
    SUCCESS_ADDED,
    SUCCESS_UPDATED;

    public static String message(Messages m){
        switch (m) {
            case CURRENT_CONDITION:
                return "Текущее количество объектов: ";
            case IS_NOT_IN_LIST:
                return "Объекта нет в списке";
            case INCORRECT_FORM:
                return "Некоректно заполнена форма  объекта";
            case INCORRECT_UPDATE_FORM:
                return "Некоректно заполнена форма обновления объекта";
            case SUCCESS_ADDED:
                return "Успешно добавлен объект: ";
            case SUCCESS_UPDATED:
                return "Успешно обновлен объект: ";
            default:
                return "Ошибка неизвестного рода";
        }
    }
}
