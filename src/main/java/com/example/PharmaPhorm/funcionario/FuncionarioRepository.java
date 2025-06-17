package com.example.PharmaPhorm.funcionario;

import com.example.PharmaPhorm.Enum.Setor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    // ✅ ADICIONE ESTE MÉTODO
    // Este método permite contar quantos registros existem para um determinado setor.
    long countBySetor(Setor setor);
}