package com.franquicia.pos.controller;

import com.franquicia.pos.model.Producto;
import com.franquicia.pos.repository.ProductoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/productos")
@CrossOrigin(origins = "*")
public class ProductoController {

    private final ProductoRepository productoRepository;

    public ProductoController(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoRepository.findAll());
    }

    @GetMapping("/franquicia/{franquiciaId}")
    public ResponseEntity<List<Producto>> listarPorFranquicia(@PathVariable Long franquiciaId) {
        return ResponseEntity.ok(productoRepository.findByFranquiciaId(franquiciaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> crearProducto(@RequestBody Producto producto) {
        try {
            if (producto.getFranquiciaId() == null) producto.setFranquiciaId(1L);
            if (producto.getDisponible() == null) producto.setDisponible(true);
            Producto nuevo = productoRepository.save(producto);
            return ResponseEntity.ok(nuevo);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setNombre(producto.getNombre());
                    p.setTamano(producto.getTamano());
                    p.setCodigoBarras(producto.getCodigoBarras());
                    p.setPrecioVenta(producto.getPrecioVenta());
                    p.setCosto(producto.getCosto());
                    p.setStock(producto.getStock());
                    p.setCategoria(producto.getCategoria());
                    if (producto.getDisponible() != null) p.setDisponible(producto.getDisponible());
                    Producto actualizado = productoRepository.save(p);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PatchMapping("/{id}/disponible")
    public ResponseEntity<?> cambiarDisponibilidad(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(p -> {
                    p.setDisponible(!Boolean.TRUE.equals(p.getDisponible()));
                    productoRepository.save(p);
                    return ResponseEntity.ok(p);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarProducto(@PathVariable Long id) {
        return productoRepository.findById(id)
                .map(p -> {
                    productoRepository.delete(p);
                    return ResponseEntity.ok(Map.of("mensaje", "Producto eliminado correctamente"));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}