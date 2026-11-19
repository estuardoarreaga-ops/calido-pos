package com.franquicia.pos.repository;

import com.franquicia.pos.model.Extra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExtraRepository extends JpaRepository<Extra, Long> {
    List<Extra> findByFranquiciaIdOrderByIdAsc(Long franquiciaId);
}