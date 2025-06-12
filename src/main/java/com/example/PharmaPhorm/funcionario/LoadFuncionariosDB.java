package com.example.PharmaPhorm.funcionario;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadFuncionariosDB {

    private static final Logger log = LoggerFactory.getLogger(LoadFuncionariosDB.class);

    @Bean
    CommandLineRunner initDatabaseFuncionario(FuncionarioRepository repository) {
        repository.deleteAll();
        return args -> {
            log.info("Preloading " + repository.save(new Funcionario("Joao Fonseca",
                                                                     42,
                                                                     "MASCULINO",
                                                                     "GERENCIA",
                                                                     3000.0)));

            log.info("Preloading " + repository.save(new Funcionario("Maria Silva",
                                                                     28,
                                                                     "FEMININO",
                                                                     "GESTAO_DE_PESSOAS",
                                                                     2200.0)));

            log.info("Preloading " + repository.save(new Funcionario("Jennifer Ortiz",
                                                                     30,
                                                                     "FEMININO",
                                                                     "FINANCEIRO",
                                                                     2500.0)));

            log.info("Preloading " + repository.save(new Funcionario("Matheus Algorta",
                                                                     20,
                                                                     "MASCULINO",
                                                                     "VENDAS",
                                                                     1800.0)));

            log.info("Preloading " + repository.save(new Funcionario("Felipe Lopes",
                                                                     22,
                                                                     "NAO_INFORMADO",
                                                                     "ALMOXARIFADO",
                                                                     2700.0)));
        };
    }
}