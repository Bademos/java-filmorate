package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Model;

import java.util.Collection;
import java.util.Map;

public interface Storage<T extends Model> {
    public Collection<T> findAll();

    public T create(T obj);

    public T update(T obj);

    public T getById(Integer id);

    public Map<Integer, T> getListOfEntities();

    public void delete(T obj);
}