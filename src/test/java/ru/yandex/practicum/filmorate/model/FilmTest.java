
package ru.yandex.practicum.filmorate.model;

import org.aspectj.lang.annotation.After;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.BeforeClass;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FilmTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void createVoidNameTest() {
        Film film = new Film(190, null, "impuudicus", LocalDate.now().minusYears(14), -180);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
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

        Film film = Film.builder().name("MACBETH").description(description).
                releaseDate(LocalDate.now().minusYears(13)).duration(180).build();

        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void createNegativeDurationTest() {
        Film film = new Film(193, "Movie", "impudicus", LocalDate.now().minusYears(20), -180);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);
        assertEquals("400 BAD_REQUEST", response.getStatusCode().toString());
    }

    @Test
    void updateVoidNameTest() {
        Film film = new Film(190, "not null", "impudicus", LocalDate.now().minusYears(14), 180);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        Film film2 = new Film(1, null, "impudicus", LocalDate.now().minusYears(14), 180);
        HttpEntity<Film> entity = new HttpEntity<Film>(film2);
        ResponseEntity<Film> response2 = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);
        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());


        ResponseEntity<Collection<Film>> response3 = restTemplate.exchange("/films", HttpMethod.GET, null,
                new ParameterizedTypeReference<Collection<Film>>() {
                });
        System.out.println(response2.getBody());
        System.out.println("hrllo");
    }

    @Test
    void updateLongDescriptionTest() {
        Film film = new Film(190, "not null", "impudicus", LocalDate.now().minusYears(14), 180);
        restTemplate.postForLocation("/films", film);
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

        Film film2 = Film.builder().name("MACBETH").description(description).
                releaseDate(LocalDate.now().minusYears(13)).duration(180).build();

        HttpEntity<Film> entity = new HttpEntity<Film>(film2);
        ResponseEntity<Film> response2 = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);
        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }

    @Test
    void updateNegativeDurationTest() {
        Film film = new Film(190, "not null", "impudicus", LocalDate.now().minusYears(14), 180);
        ResponseEntity<Film> response = restTemplate.postForEntity("/films", film, Film.class);

        Film film2 = new Film(1, "nor", "impudicus", LocalDate.now().minusYears(14), -180);
        HttpEntity<Film> entity = new HttpEntity<Film>(film2);
        ResponseEntity<Film> response2 = restTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);
        assertEquals("400 BAD_REQUEST", response2.getStatusCode().toString());
    }


}