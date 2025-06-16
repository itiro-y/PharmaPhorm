package com.example.PharmaPhorm.funcionario.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class FuncionarioNotFoundAdvice {

    @ExceptionHandler(FuncionarioNotFoundException.class)

    @ResponseStatus(HttpStatus.NOT_FOUND)

    String funcionarioNotFoundHandle(FuncionarioNotFoundException ex){
        return ex.getMessage();
    }
}
