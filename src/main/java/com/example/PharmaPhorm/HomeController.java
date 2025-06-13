package com.example.PharmaPhorm;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String paginaInicial() {
        return "index"; // Procura templates/index.html
    }

    @GetMapping("/funcionarios")
    public String paginaFuncionario() {
       return "funcionarios";
    }

    @GetMapping("/produto")
    public String paginaProduto() {
        return "produto";
    }

}
