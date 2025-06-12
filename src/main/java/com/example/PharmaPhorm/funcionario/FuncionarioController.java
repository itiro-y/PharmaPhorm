package com.example.PharmaPhorm.funcionario;

import com.example.PharmaPhorm.funcionario.Exceptions.FuncionarioNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class FuncionarioController {
    private final FuncionarioRepository repository;

    FuncionarioController(FuncionarioRepository repository) {
        this.repository = repository;
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
}
