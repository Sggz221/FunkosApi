package org.example.categorias.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaPostPutRequest {
    private Long id;
    @Pattern(regexp = "^(?!\\s*$).+", message = "El nombre no puede estar vacío")
    private String nombre;
}
