
/*
package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController fc;

    @BeforeEach
    public void start() {
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
        FilmService filmService = new FilmService(filmStorage,userStorage, idGenerator);
        fc = new FilmController(filmService);
    }

    @Test
    void createOldReleaseTest() {
        Film film = new Film(192, "Movie", "impudicus", LocalDate.now().minusYears(200), 180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.create(film);
            }
        });
        assertEquals(FilmMessages.filmMessage(FilmMessages.INCORRECT_FILM_FORM), ex.getMessage());
    }

    @Test
    void updateOldReleaseTest() {
        Film film = new Film(192, "Movie", "impudicus", LocalDate.now().minusYears(20), 180);
        fc.create(film);
        Film filmUpd = new Film(192, "Movie", "impudicus", LocalDate.now().minusYears(200), 180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.update(filmUpd);
            }
        });
        assertEquals(FilmMessages.filmMessage(FilmMessages.INCORRECT_UPDATE_FILM_FORM), ex.getMessage());
    }
}
*/
