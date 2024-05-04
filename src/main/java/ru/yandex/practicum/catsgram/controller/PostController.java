package ru.yandex.practicum.catsgram.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.catsgram.exception.ConditionsNotMetException;
import ru.yandex.practicum.catsgram.exception.ParameterNotValidException;
import ru.yandex.practicum.catsgram.model.Post;
import ru.yandex.practicum.catsgram.service.PostService;

import java.util.Collection;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping
    public Collection<Post> findAll(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "desc") String sort
    ) {
        if (!sort.equals("desc") && !sort.equals("asc")) {
            throw new ParameterNotValidException("sort", "Некорректное значение");
        }
        if (size <= 0) {
            throw new ParameterNotValidException("size", "Должен быть больше нуля");
        }
        if (page < 0) {
            throw new ParameterNotValidException("page", "Не может быть меньше нуля");
        }
        return postService.findAll((page - 1) * size, size, sort);
    }

    @GetMapping("/{id}")
    public Post get(@PathVariable long id) {
        return postService.find(id).orElseThrow(() -> new ConditionsNotMetException("Указанный пост не найден"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Post create(@RequestBody Post post) {
        return postService.create(post);
    }

    @PutMapping
    public Post update(@RequestBody Post post) {
        return postService.update(post);
    }
}