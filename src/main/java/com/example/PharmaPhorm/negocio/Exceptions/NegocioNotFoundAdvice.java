package com.example.PharmaPhorm.negocio.Exceptions;

import com.example.PharmaPhorm.funcionario.Exceptions.FuncionarioNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NegocioNotFoundAdvice {
    @ExceptionHandler(NegocioNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String NegocioNotFoundHandle(NegocioNotFoundException ex){
        return ex.getMessage();
    }
}
