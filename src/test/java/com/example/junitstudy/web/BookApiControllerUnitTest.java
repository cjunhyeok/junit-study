package com.example.junitstudy.web;

import com.example.junitstudy.service.BookService;
import com.example.junitstudy.web.dto.request.BookSaveRequestDto;
import com.example.junitstudy.web.dto.response.BookListResponseDto;
import com.example.junitstudy.web.dto.response.BookResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.body.items[0].title").value("title"))
                .andExpect(jsonPath("$.body.items[1].title").value("title2"))
                .andExpect(jsonPath("$.body.items[0].author").value("author"))
                .andExpect(jsonPath("$.body.items[1].author").value("author2"));
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
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.body.title").value("title"))
                .andExpect(jsonPath("$.body.author").value("author"));
    }

    @Test
    void deleteTest() throws Exception {

        // given
        Long bookId = 1L;

        // DELETE 요청 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.msg").value("success"));

        // bookService의 delete 메서드가 bookId 인자로 호출되었는지 확인할 수도 있습니다.
         verify(bookService).delete(bookId);
    }

    @Test
    void updateTest() throws Exception {

        // given
        Long bookId = 1L;
        BookSaveRequestDto bookSaveRequestDto = new BookSaveRequestDto();
        bookSaveRequestDto.setTitle("Updated Title");
        bookSaveRequestDto.setAuthor("Updated Author");

        // PUT 요청 및 응답 검증
        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/book/{id}", bookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(bookSaveRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.msg").value("success"));

        // bookService의 update 메서드가 bookId와 bookSaveRequestDto 인자로 호출되었는지 확인할 수도 있습니다.
        // verify(bookService).update(bookId, bookSaveRequestDto);
    }

    @Test
    void saveBook() throws Exception {
        // Arrange
        BookSaveRequestDto dto = new BookSaveRequestDto();
        dto.setTitle("Book Title");
        dto.setAuthor("Book Author");

        BookResponseDto responseDto = new BookResponseDto(/* fill in with relevant values */);
        when(bookService.registerBook(ArgumentMatchers.any(BookSaveRequestDto.class)))
                .thenReturn(responseDto);

        // Act
        ResultActions resultActions = mockMvc.perform(post("/api/v1/book")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // Assert
        resultActions.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(1))
                .andExpect(jsonPath("$.msg").value("success"))
                .andExpect(jsonPath("$.body").exists())
                .andExpect(jsonPath("$.body").isNotEmpty());
    }
}
