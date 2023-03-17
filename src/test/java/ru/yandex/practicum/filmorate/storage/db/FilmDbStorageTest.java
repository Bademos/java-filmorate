package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.HashSet;

@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getByIdTest(){
        Film film = Film.builder().id(1).name("Godfather").
                description("boing movie").
                releaseDate(LocalDate.now().minusYears(40)).
                duration(130).genres(new HashSet<>()).mpa(mpaDbStorage.getMpaById(1)).build();
        filmDbStorage.create(film);
        System.out.println(film.getId());
        System.out.println(filmDbStorage.getById(1));
        Assertions.assertEquals(filmDbStorage.getById(1),film);
    }
}
