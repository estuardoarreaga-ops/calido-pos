package com.franquicia.pos.controller;

import com.franquicia.pos.model.Mesa;
import com.franquicia.pos.repository.MesaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    private final MesaRepository mesaRepository;

    public MesaController(MesaRepository mesaRepository) {
        this.mesaRepository = mesaRepository;
    }

    // 1. OBTENER TODAS LAS MESAS (Soporta /api/mesas y /api/mesas/franquicia/{id})
    @GetMapping
    public ResponseEntity<List<Mesa>> obtenerTodas() {
        return ResponseEntity.ok(mesaRepository.findAll());
    }

    @GetMapping("/franquicia/{franquiciaId}")
    public ResponseEntity<List<Mesa>> listarMesas(@PathVariable Long franquiciaId) {
        return ResponseEntity.ok(mesaRepository.findByFranquiciaIdOrderByIdAsc(franquiciaId));
    }

    // 2. CREAR NUEVA MESA Y GUARDAR EN LA BASE DE DATOS
    @PostMapping
    public ResponseEntity<?> crearMesa(@RequestBody Map<String, Object> payload) {
        try {
            String numeroMesa = payload.get("numeroMesa") != null ? payload.get("numeroMesa").toString() : "Mesa";
            Long franquiciaId = payload.get("franquiciaId") != null ? Long.parseLong(payload.get("franquiciaId").toString()) : 1L;
            Integer capacidad = payload.get("capacidad") != null ? Integer.parseInt(payload.get("capacidad").toString()) : 4;
            String estado = payload.get("estado") != null ? payload.get("estado").toString() : "LIBRE";

            Mesa nueva = new Mesa(franquiciaId, numeroMesa, capacidad, estado);
            mesaRepository.save(nueva);

            return ResponseEntity.ok(nueva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear mesa: " + e.getMessage());
        }
    }

    // 3. CAMBIAR ESTADO DE MESA (LIBRE -> OCUPADA -> CUENTA_PEDIDA)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstadoMesa(@PathVariable Long id, @RequestBody Map<String, String> payload) {
        String nuevoEstado = payload.get("estado");
        return mesaRepository.findById(id)
                .map(mesa -> {
                    mesa.setEstado(nuevoEstado);
                    mesaRepository.save(mesa);
                    return ResponseEntity.ok(mesa);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. ELIMINAR MESA
    @DeleteMapping("/{numeroMesa}")
    public ResponseEntity<?> eliminarMesa(@PathVariable String numeroMesa) {
        List<Mesa> todas = mesaRepository.findAll();
        for (Mesa m : todas) {
            if (m.getNumeroMesa() != null && m.getNumeroMesa().equalsIgnoreCase(numeroMesa)) {
                mesaRepository.delete(m);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}