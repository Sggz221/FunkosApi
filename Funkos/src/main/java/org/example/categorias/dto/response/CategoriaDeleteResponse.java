package org.example.categorias.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.categorias.models.Categoria;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDeleteResponse {
    private String mensaje;
    private CategoriaResponse deleted;
}
