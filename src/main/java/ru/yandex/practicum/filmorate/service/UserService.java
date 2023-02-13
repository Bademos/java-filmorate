package ru.yandex.practicum.filmorate.service;

import lombok.Generated;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.enums.UserMessages;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.util.IdGenerator;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Set;

@Component
@Service
@Slf4j
public class UserService {
    final private InMemoryUserStorage userStorage;
    final private IdGenerator idGenerator;

    @Autowired
    public UserService(InMemoryUserStorage userStorage){
        this.userStorage = userStorage;
        this.idGenerator = new IdGenerator();
    }

    public Collection<User> findAll(){

        return userStorage.findAll();
    }

    public User create(User user){
        validate(user, UserMessages.userMessage(UserMessages.INCORRECT_USER_FORM));
        String name = fixVoidName(user);
        user.setName(name);
        user.setId(idGenerator.getId());
        return userStorage.create(user);
    }

    public User update(User user){
        validate(user, UserMessages.userMessage(UserMessages.INCORRECT_UPDATE_USER_FORM));
        String name = fixVoidName(user);
        user.setName(name);
        return userStorage.update(user);
    }

    public User getById(Integer id){
        return userStorage.getById(id);
    }

    public User addFriend(Integer userId, Integer friendId){
        checkUser(userId,friendId);
        return userStorage.addFriend(userId,friendId);
    }

    public User removeFriend(Integer userId, Integer friendId){
        checkUser(userId, friendId);
        return userStorage.removeFriend(userId,friendId);
    }

    public Set<User> getAllFriends(Integer userId){
        checkUser(userId,userId);
        return userStorage.getAllFriends(userStorage.getById(userId));
    }

    public Set<User> getCommonFriends(Integer user1Id, Integer user2Id){
        checkUser(user1Id,user2Id);
        return userStorage.getCommonFriends(user1Id, user2Id);
    }

    void checkUser(Integer userId, Integer friendId){
        if (!userStorage.getListOfEntities().containsKey(userId)||
        !userStorage.getListOfEntities().containsKey(friendId)){
            throw new ValidationException(UserMessages.userMessage(UserMessages.USER_IS_NOT_IN_LIST));
        }
    }

    public boolean validation(User user) {
        return !(user.getBirthday().isAfter(LocalDate.now()));
    }

    public void validate(User user, String message) {
        if (!validation(user)) {
            log.debug(message);
            throw new ValidationException(message);
        }
    }

    public String fixVoidName(User user) {
        if (user.getName() == null) {
            user.setName(user.getLogin());
            return user.getLogin();
        }
        return user.getName();
    }
}