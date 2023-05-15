package com.example.junitstudy.web.dto.request;

import com.example.junitstudy.domain.Book;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter // Controller 에서 Setter 호출하여 값 채워짐
@Getter
public class BookSaveRequestDto {

    @NotBlank
    @Size(min = 1, max = 50)
    private String title;
    @NotBlank
    @Size(min = 2, max = 20)
    private String author;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .author(author)
                .build();
    }
}
