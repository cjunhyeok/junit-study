package com.example.junitstudy.web;

import com.example.junitstudy.service.BookService;
import com.example.junitstudy.web.dto.response.BookListResponseDto;
import com.example.junitstudy.web.dto.response.BookResponseDto;
import com.example.junitstudy.web.dto.request.BookSaveRequestDto;
import com.example.junitstudy.web.dto.response.CMResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
public class BookApiController {

    private final BookService bookService;

    // 책 등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveRequestDto dto, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }

            throw new RuntimeException(errorMap.toString());
        }
        BookResponseDto bookResponseDto = bookService.registerBook(dto);

        return new ResponseEntity<>(CMResponseDto.builder()
                .code(1)
                .msg("success")
                .body(bookResponseDto)
                .build(), HttpStatus.CREATED); // 201 = insert
    }

    // 책 목록 보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> Books() {
        BookListResponseDto findAllBooksDto = bookService.findAllBooks();

        return new ResponseEntity<>(CMResponseDto.builder()
                .code(1)
                .msg("success")
                .body(findAllBooksDto)
                .build(), HttpStatus.OK);
    }

    // 책 한건 보기
    @GetMapping("/api/v1/book/{id}")
    public ResponseEntity<?> Book(@PathVariable(name = "id") Long id) {
        BookResponseDto findDto = bookService.findOne(id);

        return new ResponseEntity<>(CMResponseDto.builder()
                .code(1)
                .msg("success")
                .body(findDto)
                .build(), HttpStatus.OK);
    }

    // 책 삭제
    @DeleteMapping("/api/v1/book/{id}")
    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
        bookService.delete(id);

        return new ResponseEntity<>(CMResponseDto.builder()
                .code(1)
                .msg("success")
                .build(), HttpStatus.OK); // mdn http
    }

    // 책 수정
    @PutMapping("/api/v1/book/{id}")
    public ResponseEntity<?> updateBook(@PathVariable Long id,
                                        @RequestBody @Valid BookSaveRequestDto bookSaveRequestDto,
                                        BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }

            throw new RuntimeException(errorMap.toString());
        }

        BookResponseDto updatedDto = bookService.update(id, bookSaveRequestDto);

        return new ResponseEntity<>(CMResponseDto.builder()
                .code(1)
                .msg("success")
                .build(), HttpStatus.OK);
    }
}
