package ru.yandex.practicum.filmorate.model;

import lombok.*;
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
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Builder(toBuilder = true)
@AllArgsConstructor
@Data
public class Film extends Model {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    final private Set<Integer> likes = new HashSet<>();
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;

    public void addLike(Integer id) {
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }
}