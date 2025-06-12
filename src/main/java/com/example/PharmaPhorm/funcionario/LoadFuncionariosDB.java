//package com.example.PharmaPhorm.funcionario;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//class LoadFuncionariosDB {
//
//    private static final Logger log = LoggerFactory.getLogger(LoadFuncionariosDB.class);
//
//    @Bean
//    CommandLineRunner initDatabase(FuncionarioRepository repository) {
//
//        return args -> {
//            log.info("Preloading " + repository.save(new Funcionario("Joao", 22, "masculino", "gerencia", 2000.0)));
//        };
//    }
//}