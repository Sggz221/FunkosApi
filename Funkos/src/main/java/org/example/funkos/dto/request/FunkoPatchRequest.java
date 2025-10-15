package org.example.funkos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class FunkoPatchRequest {
    private String nombre;
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "El UUID no tiene un formato valido.")
    private String uuid;
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;
    @Pattern(regexp = "PELICULAS|ANIME|VIDEOJUEGOS", message = "La categoria debe ser PELICULAS, ANIME o VIDEOJUEGOS")
    private String categoria;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha de lanzamiento debe tener formato AAAA-MM-DD")
    private String fechaLanzamiento;
}
