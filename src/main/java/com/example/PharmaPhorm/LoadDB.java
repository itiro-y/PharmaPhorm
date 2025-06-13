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

import java.util.ArrayList;
import java.util.List;

@Configuration
class LoadDB {

    private static final Logger log = LoggerFactory.getLogger(LoadDB.class);

    @Bean
    CommandLineRunner initDatabaseNegocios(NegocioRepository repositoryNegocio,
                                           TransportadoraRepository repositoryTransportadora,
                                           ProdutoRepository produtoRepository,
                                           ItemNegocioRepository itemNegocioRepository,
                                           FuncionarioRepository funcionarioRepository) {
        repositoryNegocio.deleteAll();
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
                    "NAO_INFORMADO",
                    "ALMOXARIFADO",
                    2700.0)));


            // ---Inicializa Produto---
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "produto1",
                    100.00,
                    200.00,
                    10
            )));
            log.info("preloading "+ produtoRepository.save(new Produto(
                    "produto super",
                    100.00,
                    200.00,
                    10
            )));

            // ---Inicializa itemNegocio---
            log.info("Preloading " + itemNegocioRepository.save(new ItemNegocio(produtoRepository.findAll().get(0), 100)));


            // ---Inicializa transportadora---
            ArrayList<String> regioes = new ArrayList<>(List.of("Sul", "Sudeste"));
            log.info("Preloading " + repositoryTransportadora.save(new Transportadora("Transportadora X", regioes)));


            //Inicializa negocio
            log.info("Preloading " + repositoryNegocio.save(new Negocio("venda", funcionarioRepository.findAll(), new Transportadora("Transportadora A", new ArrayList<>(List.of("Sul", "Sudeste"))))));


        };
    }
}