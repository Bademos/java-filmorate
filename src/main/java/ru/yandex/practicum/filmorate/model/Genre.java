package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Builder(toBuilder = true)
@Getter
@Setter
public class Genre extends ListModel {
    public final static Map<Integer, String> mp = new HashMap<Integer, String>() {{
        put(1, "Комедия");
        put(2, "Драма");
        put(3, "Мультфильм");
        put(4, "Триллер");
        put(5, "Документальный");
        put(6, "Боевик");
        put(0, null);
    }};

    public Genre() {
    }

    public Genre(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Genre(int id) {
        this.id = id;
        this.name = getNameByID(id, mp);
    }

    public Genre(String name) {
        if (getIdByName(name, mp).isPresent()) {
            this.id = getIdByName(name, mp).get();
            this.name = name;
        }
    }
}