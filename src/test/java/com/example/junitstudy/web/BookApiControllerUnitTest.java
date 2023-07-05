package com.example.junitstudy.web;

import com.example.junitstudy.service.BookService;
import com.example.junitstudy.web.dto.response.BookListResponseDto;
import com.example.junitstudy.web.dto.response.BookResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;

@WebMvcTest(BookApiController.class)
public class BookApiControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    void booksTest() throws Exception {
        List<BookResponseDto> bookList = new ArrayList<>();
        bookList.add(BookResponseDto.builder()
                .title("title")
                .author("author")
                .build());
        bookList.add(BookResponseDto.builder()
                .title("title2")
                .author("author2")
                .build());
        BookListResponseDto dto = BookListResponseDto.builder().items(bookList).build();

        // Mock 서비스 응답 설정
        given(bookService.findAllBooks()).willReturn(dto);

        // GET 요청 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].title").value("title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.items[1].title").value("title2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.items[0].author").value("author"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.items[1].author").value("author2"));
    }
}
