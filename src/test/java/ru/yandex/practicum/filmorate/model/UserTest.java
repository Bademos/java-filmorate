package ru.yandex.practicum.filmorate.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createEmailTest() {
        User user = new User(10, "idram.ru", "bademus", "vadique", LocalDate.now().minusYears(33));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createLoginTest() {
        User user = new User(10, "id@ram.ru", " ", "vadique", LocalDate.now().minusYears(33));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createVoidNameTest() {
        int id = 1;
        String login = "bademus";
        User user = new User(id, "id@ram.ru", login, null, LocalDate.now().minusYears(33));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals(null, response.getBody().getName());
    }

    @Test
    void createBackToFutureBirthTest() {
        User user = new User(10, "id@ram.ru", "bademus", "vadique", LocalDate.now().plusYears(33));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", user, User.class);
        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void updateEmailTest() {
        User usr = new User(1, "id@ram.ru", "bademus", "vadique", LocalDate.now().minusYears(33));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", usr, User.class);

        User user2 = new User(1, null, "bademus", "vadique", LocalDate.now().minusYears(33));
        HttpEntity<User> entity = new HttpEntity<User>(user2);
        ResponseEntity<User> response2 = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);
        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }

    @Test
    void updateLoginTest() {
        User usr = new User(10, "id@ram.ru", "bademus", "vadique", LocalDate.now().minusYears(33));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", usr, User.class);
        User user = new User(10, "id@ram.ru", " ", "vadique", LocalDate.now().minusYears(33));
        HttpEntity<User> entity = new HttpEntity<User>(user);
        ResponseEntity<User> response2 = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);
        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }

    @Test
    void updateBackToFutureBirthTest() {
        User usr = new User(10, "id@ram.ru", "bademus", "vadique", LocalDate.now().minusYears(33));
        ResponseEntity<User> response = restTemplate.postForEntity("/users", usr, User.class);
        User user = new User(10, "id@ram.ru", "bademus", "vadique", LocalDate.now().plusYears(33));
        HttpEntity<User> entity = new HttpEntity<User>(user);
        ResponseEntity<User> response2 = restTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);
        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }
}