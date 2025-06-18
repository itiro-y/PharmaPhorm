package com.example.PharmaPhorm;

import com.example.PharmaPhorm.funcionario.Funcionario;
import com.example.PharmaPhorm.funcionario.FuncionarioRepository;
import com.example.PharmaPhorm.itemnegocio.ItemNegocio;
import com.example.PharmaPhorm.itemnegocio.ItemNegocioRepository;
import com.example.PharmaPhorm.negocio.Negocio;
import com.example.PharmaPhorm.negocio.NegocioRepository;
import com.example.PharmaPhorm.produto.Produto;
import com.example.PharmaPhorm.produto.ProdutoRepository;
import com.example.PharmaPhorm.transportadora.Transportadora;
import com.example.PharmaPhorm.transportadora.TransportadoraRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

@Configuration
class LoadDB {

    private static final Logger log = LoggerFactory.getLogger(LoadDB.class);

    @Bean
    CommandLineRunner initDatabaseNegocios(NegocioRepository repositoryNegocio,
                                           TransportadoraRepository repositoryTransportadora,
                                           ProdutoRepository produtoRepository,
                                           ItemNegocioRepository itemNegocioRepository,
                                           FuncionarioRepository funcionarioRepository,
                                           JdbcTemplate jdbcTemplate, NegocioRepository negocioRepository) {

        jdbcTemplate.execute("DELETE FROM negocio_funcionario");
        itemNegocioRepository.deleteAll();
        funcionarioRepository.deleteAll();
        repositoryNegocio.deleteAll();
        repositoryTransportadora.deleteAll();
        produtoRepository.deleteAll();

        return args -> {

            // ---Inicializa Funcionarios---
            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Joao Fonseca",
                    42,
                    "MASCULINO",
                    "GERENCIA",
                    3000.0)));

            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Maria Silva",
                    28,
                    "FEMININO",
                    "GESTAO_DE_PESSOAS",
                    2200.0)));

            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Jennifer Ortiz",
                    30,
                    "FEMININO",
                    "FINANCEIRO",
                    2500.0)));

            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Matheus Algorta",
                    20,
                    "MASCULINO",
                    "VENDAS",
                    1800.0)));

            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Felipe Lopes",
                    22,
                    "MASCULINO",
                    "VENDAS",
                    2700.0)));
            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Gabriel Silva",
                    32,
                    "NAO_INFORMADO",
                    "FINANCEIRO",
                    3000.0)));
            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Pedro Nunes",
                    29,
                    "MASCULINO",
                    "GESTAO_DE_PESSOAS",
                    2700.0)));
            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Isadora Batista",
                    21,
                    "FEMININO",
                    "ATENDIMENTO_AO_CLIENTE",
                    1900.0)));
            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Katia Yamashida",
                    23,
                    "FEMININO",
                    "ATENDIMENTO_AO_CLIENTE",
                    2000.0)));
            log.info("Preloading " + funcionarioRepository.save(new Funcionario("Thiago Junior",
                    23,
                    "MASCULINO",
                    "ATENDIMENTO_AO_CLIENTE",
                    2000.0)));


            // ---Inicializa Produto---
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Dipirona ",
                    1.20,
                    3.90,
                    100
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Omeprazol",
                    3.80,
                    11.90,
                    50
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Amoxicilina  ",
                    2.50,
                    9.50,
                    60
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Rivotril (Clonazepam)",
                    4.30,
                    14.00,
                    30
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Neosaldina ",
                    5.00,
                    17.90,
                    10
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Fluoxetina ",
                    2.20,
                    8.50,
                    25
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Buscopan ",
                    1.80,
                    6.90,
                    100
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Produto Super",
                    5.00,
                    10.00,
                    90
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Fluoxetina",
                    1.80,
                    6.90,
                    75
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "Losartana",
                    2.90,
                    10.50,
                    80
            )));

            // ---Inicializa transportadora---
            log.info("Preloading " + repositoryTransportadora.save(new Transportadora("Jadlog", new ArrayList<>(List.of("Sul", "Sudeste")))));
            log.info("Preloading " + repositoryTransportadora.save(new Transportadora("Transportadora Braspress", new ArrayList<>(List.of("Norte", "Sul")))));
            log.info("Preloading " + repositoryTransportadora.save(new Transportadora("Total Express", new ArrayList<>(List.of("Nordeste", "Sul")))));
            log.info("Preloading " + repositoryTransportadora.save(new Transportadora("Correios (SEDEX e PAC)", new ArrayList<>(List.of("Norte", "Nordeste", "Sul")))));

            //Inicializa negocio
            log.info("Preloading " + repositoryNegocio.save(new Negocio("venda", new HashSet<>(funcionarioRepository.findAll()), repositoryTransportadora.findAll().getFirst())));

            // ---Inicializa itemNegocio---
            log.info("Preloading " + itemNegocioRepository.save(new ItemNegocio(produtoRepository.findAll().get(0), repositoryNegocio.findAll().getFirst(),100)));
            log.info("Preloading " + itemNegocioRepository.save(new ItemNegocio(produtoRepository.findAll().get(1), repositoryNegocio.findAll().getFirst(),100)));

            List<Funcionario> lista_funcionarios = funcionarioRepository.findAll();
            for(Funcionario f : lista_funcionarios) {
                f.setNegociosParticipantes(new HashSet<>(new ArrayList<>(Arrays.asList(repositoryNegocio.findAll().getFirst()))));
                funcionarioRepository.save(f);
            }

            negocioRepository.findAll().getFirst().setItemsNegocio(new HashSet<>(itemNegocioRepository.findAll()));

        };
    }
}