package com.example.junitstudy.web.handler;

import com.example.junitstudy.web.dto.response.CMResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // RuntimeException 이 터지면 잡아서 해당 메서드 실행
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> apiException(RuntimeException e) {

        return new ResponseEntity<>(CMResponseDto.builder().code(-1).msg(e.getMessage()).build(),
                HttpStatus.BAD_REQUEST);
    }
}
