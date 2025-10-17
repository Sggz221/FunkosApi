package org.example.funkos.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Funko {
    @Id // Anotacion necesaria para definir la PK de la tabla
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Id autoincremental
    private Long id;

    @Column(nullable = false)
    private UUID uuid;

    @Column(nullable = false)
    @Size(min = 1, max = 50, message = "El nombre debe ser como maximo 50 caracteres y no puede estar vacio")
    private String nombre;

    @Column(nullable = false)
    @Min(value = 0, message = "El precio no peude ser nulo")
    private Double precio;

    @Column(nullable = false)
    private String categoria;

    @Column(name = "fecha_lanzamiendo", nullable = false)
    private LocalDate fechaLanzamiento;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
