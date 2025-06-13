package com.example.PharmaPhorm.produto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initProdutoDatabase(ProdutoRepository repository) {
        repository.deleteAll();
        return args ->{
            log.info("preloading " + repository.save(new Produto("Dipirona 500mg", 1.50, 3.00, 100)));
            log.info("preloading " + repository.save(new Produto("Paracetamol 750mg", 1.80, 3.50, 120)));
            log.info("preloading " + repository.save(new Produto("Ibuprofeno 400mg", 2.20, 4.50, 80)));
            log.info("preloading " + repository.save(new Produto("Amoxicilina 500mg", 3.00, 6.00, 60)));
            log.info("preloading " + repository.save(new Produto("Espironolactona 25mg", 2.50, 6.20, 35)));
            log.info("preloading " + repository.save(new Produto("Omeprazol 20mg", 2.00, 4.00, 90)));
            log.info("preloading " + repository.save(new Produto("Loratadina 10mg", 1.60, 3.20, 75)));
            log.info("preloading " + repository.save(new Produto("Simeticona 125mg", 1.90, 3.80, 50)));
            log.info("preloading " + repository.save(new Produto("Ácido Acetilsalicílico 100mg", 1.40, 2.80, 110)));
            log.info("preloading " + repository.save(new Produto("Cloridrato de Sertralina 50mg", 4.00, 8.00, 30)));
            log.info("preloading " + repository.save(new Produto("Metformina 850mg", 2.50, 5.00, 70)));
        };
    }
}
