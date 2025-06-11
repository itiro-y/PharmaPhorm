package com.example.PharmaPhorm.funcionario.Exceptions;

public class FuncionarioNotFoundException extends RuntimeException {
    public FuncionarioNotFoundException(Long id){
        super("Não foi possível achar o funcionário " + id);
    }
}
