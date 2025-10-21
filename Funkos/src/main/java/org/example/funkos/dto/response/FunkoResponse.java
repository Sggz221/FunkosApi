package org.example.funkos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.categorias.dto.response.CategoriaResponse;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunkoResponse {
    private Long id;
    private String uuid;
    private String nombre;
    private Double precio;
    private CategoriaResponse categoria;
    private LocalDate fechaLanzamiento;
}
