package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import java.util.*;

@Builder(toBuilder = true)
@AllArgsConstructor
//@Data
@Getter
@Setter
public class Film extends Model {
    private int id;
    @NotBlank
    private String name;
    @Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
    final private  Set<Genre> genres= new TreeSet<Genre>(new Comparator<Genre>() {
        public int compare(Genre o1, Genre o2) {
            return o1.getId()-o2.getId();
        }
    });
    private Mpa mpa;
    @JsonIgnore
    final private Set<Integer> likes = new HashSet<>();

    public void addLike(Integer id) {
        likes.add(id);
    }

    public void deleteLike(Integer id) {
        likes.remove(id);
    }
    public void addGenre(Genre genre){
        genres.add(genre);
    }
    public void removeAllGenres(){genres.clear();}

    public void removeGenre(Genre genre){
        genres.remove(genre);
    }
}