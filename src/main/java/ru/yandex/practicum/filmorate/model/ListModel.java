package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;
import java.util.Optional;

@Getter
@Setter
public abstract class ListModel {
    protected int id;
    protected String name;


    public static String getNameByID(Integer id, Map<Integer, String> mp) {
        if (mp.containsKey(id)) {
            return mp.get(id);
        }
        return null;
    }

    public static Optional<Integer> getIdByName(String name, Map<Integer, String> mp) {
        if (mp.containsValue(name)) {
            return mp.entrySet().stream().filter(entry -> name.equals(entry.getValue()))
                    .map(Map.Entry::getKey).findFirst();
        }
        return Optional.empty();
    }
}
