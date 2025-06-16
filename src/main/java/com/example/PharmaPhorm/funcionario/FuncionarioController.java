package com.example.PharmaPhorm.funcionario;

import com.example.PharmaPhorm.Enum.Setor;
import com.example.PharmaPhorm.caixa.CaixaController;
import com.example.PharmaPhorm.caixa.CaixaRepository;
import com.example.PharmaPhorm.funcionario.Exceptions.FuncionarioLimitExceededException;
import com.example.PharmaPhorm.funcionario.Exceptions.FuncionarioNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
class FuncionarioController {
    private final FuncionarioRepository repository;
    private final CaixaController caixaController;

    private final int LIMITE_GERENCIA = 1;
    private final int LIMITE_ATENDIMENTO = 4;
    private final int LIMITE_GESTAO = 4;
    private final int LIMITE_FINANCEIRO = 3;
    private final int LIMITE_VENDAS = 5;
    private final int LIMITE_ALMOXARIFADO = 3;

    private int counterGerencia;
    private int counterAtendimento;
    private int counterGestao;
    private int counterFinanceiro;
    private int counterVendas;
    private int counterAlmoxarifado;

    FuncionarioController(FuncionarioRepository repository, CaixaController caixaController) {
        this.repository = repository;
        this.caixaController = caixaController;

        counterGerencia = 1;
        counterAtendimento = 0;
        counterGestao = 1;
        counterFinanceiro = 1;
        counterVendas = 1;
        counterAlmoxarifado = 1;
    }

    @GetMapping("/funcionario")
    List<Funcionario> getFuncionario() {
        return repository.findAll();
    }

    @PostMapping("/funcionario")
    Funcionario addFuncionario(@RequestBody Funcionario funcionario) {
        if(checarLimiteFuncionarios(funcionario)){
            if(funcionario != null){
                switch (funcionario.getSetor()){
                    case Setor.GERENCIA -> this.counterGerencia++;
                    case Setor.ATENDIMENTO_AO_CLIENTE -> this.counterAtendimento++;
                    case Setor.GESTAO_DE_PESSOAS -> this.counterGestao++;
                    case Setor.FINANCEIRO -> this.counterFinanceiro++;
                    case Setor.VENDAS -> this.counterVendas++;
                    case Setor.ALMOXARIFADO -> this.counterAlmoxarifado++;
                    default -> throw new RuntimeException("Setor invalido");
                }
                ResponseEntity.ok("HELLO");
                return repository.save(funcionario);
            }
        } else{
            throw new FuncionarioLimitExceededException(funcionario.getSetor());
        }
        return null;
    }

    @GetMapping("/funcionario/{id}")
    Funcionario getFuncionariosByID(@PathVariable Long id ) {
        return repository.findById(id).orElseThrow(() -> new FuncionarioNotFoundException(id));
    }

    @PutMapping("/funcionario/{id}")
    Funcionario updateFuncionario(@RequestBody Funcionario funcionario, @PathVariable Long id) {
        return repository.findById(id)
                .map(f -> {
                    f.setNome(funcionario.getNome());
                    f.setIdade(funcionario.getIdade());
                    f.setGenero(funcionario.getGenero());
                    f.setSetor(funcionario.getSetor());
                    f.setSalariobase(funcionario.getSalariobase());
                    return repository.save(f);
                })
                .orElseGet(() -> {
                    return repository.save(funcionario);
                });
    }

    @DeleteMapping("/funcionario/{id}")
    void deleteFuncionario(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @GetMapping("/funcionario/calcularParticipacaoLucro")
    public double calcularParticipacaoLucros() {
        return caixaController.estimarLucroMensal() * 0.1 / repository.findAll().size();
    }

    // Metodo para checar se o limite de funcionarios está cheio
    // Retorna falso se a lista de funcionarios estiver cheia, verdadeiro caso ainda haja vaga na lista
    public boolean checarLimiteFuncionarios(Funcionario func) {
        int innerCounterGerencia = this.counterGerencia;
        int innerCounterAlmoxarifado = this.counterAlmoxarifado;
        int innerCounterAtendimento = this.counterAtendimento;
        int innerCounterFinanceiro = this.counterFinanceiro;
        int innerCounterVendas = this.counterVendas;
        int innerCounterGestao = this.counterGestao;

        switch (func.getSetor()) {
            case Setor.GERENCIA -> innerCounterGerencia++;
            case Setor.ATENDIMENTO_AO_CLIENTE -> innerCounterAtendimento++;
            case Setor.GESTAO_DE_PESSOAS -> innerCounterGestao++;
            case Setor.FINANCEIRO -> innerCounterFinanceiro++;
            case Setor.VENDAS -> innerCounterVendas++;
            case Setor.ALMOXARIFADO -> innerCounterAlmoxarifado++;
            default -> throw new RuntimeException("Setor invalido");
        }

        // Checando se cada Setor está cheio
        if (innerCounterGerencia > LIMITE_GERENCIA) {
            return false;
        } else if (innerCounterAtendimento > LIMITE_ATENDIMENTO) {
            return false;
        } else if (innerCounterGestao > LIMITE_GESTAO) {
            return false;
        } else if (innerCounterFinanceiro > LIMITE_FINANCEIRO) {
            return false;
        } else if (innerCounterVendas > LIMITE_VENDAS) {
            return false;
        } else if (innerCounterAlmoxarifado > LIMITE_ALMOXARIFADO) {
            return false;
        } else {
            return true;
        }
    }
}
