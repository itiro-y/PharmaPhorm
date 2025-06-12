package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.negocio.Exceptions.NegocioNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NegocioController {
    private final NegocioRepository repository;

    //Injeção de dependência da classe Repository
    NegocioController(NegocioRepository negocioRepository) {
        this.repository = negocioRepository;
    }

    @GetMapping("/negocio")
    List<Negocio> getNegocio() {
        return repository.findAll();
    }

    @PostMapping("/negocio")
    Negocio addNegocio(@RequestBody Negocio negocio) {
        return repository.save(negocio);
    }

    @GetMapping("/negocio/{id}")
    Negocio getFNegociosByID(@PathVariable Long id ) {
        return repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
    }

}
