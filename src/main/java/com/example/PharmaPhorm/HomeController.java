package com.example.PharmaPhorm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String paginaInicial() {
        String nome = "Jao";
        return "index"; // Procura templates/index.html
    }
}
