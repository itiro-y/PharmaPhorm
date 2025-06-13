package com.example.PharmaPhorm.transportadora.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
class TransportadoraNotFoundAdvice {

    @ExceptionHandler(TransactionSystemException.class)

    @ResponseStatus(HttpStatus.NOT_FOUND)
    String funcionarioNotFoundHandle(TransactionSystemException ex){
        return ex.getMessage();
    }
}
