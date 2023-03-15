package ru.yandex.practicum.filmorate.storage.db;

import lombok.RequiredArgsConstructor;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userStorage;

    @Test
    public void testFindUserById() {
        User user = User.builder().id(1).name("Vacya").
                login("Pukin").
                email("zoo@shop.com").
                birthday(LocalDate.now().minusYears(34)).build();
        userStorage.create(user);
        User userOptional = userStorage.getById(1);
        Assertions.assertEquals(userOptional.getName(), "Vacya");
    }

    @Test
    public void testFindUserByWrongId(){
        User userOptional = userStorage.getById(1);
        Assertions.assertEquals(userOptional.getName(), "Vacya");
    }
}