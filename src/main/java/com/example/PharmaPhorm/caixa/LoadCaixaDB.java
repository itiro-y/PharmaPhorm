package com.example.PharmaPhorm.caixa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadCaixaDB {

    private static final Logger log = LoggerFactory.getLogger(LoadCaixaDB.class);

    @Bean
    CommandLineRunner initDatabaseCaixa(CaixaRepository caixaRepository) {
        caixaRepository.deleteAll();
        return args -> {
            log.info("Preloading " + caixaRepository.save(new Caixa(200000)));
        };
    }
}
