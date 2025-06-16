package com.example.PharmaPhorm.caixa.Exceptions;

import com.example.PharmaPhorm.caixa.Exceptions.SaldoInsuficienteException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@RestControllerAdvice
public class SaldoInsuficienteAdvice {
    @ExceptionHandler(SaldoInsuficienteException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    String SaldoInsuficienteHandle(SaldoInsuficienteException ex){
        return ex.getMessage();
    }
}
