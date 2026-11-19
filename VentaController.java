package com.franquicia.pos.controller;

import com.franquicia.pos.model.Insumo;
import com.franquicia.pos.model.Producto;
import com.franquicia.pos.model.RecetaElemento;
import com.franquicia.pos.repository.InsumoRepository;
import com.franquicia.pos.repository.ProductoRepository;
import com.franquicia.pos.repository.RecetaElementoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@CrossOrigin(origins = "*")
public class VentaController {

    private final ProductoRepository productoRepository;
    private final InsumoRepository insumoRepository;
    private final RecetaElementoRepository recetaElementoRepository;

    public VentaController(ProductoRepository productoRepository,
                           InsumoRepository insumoRepository,
                           RecetaElementoRepository recetaElementoRepository) {
        this.productoRepository = productoRepository;
        this.insumoRepository = insumoRepository;
        this.recetaElementoRepository = recetaElementoRepository;
    }

    public static class ItemVenta {
        public Long productoId;
        public Integer cantidad;
    }

    public static class SolicitudVenta {
        public Long franquiciaId;
        public String cajero;
        public List<ItemVenta> items;
    }

    @PostMapping("/procesar")
    @Transactional
    public ResponseEntity<?> procesarVenta(@RequestBody SolicitudVenta solicitud) {
        if (solicitud.items == null || solicitud.items.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "La venta no contiene ítems"));
        }

        for (ItemVenta item : solicitud.items) {
            Producto producto = productoRepository.findById(item.productoId).orElse(null);
            if (producto == null) continue;

            // Descontar directamente el stock del producto registrado
            int nuevoStockProd = Math.max(0, producto.getStock() - item.cantidad);
            producto.setStock(nuevoStockProd);
            productoRepository.save(producto);
        }

        return ResponseEntity.ok(Map.of("mensaje", "Venta procesada y stock actualizado con éxito"));
    }
}