package ru.yandex.practicum.filmorate.enums;

public enum Genres {
    COMEDY,
    THRILLER,
    DRAMA,
    DOCUMENTARY,
    CARTOON,
    ACTION;

    public String getMessage(Genres genre){
        switch (genre){
            case DRAMA:
                return "Драма";
            case ACTION:
                return "Боевик";
            case COMEDY:
                return "Комедия";
            case CARTOON:
                return "Мультфильм";
            case THRILLER:
                return "Триллер";
            case DOCUMENTARY:
                return "Документальный";
            default:
                return "Неизвестный";
        }
    }
}
