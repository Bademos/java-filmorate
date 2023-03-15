package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.storage.interfaces.Storage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public abstract class StorageAbs<T extends Model> implements Storage<T> {
    protected final Map<Integer, T> listOfEntities = new HashMap<>();

    @Override
    public Collection<T> findAll() {
        return listOfEntities.values();
    }

    @Override
    public T create(T obj) {
        listOfEntities.put(obj.getId(), obj);
        return obj;
    }

    @Override
    public T update(T obj) {
        if (listOfEntities.containsKey(obj.getId())) {
            listOfEntities.put(obj.getId(), obj);
        } else {
            log.debug(Messages.message(Messages.IS_NOT_IN_LIST));
            throw new NotFoundException(Messages.message(Messages.IS_NOT_IN_LIST));
        }
        return obj;
    }

    @Override
    public Map<Integer, T> getListOfEntities() {
        return listOfEntities;
    }

    @Override
    public T getById(Integer id) {
        T obj;
        if (listOfEntities.containsKey(id)) {
            obj = listOfEntities.get(id);
        } else {
            throw new NotFoundException(Messages.
                    message(Messages.IS_NOT_IN_LIST));
        }
        return obj;
    }

    @Override
    public void delete(T obj) {
        listOfEntities.remove(obj);
    }
}