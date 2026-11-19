package com.franquicia.pos.repository;

import com.franquicia.pos.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    List<Categoria> findByFranquiciaIdOrderByIdAsc(Long franquiciaId);
}