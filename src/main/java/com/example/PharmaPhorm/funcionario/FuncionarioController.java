package com.example.PharmaPhorm.funcionario;

import com.example.PharmaPhorm.caixa.CaixaController;
import com.example.PharmaPhorm.caixa.CaixaRepository;
import com.example.PharmaPhorm.funcionario.Exceptions.FuncionarioNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class FuncionarioController {
    private final FuncionarioRepository repository;
    private final CaixaController caixaController;

    FuncionarioController(FuncionarioRepository repository, CaixaController caixaController) {
        this.repository = repository;
        this.caixaController = caixaController;
    }

    @GetMapping("/funcionario")
    List<Funcionario> getFuncionario() {
        return repository.findAll();
    }

    @PostMapping("/funcionario")
    Funcionario addFuncionario(@RequestBody Funcionario funcionario) {
        return repository.save(funcionario);
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
}
