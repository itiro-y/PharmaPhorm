package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.funcionario.Funcionario;
import com.example.PharmaPhorm.transportadora.Transportadora;
import com.example.PharmaPhorm.transportadora.TransportadoraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;

import java.util.ArrayList;
import java.util.List;

@Configuration
class LoadNegociosDB {

    private static final Logger log = LoggerFactory.getLogger(LoadNegociosDB.class);

    @Bean
    CommandLineRunner initDatabaseNegocios(NegocioRepository repository, TransportadoraRepository repositoryTransportadora) {
        repository.deleteAll();
        ArrayList<Funcionario> funcionarios = new ArrayList<>();
        funcionarios.add(new Funcionario("Gabriel Fonseca", 22, "masculino", "vendas", 1234.0, 0));
        return args -> {
            ArrayList<String> regioes = new ArrayList<>(List.of("Sul", "Sudeste"));
            log.info("Preloading " + repositoryTransportadora.save(new Transportadora("Transportadora X", regioes)));

            log.info("Preloading " + repository.save(new Negocio("venda", funcionarios, new Transportadora("Transportadora A", new ArrayList<>(List.of("Sul", "Sudeste"))))));
        };
    }
}