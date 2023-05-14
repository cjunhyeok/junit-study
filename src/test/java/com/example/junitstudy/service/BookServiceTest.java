package com.example.junitstudy.service;

import com.example.junitstudy.domain.Book;
import com.example.junitstudy.domain.BookRepository;
import com.example.junitstudy.util.MailSender;
import com.example.junitstudy.web.dto.BookResponseDto;
import com.example.junitstudy.web.dto.BookSaveRequestDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// 서비스 레이어를 테스트할 때 repository 레이어 까지 메모리에 올릴 필요가 없으므로 가짜 환경 구축
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

//    @Autowired // autowired 불가능
    @InjectMocks // 목 객체 주입
    private BookService bookService;
    @Mock // 가짜 객체 생성
    private BookRepository bookRepository;
    @Mock
    private MailSender mailSender;

    @Test
    public void registerBookTest() {
        // given
        BookSaveRequestDto dto = new BookSaveRequestDto();
        dto.setTitle("junit");
        dto.setAuthor("metaCode");

        // stub (가설)
        // when(bookRepository.save(any())) -> 어떤 객체를 save 하면
        // thenReturn(dto.toEntity()) --> dto.toEntity 객체가 반환될 것 이다.
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        when(mailSender.send()).thenReturn(true);

        // when
        BookResponseDto bookResponseDto = bookService.registerBook(dto);

        // then
        assertThat(bookResponseDto.getTitle()).isEqualTo(dto.getTitle());
    }

    @Test
    public void findAll_test() {
        // given

        // stub
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "junit", "meta"));
        books.add(new Book(2L, "spring", "metaCoding"));

        when(bookRepository.findAll()).thenReturn(books);

        // when
        List<BookResponseDto> findBooksDto = bookService.findAllBooks();

        // then
        assertThat(findBooksDto.get(0).getTitle()).isEqualTo(books.get(0).getTitle());
    }

    @Test
    public void findOne_test() {
        // given
        Long id = 1L;

        // stub
        Book book = new Book(1L, "junit", "meta");
        Optional<Book> bookOptional = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOptional);

        // when
        BookResponseDto findDto = bookService.findOne(id);

        // then
        assertThat(findDto.getTitle()).isEqualTo(book.getTitle());
    }

    @Test
    public void update_test() {
        // given
        Long id = 1L;
        BookSaveRequestDto dto = new BookSaveRequestDto();
        dto.setTitle("spring");
        dto.setAuthor("metaCoding");

        // stub
        Book book = new Book(1L, "junit", "meta");
        Optional<Book> bookOptional = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOptional);

        // when
        BookResponseDto updatedBook = bookService.update(id, dto);

        // then
        assertThat(updatedBook.getTitle()).isEqualTo(dto.getTitle());
    }
}