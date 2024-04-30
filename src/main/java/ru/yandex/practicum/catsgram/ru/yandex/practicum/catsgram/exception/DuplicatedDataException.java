package ru.yandex.practicum.catsgram.ru.yandex.practicum.catsgram.exception;

public class DuplicatedDataException extends RuntimeException {
    public DuplicatedDataException(String message) {
        super(message);
    }
}
