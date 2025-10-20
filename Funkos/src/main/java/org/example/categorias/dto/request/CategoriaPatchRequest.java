package org.example.categorias.dto.request;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaPatchRequest {
    @Pattern(regexp = "^(?!\\s*$).+", message = "El nombre no puede estar vacío")
    private String nombre;
}
