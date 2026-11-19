package com.franquicia.pos.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "categorias")
public class Categoria implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long franquiciaId;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 10)
    private String icono; // Ej. "☕", "🍔", "🍰"

    public Categoria() {}

    public Categoria(Long franquiciaId, String nombre, String icono) {
        this.franquiciaId = franquiciaId;
        this.nombre = nombre;
        this.icono = icono;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getFranquiciaId() { return franquiciaId; }
    public void setFranquiciaId(Long franquiciaId) { this.franquiciaId = franquiciaId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getIcono() { return icono; }
    public void setIcono(String icono) { this.icono = icono; }
}