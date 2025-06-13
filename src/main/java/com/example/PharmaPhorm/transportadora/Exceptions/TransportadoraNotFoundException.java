package com.example.PharmaPhorm.transportadora.Exceptions;

public class TransportadoraNotFoundException extends RuntimeException {
    public TransportadoraNotFoundException(Long id){
        super("Não foi possível achar o transporadora " + id);
    }
}
