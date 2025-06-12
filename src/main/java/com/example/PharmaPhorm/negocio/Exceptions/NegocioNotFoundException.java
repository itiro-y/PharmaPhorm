package com.example.PharmaPhorm.negocio.Exceptions;

public class NegocioNotFoundException extends RuntimeException{
    public NegocioNotFoundException(Long id){
        super("Não foi possível achar o funcionário " + id);
    }
}
