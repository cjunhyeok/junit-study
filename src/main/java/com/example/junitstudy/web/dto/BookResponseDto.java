package com.example.junitstudy.web.dto;

import com.example.junitstudy.domain.Book;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class BookResponseDto {

    private Long id;
    private String title;
    private String author;

    public BookResponseDto toDto(Book savedBook) {
        this.id = savedBook.getId();
        this.title = savedBook.getTitle();
        this.author = savedBook.getAuthor();
        return this;
    }
}
