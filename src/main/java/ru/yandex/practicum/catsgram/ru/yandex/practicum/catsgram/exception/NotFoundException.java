package ru.yandex.practicum.catsgram.ru.yandex.practicum.catsgram.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}
