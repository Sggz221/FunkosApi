package org.example.categorias.service;

import lombok.val;
import org.example.categorias.dto.request.CategoriaPatchRequest;
import org.example.categorias.dto.request.CategoriaPostPutRequest;
import org.example.categorias.dto.response.CategoriaDeleteResponse;
import org.example.categorias.dto.response.CategoriaResponse;
import org.example.categorias.exceptions.CategoriaException;
import org.example.categorias.mappers.CategoriaMapper;
import org.example.categorias.models.Categoria;
import org.example.categorias.repositories.CategoriaRepository;
import org.example.funkos.repository.FunkoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
@CacheConfig(cacheNames = "categorias")
public class CategoriaServiceImpl implements CategoriaService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    private final CategoriaRepository categoriaRepository;
    private final FunkoRepository funkoRepository;

    @Autowired
    public CategoriaServiceImpl(CategoriaRepository categoriaRepository, FunkoRepository funkoRepository) {
        this.categoriaRepository = categoriaRepository;
        this.funkoRepository = funkoRepository;
        initData();
    }

    private void initData(){
        val categoria1 = new Categoria(null, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        val categoria2 = new Categoria(null, "VIDEOJUEGOS", LocalDateTime.now(), LocalDateTime.now());
        val categoria3 = new Categoria(null, "PELICULAS", LocalDateTime.now(), LocalDateTime.now());
        categoriaRepository.save(categoria1);
        categoriaRepository.save(categoria2);
        categoriaRepository.save(categoria3);
    }

    @Override
    public List<CategoriaResponse> getAll() {
        logger.info("Obteniendo todas las categorias");
        return categoriaRepository.findAll().stream().map(CategoriaMapper::toResponse).toList();
    }

    @Override
    @Cacheable(key = "#id")
    public CategoriaResponse getById(Long id) {
        logger.info("Buscando categoria por id: " + id);
        val categoria = categoriaRepository.findById(id).orElseThrow(
                () -> new CategoriaException.NotFoundException("No se encontro categoria con id: " + id)
        );

        return CategoriaMapper.toResponse(categoria);
    }

    @Override
    public CategoriaResponse save(CategoriaPostPutRequest categoria) {
        logger.info("Guardando categoria: " + categoria);
        val guardada = categoriaRepository.save(CategoriaMapper.postPutToModel(categoria));
        guardada.setCreatedAt(LocalDateTime.now());
        guardada.setUpdatedAt(LocalDateTime.now());
        return CategoriaMapper.toResponse(guardada);
    }

    @Override
    @CachePut(key = "#id")
    public CategoriaResponse update(Long id, CategoriaPostPutRequest categoria) {
        logger.info("Actualizando categoria con id: " + id);
        val nombre = categoriaRepository.findByNombreIgnoreCase(categoria.getNombre()); // Si el nombre nuevo que se le quiere poner a esta categoria ya lo tiene otra hay que devolver un error
        if(nombre != null && nombre.getId().equals(id)) throw new CategoriaException.ConflictException("El nombre elegido para esta categoria ya lo tiene otra categoria");
        val actualizada = categoriaRepository.findById(id).orElseThrow(() -> new CategoriaException.NotFoundException("No se encontro categoria con id: " + id));
        actualizada.setId(id);
        actualizada.setNombre(categoria.getNombre());
        actualizada.setUpdatedAt(LocalDateTime.now());
        return CategoriaMapper.toResponse(categoriaRepository.save(actualizada));
    }

    @Override
    @CachePut(key = "#id")
    public CategoriaResponse patch(Long id, CategoriaPatchRequest categoria) {
        logger.info("Actualizando (patch) categoria con id: " + id);

        val actualizada = categoriaRepository.findById(id).orElseThrow(() -> new CategoriaException.NotFoundException("No se encontro categoria con id: " + id));
        val nombre = categoriaRepository.findByNombreIgnoreCase(categoria.getNombre());
        if (nombre != null) { // Si el nombre nuevo que se le quiere poner a esta categoria ya lo tiene otra hay que devolver un error
            if (nombre.getNombre() != null && nombre.getId().equals(id)) throw new CategoriaException.ConflictException("El nombre elegido para esta categoria ya lo tiene otra categoria");
            funkoRepository.actualizarCategorias(CategoriaMapper.patchToModel(categoria), actualizada); // Se actualiza la categoria de los funkos
            actualizada.setNombre(categoria.getNombre()); // Se actualiza el nombre de la categoria
        }
        actualizada.setUpdatedAt(LocalDateTime.now());
        return CategoriaMapper.toResponse(categoriaRepository.save(actualizada));
    }

    @Override
    public CategoriaDeleteResponse delete(Long id) {
        logger.info("Eliminando categoria con id: " + id);
        val borrada = categoriaRepository.findById(id).orElseThrow(
                () -> new CategoriaException.NotFoundException("No se encontro categoria con id: " + id)
        );
        val funkos = funkoRepository.findByCategoria(borrada); // No se puede borrar la categoria si hay funkos que estan en ella
        if (!funkos.isEmpty()) throw new CategoriaException.ConflictException("Esta categoria tiene recursos asociados");
        categoriaRepository.delete(borrada);
        return new CategoriaDeleteResponse("Categoria borrada correctamente", CategoriaMapper.toResponse(borrada));
    }

    @Override
    public CategoriaResponse findByNombreIgnoreCase(String nombre) {
        logger.info("Buscando categoria por nombre: " + nombre);
        val categoria = categoriaRepository.findByNombreIgnoreCase(nombre);
        if (categoria == null) throw new CategoriaException.NotFoundException("No se encontro categoria con nombre: " + nombre);
        return CategoriaMapper.toResponse(categoria);
    }
}
