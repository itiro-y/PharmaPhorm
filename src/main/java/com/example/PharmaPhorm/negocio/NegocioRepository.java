package com.example.PharmaPhorm.negocio;

import com.example.PharmaPhorm.negocio.Negocio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NegocioRepository extends JpaRepository<Negocio, Long> {
}