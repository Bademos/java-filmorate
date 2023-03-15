package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.db.MpaDbStorage;

import java.util.List;

@Service
public class MpaService {
    private final MpaDbStorage mpaDbStorage;

    public MpaService(MpaDbStorage mpaDbStorage) {
        this.mpaDbStorage = mpaDbStorage;
    }

    public Mpa getMpaById(int id) {
        Mpa mpa = mpaDbStorage.getMpaById(id);
        if (mpa == null) {
            throw new NotFoundException("Такого MPA нет");
        }
        return mpa;
    }

    public List<Mpa> getAllMpa(){
        return mpaDbStorage.getListOfMpa();
    }
}