

/*
package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.memory.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController uc;

    @BeforeEach

    public void start() {
        InMemoryUserStorage userStorage = new InMemoryUserStorage();
        UserService userService = new UserService(userStorage);
        uc = new UserController( userService);
    }

    @Test
    void createVoidNameTest() {
        int id = 1;
        String login = "bademus";
        User user = new User(id, "id@ram.ru", login, null, LocalDate.now().minusYears(33));
        uc.create(user);
        Assertions.assertEquals(login, uc.getUsers().get(id).getName());
    }

    @Test
    void createBackToFutureBirthTest() {
        User user = new User(10, "id@ram.ru", "bademus", "vadique", LocalDate.now().plusYears(33));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.create(user);
            }
        });
        assertEquals(UserMessages.userMessage(UserMessages.INCORRECT_USER_FORM), ex.getMessage());
    }

    @Test
    void updateVoidNameTest() {
        User usr = new User(1, "id@ram.ru", "bademus", "", LocalDate.now().minusYears(33));
        uc.create(usr);
        int id = 1;
        String login = "bademus";
        User user = new User(id, "id@ram.ru", login, null, LocalDate.now().minusYears(33));
        uc.update(user);
        System.out.println(uc.getUsers());
        Assertions.assertEquals(login, uc.getUsers().get(id).getName());
    }

    @Test
    void updateBackToFutureBirthTest() {
        User usr = new User(10, "id@ram.ru", "bademus", "vadique", LocalDate.now().minusYears(33));
        uc.create(usr);
        User user = new User(10, "id@ram.ru", "bademus", "vadique", LocalDate.now().plusYears(33));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.update(user);
            }
        });
        assertEquals(UserMessages.userMessage(UserMessages.INCORRECT_UPDATE_USER_FORM), ex.getMessage());
    }
}
*/