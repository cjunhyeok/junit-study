package com.example.junitstudy.web.dto;

import com.example.junitstudy.domain.Book;
import lombok.Getter;
import lombok.Setter;

@Setter // Controller 에서 Setter 호출하여 값 채워짐
@Getter
public class BookSaveRequestDto {

    private String title;
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
