package ru.yandex.practicum.filmorate.controllers;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    UserController uc;
    @BeforeEach
    public void start() {
     uc = new UserController();

    }

    @Test
    void createEmailTest() {
        User user = new User(10,"idram.ru","bademus","vadique", LocalDate.now().minusYears(33));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.create(user);
            }
        });
        assertEquals("Некоректно заполнена форма регистрации учетной записи", ex.getMessage());

        User user2 = new User(10,"","bademus","vadique", LocalDate.now().minusYears(33));
         ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.create(user2);
            }
        });
        assertEquals("Некоректно заполнена форма регистрации учетной записи", ex.getMessage());
    }

    @Test
    void createLoginTest() {
        User user = new User(10,"id@ram.ru"," ","vadique", LocalDate.now().minusYears(33));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.create(user);
            }
        });
        assertEquals("Некоректно заполнена форма регистрации учетной записи", ex.getMessage());
    }

    @Test
    void createVoidNameTest() {
        int id = 10;
        String login = "bademus";
        User user = new User(id,"id@ram.ru",login,null, LocalDate.now().minusYears(33));
        uc.create(user);
        Assertions.assertEquals(login,uc.getUsers().get(id).getName());
    }
    @Test
    void createBackToFutureBirthTest() {
        User user = new User(10,"id@ram.ru","bademus","vadique", LocalDate.now().plusYears(33));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.create(user);
            }
        });
        assertEquals("Некоректно заполнена форма регистрации учетной записи", ex.getMessage());
    }

    @Test
    void updateEmailTest() {
        User usr = new User(10,"id@ram.ru","bademus","vadique", LocalDate.now().minusYears(33));
        uc.create(usr);
        User user = new User(10,"idram.ru","bademus","vadique", LocalDate.now().minusYears(33));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.update(user);
            }
        });
        assertEquals("Некоректно заполнена форма обновления учетной записи", ex.getMessage());

        User user2 = new User(10,"","bademus","vadique", LocalDate.now().minusYears(33));
        ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.update(user2);
            }
        });
        assertEquals("Некоректно заполнена форма обновления учетной записи", ex.getMessage());
    }

    @Test
    void updateLoginTest() {
        User usr = new User(10,"id@ram.ru","bademus","vadique", LocalDate.now().minusYears(33));
        uc.create(usr);
        User user = new User(10,"id@ram.ru"," ","vadique", LocalDate.now().minusYears(33));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.update(user);
            }
        });
        assertEquals("Некоректно заполнена форма обновления учетной записи", ex.getMessage());
    }

    @Test
    void updateVoidNameTest() {
        User usr = new User(10,"id@ram.ru","bademus","vadique", LocalDate.now().minusYears(33));
        uc.create(usr);
        int id = 10;
        String login = "bademus";
        User user = new User(id,"id@ram.ru",login,null, LocalDate.now().minusYears(33));
        uc.create(user);
        Assertions.assertEquals(login,uc.getUsers().get(id).getName());
    }

    @Test
    void updateBackToFutureBirthTest() {
        User usr = new User(10,"id@ram.ru","bademus","vadique", LocalDate.now().minusYears(33));
        uc.create(usr);
        User user = new User(10,"id@ram.ru","bademus","vadique", LocalDate.now().plusYears(33));
        ValidationException ex = assertThrows(ValidationException.class, new Executable() {
            @Override
            public void execute() {
                uc.update(user);
            }
        });
        assertEquals("Некоректно заполнена форма обновления учетной записи", ex.getMessage());
    }

}