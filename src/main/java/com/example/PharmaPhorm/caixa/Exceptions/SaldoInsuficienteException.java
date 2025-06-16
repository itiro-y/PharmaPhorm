package com.example.PharmaPhorm.caixa.Exceptions;

public class SaldoInsuficienteException extends RuntimeException{
    public SaldoInsuficienteException(){
        super("O saldo do caixa é insuficiente para realizar a operação ");
    }
}
