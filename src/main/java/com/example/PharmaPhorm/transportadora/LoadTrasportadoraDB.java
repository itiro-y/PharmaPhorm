//package com.example.PharmaPhorm.transportadora;
//
//import com.example.PharmaPhorm.events.TransportadoraInitializedEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//class LoadTrasportadoraDB {
//
//    private static final Logger log = LoggerFactory.getLogger(LoadTrasportadoraDB.class);
//
//    @Bean
//    CommandLineRunner initDatabaseTransportadora(TransportadoraRepository repository) {
//
//        repository.deleteAll();
//        return args -> {
//
//        };
//    }
//}