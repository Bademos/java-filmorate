package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.web.bind.MethodArgumentNotValidException;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    FilmController fc;

    @BeforeEach
    public void start() {
        fc = new FilmController();
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
        assertEquals(Messages.message(Messages.INCORRECT_FORM), ex.getMessage());
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
        assertEquals(Messages.message(Messages.INCORRECT_UPDATE_FORM), ex.getMessage());
    }
}