package com.example.junitstudy.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

// Repository 는 DB쪽 테스트
// author 가 null 이다 같은 테스트는 repository 가 아닌 controller 의 테스트
// service 는 기능들이 트랜잭션을 잘 타는지 테스트

// 테스트 실행 (테스트 환경) -> 메모리(레포지토리 로드) -> 단위 테스트

@DataJpaTest // DB 와 관련된 컴포넌트만 메모리에 로딩
class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

//    @BeforeAll // 테스트 시작 전 한번만 실행
    @BeforeEach // 각 테스트 시작전에 한번씩 실행
    public void initData() {
        String title = "junit5";
        String author = "meta";

        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();
        bookRepository.save(book);
        // [ initData() + 1 save_test ] (Tx) , [ initData() + findAll_test ] (Tx) --> BeforeEach + 다음 메서드 까지 트랜잭션이 걸린다
    }

    @Test
    public void save_test() {
        // given (데이터 준비)
        String title = "junit5";
        String author = "meta";

        Book book = Book.builder()
                .title(title)
                .author(author)
                .build();

        // when (테스트  실행)
        Book savedBook = bookRepository.save(book);

        // then (검증)
        assertEquals(title, savedBook.getTitle());
        assertEquals(author, savedBook.getAuthor());
    }

    @Test
    public void findAll_test() {
        // given
        String title = "junit5";
        String author = "meta";

        // when
        List<Book> findBooks = bookRepository.findAll();

        // then
        assertEquals(title, findBooks.get(0).getTitle());
        assertEquals(author, findBooks.get(0).getAuthor());
    }

    @Test
    @Sql("classpath:db/tableInit.sql")
    public void findOne_test() {
        // given
        String title = "junit5";
        String author = "meta";

        // when
        Book findBook = bookRepository.findById(1L).get();

        // then
        assertEquals(title, findBook.getTitle());
        assertEquals(author, findBook.getAuthor());
    }

    // Junit 으로 전체 테스트를 하면 순서 보장이 안된다 -> @Order 로 번호 지정 가능
    // 테스트 메서드가 하나 실행 후 종료되면 데이터 초기화 -> 그러나 primary key auto_increment 값이 초기화가 안됨
    // AfterEach 로도 해결할 수 있다.
    @Test
    @Sql("classpath:db/tableInit.sql") // classpath : /resource, Id를 사용할 땐 넣어주는 것이 좋다
    public void delete_test() {
        // given
        Long id = 1L;

        // when
        bookRepository.deleteById(id);

        // then
        Optional<Book> findBookOptional = bookRepository.findById(id);

        assertFalse(findBookOptional.isPresent()); // false 이면 성공
    }

    @Test
    @Sql("classpath:db/tableInit.sql")
    public void update_test() {

        // beforeEach 로 save --> @Sql 로 하드 디스크(DB)에 직접 Drop (메모리는 남아있다) --> update --> 트렌젝션 종료(메모리 RollBack)
        // 만약 실제 서버에서 Id로 검증을 해야 한다면? Id 로 검증하는 방법을 모두 제외

        // given
        Long id = 1L;
        String title = "junit";
        String author = "metaCode";

        Book entity = Book.builder()
                .id(id)
                .title(title)
                .author(author)
                .build();

        // when
        Book savedEntity = bookRepository.save(entity); // 변경감지 X

        // then
        assertEquals(id, savedEntity.getId());
        assertEquals(title, savedEntity.getTitle());
        assertEquals(author, savedEntity.getAuthor());
    }

}