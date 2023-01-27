package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
<<<<<<< HEAD
import lombok.Value;
=======
>>>>>>> 200a7cdd93df3802839354174c96ffb7c08ba50a
import org.springframework.boot.autoconfigure.domain.EntityScan;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

@Builder(toBuilder = true)
@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotBlank
    @NotNull
    private String name;
    @Size(max = 200)
    private String description;
    LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
}