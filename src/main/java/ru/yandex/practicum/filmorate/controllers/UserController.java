package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {
    int id = 0;
    private final Map<Integer, User> users = new HashMap<>();
    private final static Logger log = LoggerFactory.getLogger(FilmController.class);


    @GetMapping
    public Collection<User> findAll(){
        log.info("Текущее количество пользоваелей: "+users.size());
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user){
        if(!userValidation(user))
                {
                    log.info("Некоректно заполнена форма регистрации учетной записи");
                    throw new ValidationException("Некоректно заполнена форма регистрации учетной записи");
        }
        if (user.getName()==null){
            user.setName(user.getLogin());
        }
        if(user.getId() ==0){
            user.setId(setId());
            }
         users.put(user.getId(),user);
        log.info("Пользователь " + user + " успешно добавлен");

        return user;
    }

    @PutMapping
    public User update(@RequestBody User user){
        if(!userValidation(user) ){
            log.info("Некоректно заполнена форма обновления учетной записи");
            throw new ValidationException("Некоректно заполнена форма обновления учетной записи");
        }
        if(user.getId() ==0){
        user.setId(setId());}

        if (user.getName()==null){
            user.setName(user.getLogin());
        }
        if(users.containsKey(user.getId())){
            users.put(user.getId(),user);
            log.info("Пользователь " + user + " успешно обновлен");
        }else {
            log.info("Такого пользователя нет в списке");
            throw new ValidationException("Такого пользователя нет в списке");
        }
        return user;
    }

    public int setId(){
        id += 1;
        return id;
    }

    public boolean userValidation(User user){
        return !(user.getLogin()==null||
                user.getEmail()==null||
                !user.getEmail().contains("@")||
                user.getBirthday().isAfter(LocalDate.now()));
    }
}
