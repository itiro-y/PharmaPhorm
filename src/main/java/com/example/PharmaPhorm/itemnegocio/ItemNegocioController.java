package com.example.PharmaPhorm.itemnegocio;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ItemNegocioController {

    private final ItemNegocioRepository itemNegocioRepository;

    public ItemNegocioController(ItemNegocioRepository itemNegocioRepository) {
        this.itemNegocioRepository = itemNegocioRepository;
    }

    @GetMapping("/itemnegocio")
    List<ItemNegocio> getItemNegocio() {
        return itemNegocioRepository.findAll();
    }

}
