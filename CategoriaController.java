package com.franquicia.pos.controller;

import com.franquicia.pos.model.Categoria;
import com.franquicia.pos.repository.CategoriaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/categorias")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaRepository categoriaRepository;

    public CategoriaController(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        return ResponseEntity.ok(categoriaRepository.findAll());
    }

    @GetMapping("/franquicia/{franquiciaId}")
    public ResponseEntity<List<Categoria>> listarPorFranquicia(@PathVariable Long franquiciaId) {
        return ResponseEntity.ok(categoriaRepository.findByFranquiciaIdOrderByIdAsc(franquiciaId));
    }

    @PostMapping
    public ResponseEntity<?> crearCategoria(@RequestBody Categoria categoria) {
        try {
            if (categoria.getFranquiciaId() == null) categoria.setFranquiciaId(1L);
            Categoria nueva = categoriaRepository.save(categoria);
            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarCategoria(@PathVariable Long id) {
        return categoriaRepository.findById(id)
                .map(c -> {
                    categoriaRepository.delete(c);
                    return ResponseEntity.ok(Map.of("mensaje", "Categoría eliminada"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}