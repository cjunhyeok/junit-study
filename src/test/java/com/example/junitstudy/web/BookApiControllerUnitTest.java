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
import static org.mockito.Mockito.verify;

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

        // given
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

        // Mock 서비스 응답 설정 (stub)
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

    @Test
    void bookTest() throws Exception {

        // given
        BookResponseDto bookResponseDto = BookResponseDto.builder()
                .id(1L)
                .title("title")
                .author("author")
                .build();

        // stub
        given(bookService.findOne(1L)).willReturn(bookResponseDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/book/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.title").value("title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.body.author").value("author"));
    }

    @Test
    void deleteTest() throws Exception {

        // given
        Long bookId = 1L;

        // DELETE 요청 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.code").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.msg").value("success"));

        // bookService의 delete 메서드가 bookId 인자로 호출되었는지 확인할 수도 있습니다.
         verify(bookService).delete(bookId);
    }
}
