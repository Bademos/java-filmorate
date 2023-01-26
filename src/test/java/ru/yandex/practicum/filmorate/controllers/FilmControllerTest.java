package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class  FilmControllerTest {

    FilmController fc;
    @BeforeEach
    public void start() {
        fc = new FilmController();
    }

    @Test
    void findAll() {
    }

    @Test
    void createVoidNameTest() {
        Film film = new Film(190,"","impudicus", LocalDate.now().minusYears(14),180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.create(film);
            }
        });
        assertEquals("Некоректно заполнена форма  фильма", ex.getMessage());
    }


    @Test
    void createLongDescriptionTest() {
        String description = "MACBETH\n" +
                "Stay, you imperfect speakers, tell me more:\n" +
                "By Sinel's death I know I am thane of Glamis;\n" +
                "But how of Cawdor? the thane of Cawdor lives,\n" +
                "A prosperous gentleman; and to be king\n" +
                "Stands not within the prospect of belief,\n" +
                "No more than to be Cawdor. Say from whence\n" +
                "You owe this strange intelligence? or why\n" +
                "Upon this blasted heath you stop our way\n" +
                "With such prophetic greeting? Speak, I charge you.\n" +
                "Witches vanish\n" +
                "\n" +
                "BANQUO\n" +
                "The earth hath bubbles, as the water has,\n" +
                "And these are of them. Whither are they vanish'd?\n" +
                "MACBETH\n" +
                "Into the air; and what seem'd corporal melted\n" +
                "As breath into the wind. Would they had stay'd!";

        Film film = new Film(191,"MACBETH",description, LocalDate.now().minusYears(14),180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.create(film);
            }
        });
        assertEquals("Некоректно заполнена форма  фильма", ex.getMessage());

    }
    @Test
    void createOldReleaseTest() {
        Film film = new Film(192,"Movie","impudicus", LocalDate.now().minusYears(200),180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.create(film);
            }
        });
        assertEquals("Некоректно заполнена форма  фильма", ex.getMessage());
    }

    @Test
    void createNegativeDurationTest() {
        Film film = new Film(193,"Movie","impudicus", LocalDate.now().minusYears(20),-180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.create(film);
            }
        });
        assertEquals("Некоректно заполнена форма  фильма", ex.getMessage());
    }

    @Test
    void updateVoidNameTest() {
        Film film = new Film(190,"Movie","impudicus", LocalDate.now().minusYears(14),180);
        fc.create(film);
        Film filmUpd =  new Film(190,"","impudicus", LocalDate.now().minusYears(14),180);
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.update(filmUpd);
            }
        });

        assertEquals("Некоректно заполнена форма", ex.getMessage());
    }

    @Test
    void updateLongDescriptionTest() {
        String description = "MACBETH\n" +
                "Stay, you imperfect speakers, tell me more:\n" +
                "By Sinel's death I know I am thane of Glamis;\n" +
                "But how of Cawdor? the thane of Cawdor lives,\n" +
                "A prosperous gentleman; and to be king\n" +
                "Stands not within the prospect of belief,\n" +
                "No more than to be Cawdor. Say from whence\n" +
                "You owe this strange intelligence? or why\n" +
                "Upon this blasted heath you stop our way\n" +
                "With such prophetic greeting? Speak, I charge you.\n" +
                "Witches vanish\n" +
                "\n" +
                "BANQUO\n" +
                "The earth hath bubbles, as the water has,\n" +
                "And these are of them. Whither are they vanish'd?\n" +
                "MACBETH\n" +
                "Into the air; and what seem'd corporal melted\n" +
                "As breath into the wind. Would they had stay'd!";

        Film film = new Film(191,"MACBETH","Play", LocalDate.now().minusYears(14),180);
        fc.create(film);

        Film filmUpd = new Film(191,"MACBETH",description, LocalDate.now().minusYears(14),180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.update(filmUpd);
            }
        });
        assertEquals("Некоректно заполнена форма", ex.getMessage());
    }

    @Test
    void updateOldReleaseTest() {
        Film film = new Film(192,"Movie","impudicus", LocalDate.now().minusYears(20),180);
        fc.create(film);
        Film filmUpd = new Film(192,"Movie","impudicus", LocalDate.now().minusYears(200),180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.update(filmUpd);
            }
        });
        assertEquals("Некоректно заполнена форма", ex.getMessage());
    }

    @Test
    void updateNegativeDurationTest() {
        Film film = new Film(193,"Movie","impudicus", LocalDate.now().minusYears(20),180);
        fc.create(film);
        Film filmUpd = new Film(193,"Movie","impudicus", LocalDate.now().minusYears(20),-180);

        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                fc.update(filmUpd);
            }
        });
        assertEquals("Некоректно заполнена форма", ex.getMessage());
    }
}