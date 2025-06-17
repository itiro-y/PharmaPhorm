package com.example.PharmaPhorm.funcionario;

import com.example.PharmaPhorm.Enum.Setor;
import com.example.PharmaPhorm.caixa.CaixaController;
import com.example.PharmaPhorm.funcionario.Exceptions.FuncionarioLimitExceededException;
import com.example.PharmaPhorm.funcionario.Exceptions.FuncionarioNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class FuncionarioController {
    private final FuncionarioRepository repository;
    private final CaixaController caixaController;

    // ✅ Constantes de limite permanecem
    private final int LIMITE_GERENCIA = 1;
    private final int LIMITE_ATENDIMENTO = 4;
    private final int LIMITE_GESTAO = 4;
    private final int LIMITE_FINANCEIRO = 3;
    private final int LIMITE_VENDAS = 5;
    private final int LIMITE_ALMOXARIFADO = 3;

    // ✅ Variáveis de contador foram REMOVIDAS

    FuncionarioController(FuncionarioRepository repository, CaixaController caixaController) {
        this.repository = repository;
        this.caixaController = caixaController;
        // ✅ Construtor limpo, sem inicialização de contadores
    }

    @GetMapping("/funcionario")
    List<Funcionario> getFuncionario() {
        return repository.findAll();
    }

    @PostMapping("/funcionario")
    Funcionario addFuncionario(@RequestBody Funcionario funcionario) {
        // A verificação agora usa o banco de dados como fonte da verdade
        if (checarLimiteFuncionarios(funcionario.getSetor())) {
            return repository.save(funcionario);
        } else {
            throw new FuncionarioLimitExceededException(funcionario.getSetor());
        }
    }

    @GetMapping("/funcionario/{id}")
    Funcionario getFuncionariosByID(@PathVariable Long id) {
        return repository.findById(id).orElseThrow(() -> new FuncionarioNotFoundException(id));
    }

    @PutMapping("/funcionario/{id}")
    Funcionario updateFuncionario(@RequestBody Funcionario funcionario, @PathVariable Long id) {
        return repository.findById(id)
                .map(f -> {
                    // Lógica para checar se a mudança de setor é válida
                    if (!f.getSetor().equals(funcionario.getSetor())) {
                        if (!checarLimiteFuncionarios(funcionario.getSetor())) {
                            throw new FuncionarioLimitExceededException(funcionario.getSetor());
                        }
                    }
                    f.setNome(funcionario.getNome());
                    f.setIdade(funcionario.getIdade());
                    f.setGenero(funcionario.getGenero());
                    f.setSetor(funcionario.getSetor());
                    f.setSalariobase(funcionario.getSalariobase());
                    return repository.save(f);
                })
                .orElseThrow(() -> new FuncionarioNotFoundException(id));
    }

    @DeleteMapping("/funcionario/{id}")
    void deleteFuncionario(@PathVariable Long id) {
        // ✅ O método delete agora não precisa se preocupar com contadores.
        // Ele apenas deleta o funcionário do banco de dados.
        if (!repository.existsById(id)) {
            throw new FuncionarioNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @GetMapping("/funcionario/calcularParticipacaoLucro")
    public double calcularParticipacaoLucros() {
        return caixaController.estimarLucroMensal() * 0.1 / repository.count();
    }

    // ✅ MÉTODO DE CHECAGEM REFEITO E MAIS SEGURO
    // Retorna true se ainda houver vaga no setor.
    public boolean checarLimiteFuncionarios(Setor setor) {
        // Consulta o número atual de funcionários naquele setor diretamente do banco
        long contagemAtual = repository.countBySetor(setor);

        return switch (setor) {
            case GERENCIA -> contagemAtual < LIMITE_GERENCIA;
            case ATENDIMENTO_AO_CLIENTE -> contagemAtual < LIMITE_ATENDIMENTO;
            case GESTAO_DE_PESSOAS -> contagemAtual < LIMITE_GESTAO;
            case FINANCEIRO -> contagemAtual < LIMITE_FINANCEIRO;
            case VENDAS -> contagemAtual < LIMITE_VENDAS;
            case ALMOXARIFADO -> contagemAtual < LIMITE_ALMOXARIFADO;
            default -> throw new RuntimeException("Setor inválido");
        };
    }
}