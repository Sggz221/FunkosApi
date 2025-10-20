package org.example.categorias.mappers;


import org.example.categorias.dto.request.CategoriaPatchRequest;
import org.example.categorias.dto.request.CategoriaPostPutRequest;
import org.example.categorias.dto.response.CategoriaResponse;
import org.example.categorias.models.Categoria;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoResponse;
import org.example.funkos.models.Funko;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class CategoriaMapper {

    public static Categoria postPutToModel(CategoriaPostPutRequest dto) {
        Categoria categoria = new Categoria();
        categoria.setNombre(dto.getNombre());
        categoria.setCreatedAt(LocalDateTime.now());
        categoria.setUpdatedAt(LocalDateTime.now());
        return categoria;
    }

    public static Categoria patchToModel(CategoriaPatchRequest dto) {
        Categoria categoria = new Categoria();
        if (dto.getNombre() != null) categoria.setNombre(dto.getNombre());
        categoria.setCreatedAt(LocalDateTime.now());
        categoria.setUpdatedAt(LocalDateTime.now());
        return categoria;
    }

    public static CategoriaPostPutRequest toPostPut(Categoria categoria) {
        return new CategoriaPostPutRequest(
                categoria.getNombre()
        );
    }

    public static CategoriaResponse toResponse(Categoria categoria) {
        return new CategoriaResponse(
                categoria.getId(),
                categoria.getNombre()
        );
    }
}