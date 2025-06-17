package com.example.PharmaPhorm.transportadora;

import com.example.PharmaPhorm.negocio.Negocio;
import com.example.PharmaPhorm.negocio.NegocioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors; // ✅ Import necessário para o .collect()

@RestController
@RequestMapping("/transportadora")
@CrossOrigin(origins = "*")
public class TransportadoraController {

    private final TransportadoraRepository repository;
    private final NegocioRepository negocioRepository;

    public TransportadoraController(TransportadoraRepository repository, NegocioRepository negocioRepository) {
        this.repository = repository;
        this.negocioRepository = negocioRepository;
    }

    @GetMapping
    public List<Transportadora> listarTodas() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Transportadora buscarPorId(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transportadora não encontrada"));
    }

    @PostMapping
    public Transportadora criarTransportadora(@RequestBody Transportadora novaTransportadora) {
        novaTransportadora.setAtivo(true);
        return repository.save(novaTransportadora);
    }

    @PutMapping("/{id}")
    public Transportadora atualizarTransportadora(@PathVariable Long id, @RequestBody Transportadora transportadoraAtualizada) {
        return repository.findById(id)
                .map(transportadora -> {
                    transportadora.setNome(transportadoraAtualizada.getNome());
                    transportadora.setRegioes(transportadoraAtualizada.getRegioes());
                    transportadora.setAtivo(transportadoraAtualizada.isAtivo());
                    return repository.save(transportadora);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Transportadora não encontrada"));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public void deletarTransportadora(@PathVariable Long id) {
        Transportadora transportadora = buscarPorId(id);
        List<Negocio> negociosAssociados = transportadora.getNegocios();

        if (negociosAssociados != null && !negociosAssociados.isEmpty()) {

            List<Negocio> negociosAtivos = negociosAssociados.stream()
                    .filter(negocio -> negocio.getStatus() == com.example.PharmaPhorm.Enum.Status.ABERTO)
                    // ✅ ALTERAÇÃO PARA MAIOR COMPATIBILIDADE
                    .collect(Collectors.toList());

            if (!negociosAtivos.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Não é possível excluir. Transportadora em uso em " + negociosAtivos.size() + " negócio(s) em aberto.");
            }

            for (Negocio negocioFinalizado : negociosAssociados) {
                negocioFinalizado.setTransportadora(null);
            }
            negocioRepository.saveAll(negociosAssociados);
        }

        repository.delete(transportadora);
    }
}