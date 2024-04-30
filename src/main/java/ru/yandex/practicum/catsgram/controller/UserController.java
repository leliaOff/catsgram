package ru.yandex.practicum.catsgram.controller;

import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.model.User;
import ru.yandex.practicum.catsgram.ru.yandex.practicum.catsgram.exception.DuplicatedDataException;
import ru.yandex.practicum.catsgram.ru.yandex.practicum.catsgram.exception.NotFoundException;

import java.time.Instant;
import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final HashMap<Long, User> users = new HashMap<>();

    @GetMapping
    public Collection<User> findAll() {
        return users.values();
    }

    @PostMapping
    public User create(@RequestBody User user) {
        if (user.getEmail() == null || user.getEmail().isBlank()) {
            throw new ru.yandex.practicum.catsgram.exception.ConditionsNotMetException("Необходимо указать адрес электронной почты");
        }
        Optional<User> equalUser = this.getUserByEmail(user.getEmail());
        if (equalUser.isPresent()) {
            throw new DuplicatedDataException("Этот адрес электронной почты уже используется");
        }
        user.setId(getNextId());
        user.setRegistrationDate(Instant.now());
        users.put(user.getId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        if (user.getId() == null) {
            throw new ru.yandex.practicum.catsgram.exception.ConditionsNotMetException("Id должен быть указан");
        }
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Id должен быть указан");
        }
        User currentUser = users.get(user.getId());
        if (user.getEmail() != null && !user.getEmail().isBlank()) {
            Optional<User> equalUser = this.getUserByEmail(user.getEmail());
            if (equalUser.isPresent() && !Objects.equals(equalUser.get().getId(), user.getId())) {
                throw new DuplicatedDataException("Этот адрес электронной почты уже используется");
            }
            currentUser.setEmail(user.getEmail());
        }
        if (user.getUsername() != null && !user.getUsername().isBlank()) {
            currentUser.setUsername(user.getUsername());
        }
        if (user.getPassword() != null && !user.getPassword().isBlank()) {
            currentUser.setPassword(user.getPassword());
        }

        return currentUser;
    }

    // вспомогательный метод для генерации идентификатора нового поста
    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }

    private Optional<User> getUserByEmail(String email) {
        return this.users.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }
}
