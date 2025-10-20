package org.example.categorias.service;

import org.example.categorias.dto.request.CategoriaPatchRequest;
import org.example.categorias.dto.request.CategoriaPostPutRequest;
import org.example.categorias.dto.response.CategoriaDeleteResponse;
import org.example.categorias.dto.response.CategoriaResponse;
import org.example.categorias.exceptions.CategoriaException;

import java.util.List;

public interface CategoriaService {
    public List<CategoriaResponse> getAll();
    public CategoriaResponse getById(Long id);
    public CategoriaResponse save(CategoriaPostPutRequest categoria);
    public CategoriaResponse update(Long id, CategoriaPostPutRequest categoria);
    public CategoriaResponse patch(Long id, CategoriaPatchRequest categoria);
    public CategoriaDeleteResponse delete(Long id);
    public CategoriaResponse findByNombreIgnoreCase(String nombre);
}
