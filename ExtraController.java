package com.franquicia.pos.controller;

import com.franquicia.pos.model.Extra;
import com.franquicia.pos.repository.ExtraRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/extras")
@CrossOrigin(origins = "*")
public class ExtraController {

    private final ExtraRepository extraRepository;

    public ExtraController(ExtraRepository extraRepository) {
        this.extraRepository = extraRepository;
    }

    @GetMapping
    public ResponseEntity<List<Extra>> listarTodos() {
        return ResponseEntity.ok(extraRepository.findAll());
    }

    @GetMapping("/franquicia/{franquiciaId}")
    public ResponseEntity<List<Extra>> listarPorFranquicia(@PathVariable Long franquiciaId) {
        return ResponseEntity.ok(extraRepository.findByFranquiciaIdOrderByIdAsc(franquiciaId));
    }

    @PostMapping
    public ResponseEntity<?> crearOActualizarExtra(@RequestBody Extra extra) {
        try {
            if (extra.getFranquiciaId() == null) extra.setFranquiciaId(1L);
            Extra nuevo = extraRepository.save(extra);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarExtra(@PathVariable Long id) {
        return extraRepository.findById(id)
                .map(e -> {
                    extraRepository.delete(e);
                    return ResponseEntity.ok(Map.of("mensaje", "Extra eliminado"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}