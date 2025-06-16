package com.example.PharmaPhorm.funcionario.Exceptions;

import com.example.PharmaPhorm.Enum.Setor;

public class FuncionarioLimitExceededException extends RuntimeException {
    public FuncionarioLimitExceededException(Setor setor) {
        super("Funcionario limit exceeded in Setor: " + setor);
    }
}
