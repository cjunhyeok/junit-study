package com.example.junitstudy.service;

import com.example.junitstudy.domain.Book;
import com.example.junitstudy.domain.BookRepository;
import com.example.junitstudy.util.MailSender;
import com.example.junitstudy.web.dto.response.BookListResponseDto;
import com.example.junitstudy.web.dto.response.BookResponseDto;
import com.example.junitstudy.web.dto.request.BookSaveRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MailSender mailSender;

    // 책 등록
    // save() 메서드로 받은 객체는 영속화 된 객체이기 때문에 컨트롤러에 반환하면 lazy loading 문제 발생
    // Dto 를 반환해서 영속화를 끊어준다
    @Transactional(rollbackFor = RuntimeException.class) // runtime exception 발생 시 롤백
    public BookResponseDto registerBook(BookSaveRequestDto dto) {

        Book savedBook = bookRepository.save(dto.toEntity());
        if (savedBook != null) {
            if (!mailSender.send()) {
                throw new RuntimeException("메일이 전송되지 않았습니다.");
            }
        }
        return savedBook.toDto();
    }

    // 책 목록 조회
    public BookListResponseDto findAllBooks() {
        List<BookResponseDto> dtos = bookRepository.findAll().stream()
                .map(Book::toDto)
                .collect(Collectors.toList());

        return BookListResponseDto.builder().items(dtos).build();
    }

    // 책 한건 보기
    public BookResponseDto findOne(Long id) {
        Optional<Book> findBookOptional = bookRepository.findById(id);
        if (findBookOptional.isPresent()) {
            return findBookOptional.get().toDto();
        } else {
            throw new RuntimeException("Id not exist");
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public void delete(Long id) {
        bookRepository.deleteById(id); // id가 null 이면 Illegal, 그러나 해당 아이디가 db에 없으면 오류는 X
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public BookResponseDto update(Long id, BookSaveRequestDto dto) {
        Optional<Book> findBookOptional = bookRepository.findById(id);

        if (findBookOptional.isPresent()) {
            Book findBook = findBookOptional.get();
            findBook.update(dto.getTitle(), dto.getAuthor());
            return findBook.toDto();
        } else {
            throw new RuntimeException("Id not exist");
        }
    }

}
