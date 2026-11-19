package com.franquicia.pos.repository;

import com.franquicia.pos.model.CajaTurno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CajaTurnoRepository extends JpaRepository<CajaTurno, Long> {
    Optional<CajaTurno> findFirstByUsuarioAndFranquiciaIdAndEstadoOrderByIdDesc(
            String usuario, String franquiciaId, String estado);
}