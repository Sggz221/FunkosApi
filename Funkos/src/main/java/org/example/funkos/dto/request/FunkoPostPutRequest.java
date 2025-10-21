package org.example.funkos.dto.request;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.categorias.dto.request.CategoriaPostPutRequest;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FunkoPostPutRequest {
    private Long id;
    @Pattern(regexp = "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$", message = "El UUID no tiene un formato valido.")
    private String uuid;
    @NotBlank(message = "El nombre no peude ser nulo")
    private String nombre;
    @NotNull(message = "EL precio no puede ser nulo")
    @Min(value = 0, message = "El precio no peude ser negativo")
    private Double precio;
    @NotNull(message = "Se debe especificar una categoria")
    private CategoriaPostPutRequest categoria;
    @NotBlank(message = "Se debe especificar una fecha")
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "La fecha de lanzamiento debe tener formato AAAA-MM-DD")
    private String fechaLanzamiento;

    public FunkoPostPutRequest(Long id, UUID uuid, @Size(min = 1, max = 50, message = "El nombre debe ser como maximo 50 caracteres y no puede estar vacio") String nombre, String categoria, LocalDate fechaLanzamiento) {
    }
}
