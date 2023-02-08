package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Model;

import javax.validation.Valid;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class Controller<T extends Model> {
    protected final Map<Integer, T> listOfEntities = new HashMap<>();
    private int id = 0;

    public Collection<T> findAll() {
        log.info(Messages.message(Messages.CURRENT_CONDITION) + listOfEntities.size());
        return listOfEntities.values();
    }

    public T create(T obj) {
        validate(obj, Messages.message(Messages.INCORRECT_FORM));
        obj.setId(setId());
        listOfEntities.put(obj.getId(), obj);
        log.info(Messages.message(Messages.SUCCESS_ADDED) + obj);
        return obj;
    }

    public T update(T obj) {
        validate(obj, Messages.message(Messages.INCORRECT_UPDATE_FORM));
        if (listOfEntities.containsKey(obj.getId())) {
            listOfEntities.put(obj.getId(), obj);
            log.info(Messages.message(Messages.SUCCESS_UPDATED) + obj);
        } else {
            log.debug(Messages.message(Messages.IS_NOT_IN_LIST));
            throw new ValidationException(Messages.message(Messages.IS_NOT_IN_LIST));
        }
        return obj;
    }

    public int setId() {
        id += 1;
        return id;
    }

    public void validate(T obj, String message) {
        if (!validation(obj)) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }

    public boolean validation(T obj) {
        return false;
    }

    public Map<Integer, T> getListOfEntities() {
        return listOfEntities;
    }
}