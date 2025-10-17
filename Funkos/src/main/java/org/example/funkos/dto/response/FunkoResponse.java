package org.example.funkos.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class FunkoResponse {
    private final Long id;
    private final String uuid;
    private final String nombre;
    private final Double precio;
    private final String categoria;
    private final LocalDate fechaLanzamiento;
}
