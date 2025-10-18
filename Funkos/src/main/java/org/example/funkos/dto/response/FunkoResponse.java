package org.example.funkos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunkoResponse {
    private Long id;
    private String uuid;
    private String nombre;
    private Double precio;
    private String categoria;
    private LocalDate fechaLanzamiento;
}
