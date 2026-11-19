package com.franquicia.pos.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/comandas")
@CrossOrigin(origins = "*") // Permite llamadas desde cualquier dispositivo en la red local
public class ComandaController {

    // Memoria en vivo para mantener las comandas activas
    private final Map<String, Map<String, Object>> comandasActivas = new ConcurrentHashMap<>();

    // 1. Guardar o actualizar comanda (enviada desde pos.html o salon.html)
    @PostMapping
    public ResponseEntity<Map<String, Object>> guardarComanda(@RequestBody Map<String, Object> comanda) {
        String id = (String) comanda.get("id");
        if (id == null || id.isEmpty()) {
            id = "comanda_" + System.currentTimeMillis();
            comanda.put("id", id);
        }
        comandasActivas.put(id, comanda);
        return ResponseEntity.ok(comanda);
    }

    // 2. Obtener todas las comandas activas (consultado por la tablet en kds.html)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> obtenerComandas() {
        List<Map<String, Object>> lista = new ArrayList<>(comandasActivas.values());
        return ResponseEntity.ok(lista);
    }

    // 3. Cambiar estado de la comanda (ej. Marcar como "LISTO" desde la tablet de cocina)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<Void> actualizarEstado(@PathVariable String id, @RequestBody Map<String, String> body) {
        if (comandasActivas.containsKey(id)) {
            String nuevoEstado = body.get("estado");
            if ("COMPLETADO".equalsIgnoreCase(nuevoEstado) || "CANCELADO".equalsIgnoreCase(nuevoEstado)) {
                comandasActivas.remove(id); // Elimina de la pantalla de cocina si ya se despachó
            } else {
                comandasActivas.get(id).put("estado", nuevoEstado);
            }
        }
        return ResponseEntity.ok().build();
    }

    // 4. Limpiar comandas de una mesa liberada
    @DeleteMapping("/mesa/{numMesa}")
    public ResponseEntity<Void> liberarComandasMesa(@PathVariable String numMesa) {
        comandasActivas.entrySet().removeIf(entry -> {
            Object mesaObj = entry.getValue().get("numMesa");
            return mesaObj != null && mesaObj.toString().equals(numMesa);
        });
        return ResponseEntity.ok().build();
    }
}