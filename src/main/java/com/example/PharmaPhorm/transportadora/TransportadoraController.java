package com.example.PharmaPhorm.transportadora;

import com.example.PharmaPhorm.transportadora.Exceptions.TransportadoraNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TransportadoraController {
    private final TransportadoraRepository transportadoraRepository;

    TransportadoraController(TransportadoraRepository transportadoraRepository) {
        this.transportadoraRepository = transportadoraRepository;
    }

    @GetMapping("/transportadora")
    List<Transportadora> getTransportadora() {
        return transportadoraRepository.findAll();
    }

    @PostMapping("/transportadora")
    Transportadora addTransportadora(@RequestBody Transportadora transportadora) {
        return transportadoraRepository.save(transportadora);
    }

    @GetMapping("/transportadora/{id}")
    Transportadora getTransportadora(@PathVariable Long id) {
        return transportadoraRepository.findById(id).orElseThrow(() -> new TransportadoraNotFoundException(id));
    }

    @PutMapping("/transportadora/{id}")
    Transportadora updateTransportadora(@RequestBody Transportadora trans, @PathVariable Long id) {
        return transportadoraRepository.findById(id)
                .map(f -> {
                    f.setNome(trans.getNome());
                    f.setRegioes(trans.getRegioes());
                    return transportadoraRepository.save(f);
                })
                .orElseGet(() -> {
                    return transportadoraRepository.save(trans);
                });
    }

    @DeleteMapping("/transportadora/{id}")
    void deleteTransportadora(@PathVariable Long id) {
        transportadoraRepository.deleteById(id);
    }
}
