package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.Message;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.Messages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Model;
import ru.yandex.practicum.filmorate.storage.Storage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.util.Collection;

@Slf4j
@Service
public abstract class ServiceAbs<T extends Model> {
    final protected IdGenerator idGenerator;
    final protected Storage<T> storage;

    protected ServiceAbs(Storage<T> storage) {
        this.storage = storage;
        this.idGenerator = new IdGenerator();
    }

    public T create(T obj) {
        obj.setId(idGenerator.getId());
        return storage.create(obj);
    }

    public T update(T obj) {
        return storage.update(obj);
    }

    public Collection<T> findAll() {
        return storage.findAll();
    }

    public T getById(Integer id) {
        log.info(Messages.message(Messages.ID_REQUEST) + id);
        return storage.getById(id);
    }

    protected void validate(T obj, String message) {
    }
}