package com.example.PharmaPhorm.funcionario.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FuncionarioLimitExceededAdvice {
    @ExceptionHandler(FuncionarioLimitExceededException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    String funcionarioLimitExceededHandle(FuncionarioLimitExceededException ex) {
        return ex.getMessage();
    }
}
