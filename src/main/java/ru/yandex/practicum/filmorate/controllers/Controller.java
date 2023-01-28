package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.enums.FilmMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class Controller<T> {
    private final Map<Integer,T> listOfEntities = new HashMap<>();
    private int id = 0;

    public Collection<T> findAll() {
        return listOfEntities.values();
    }

    public T create( T obj) {
        return obj;
    }

    public T update(T obj) {
        return obj;
    }

    public int setId() {
        id += 1;
        return id;
    }

    public void validate (T obj,String message){
        if (!validation(obj)) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }

    public boolean validation(T obj) {
        return  obj==null;
    }

    public Map<Integer, T> getListOfEntities() {
        return listOfEntities;
    }
}