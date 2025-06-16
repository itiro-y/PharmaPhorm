package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.Enum.Status;
import com.example.PharmaPhorm.Enum.Tipo;
import com.example.PharmaPhorm.caixa.Caixa;
import com.example.PharmaPhorm.caixa.CaixaRepository;
import com.example.PharmaPhorm.caixa.Exceptions.SaldoInsuficienteException;
import com.example.PharmaPhorm.itemnegocio.ItemNegocio;
import com.example.PharmaPhorm.negocio.Exceptions.NegocioNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;
import com.example.PharmaPhorm.negocio.NegocioRepository;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NegocioController {
    private final NegocioRepository repository;
    private final CaixaRepository caixaRepository;

    //Injeção de dependência da classe Repository
    NegocioController(NegocioRepository negocioRepository, CaixaRepository caixaRepository) {
        this.repository = negocioRepository;
        this.caixaRepository = caixaRepository;
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

    //Metodo para alterar o status de um negócio para concluido
    @PutMapping("/negocio/concluir/{id}")
    Negocio concluirNegocioByID(@PathVariable Long id ) {

        Negocio negocio = repository.findById(id).orElseThrow(() -> new NegocioNotFoundException(id));
        Caixa caixa = caixaRepository.findAll().getFirst();
        double difCaixa = 0.0;

        if(negocio.getTipo().equals(Tipo.COMPRA)){
            for(ItemNegocio item : negocio.getItemsNegocio()){
                difCaixa += item.getProduto().getValorCompra()*item.getQuantidade();
            }
            caixa.removerValor(difCaixa); //Pode lançar a exceção SaldoInsuficienteException
        }else{
            for(ItemNegocio item : negocio.getItemsNegocio()){
                difCaixa += item.getProduto().getValorVenda()*item.getQuantidade();
            }
            caixa.adicionarValor(difCaixa);
        }
        negocio.setStatus(Status.CONCLUIDO);
        caixaRepository.save(caixa);
        repository.save(negocio);
        return negocio;
    }

    //Metodo para alterar o status de um negócio para cancelado
    @PutMapping("/negocio/cancelar/{id}")
    Negocio cancelarNegocioByID(@PathVariable Long id ) {
//
    }

}
