package org.example.funkos.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.UUID;

@Data
public class FunkoPostPutRequest {
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "El UUID no tiene un formato valido.")
    private String uuid;
    @NotBlank(message = "El nombre no peude ser nulo")
    private String nombre;
    @NotNull(message = "EL precio no puede ser nulo")
    @Min(value = 0, message = "El precio no peude ser negativo")
    private Double precio;
    @NotBlank(message = "Se debe especificar una categoria")
    @Pattern(regexp = "PELICULAS|ANIME|VIDEOJUEGOS", message = "La categoria debe ser PELICULAS, ANIME o VIDEOJUEGOS")
    private String categoria;
    @NotBlank(message = "Se debe especificar una fecha")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha de lanzamiento debe tener formato AAAA-MM-DD")
    private String fechaLanzamiento;
}
