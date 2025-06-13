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
    CommandLineRunner initFuncionarioDatabase(FuncionarioRepository repository) {
        return args -> {
            repository.deleteAll();
            log.info("Preloading " + repository.save(new Funcionario("Carlos Alberto", 50, "MASCULINO", "GERENCIA", 5000.00)));

            log.info("Preloading " + repository.save(new Funcionario("João Gouveia", 20, "MASCULINO", "ATENDIMENTO_AO_CLIENTE", 2000.00)));
            log.info("Preloading " + repository.save(new Funcionario("Maria Souza", 28, "FEMININO", "ATENDIMENTO_AO_CLIENTE", 2000.00)));
            log.info("Preloading " + repository.save(new Funcionario("Carlos Pereira", 35, "MASCULINO", "ATENDIMENTO_AO_CLIENTE", 2000.00)));
            log.info("Preloading " + repository.save(new Funcionario("Ana Lima", 26, "FEMININO", "ATENDIMENTO_AO_CLIENTE", 2000.00)));

            log.info("Preloading " + repository.save(new Funcionario("Lucas Almeida", 40, "MASCULINO", "GESTAO_DE_PESSOAS", 3000.00)));
            log.info("Preloading " + repository.save(new Funcionario("Isabela Fernandes", 32, "FEMININO", "GESTAO_DE_PESSOAS", 3000.00)));
            log.info("Preloading " + repository.save(new Funcionario("Rafael Costa", 29, "MASCULINO", "GESTAO_DE_PESSOAS", 3000.00)));
            log.info("Preloading " + repository.save(new Funcionario("Carolina Rocha", 38, "FEMININO", "GESTAO_DE_PESSOAS", 3000.00)));

            log.info("Preloading " + repository.save(new Funcionario("Maria de Lourdes", 20, "FEMININO", "FINANCEIRO", 4000.00)));
            log.info("Preloading " + repository.save(new Funcionario("Paulo Martins", 33, "MASCULINO", "FINANCEIRO", 4000.00)));
            log.info("Preloading " + repository.save(new Funcionario("Renata Gomes", 29, "FEMININO", "FINANCEIRO", 4000.00)));

            log.info("Preloading " + repository.save(new Funcionario("Bruno Silva", 27, "MASCULINO", "VENDAS", 2800.00)));
            log.info("Preloading " + repository.save(new Funcionario("Juliana Costa", 31, "FEMININO", "VENDAS", 2800.00)));
            log.info("Preloading " + repository.save(new Funcionario("Felipe Oliveira", 24, "MASCULINO", "VENDAS", 2800.00)));
            log.info("Preloading " + repository.save(new Funcionario("Carla Mendes", 29, "FEMININO", "VENDAS", 2800.00)));
            log.info("Preloading " + repository.save(new Funcionario("Diego Ramos", 35, "MASCULINO", "VENDAS", 2800.00)));

            log.info("Preloading " + repository.save(new Funcionario("Paula Andrade", 34, "FEMININO", "ALMOXARIFADO", 2200.00)));
            log.info("Preloading " + repository.save(new Funcionario("Marcelo Souza", 41, "MASCULINO", "ALMOXARIFADO", 2200.00)));
            log.info("Preloading " + repository.save(new Funcionario("Fernanda Lima", 28, "FEMININO", "ALMOXARIFADO", 2000.00)));
        };
    }
}
