package com.example.PharmaPhorm.transportadora;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
class LoadTrasportadoraDB {

    private static final Logger log = LoggerFactory.getLogger(LoadTrasportadoraDB.class);

    @Bean
    CommandLineRunner initDatabaseTransportadora(TransportadoraRepository repository) {
        repository.deleteAll();
        return args -> {
            ArrayList<String> regioes = new ArrayList<>(List.of("Sul", "Sudeste"));
            log.info("Preloading " + repository.save(new Transportadora("Transportadora X", regioes)));
        };
    }
}