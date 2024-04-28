package ru.yandex.practicum.catsgram.model;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"id"})
public class Image {
    private Long id;
    private Long postId;
    private String originalFilename;
    private String filepath;
}
