package org.example.funkos.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Funko {
    private Long id;
    private UUID uuid;
    private String nombre;
    private Double precio;
    private Categoria categoria;
    private LocalDate fechaLanzamiento;
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();
}
