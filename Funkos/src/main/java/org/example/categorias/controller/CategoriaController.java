package org.example.categorias.controller;

import jakarta.validation.Valid;
import org.example.categorias.dto.request.CategoriaPatchRequest;
import org.example.categorias.dto.request.CategoriaPostPutRequest;
import org.example.categorias.dto.response.CategoriaDeleteResponse;
import org.example.categorias.dto.response.CategoriaResponse;
import org.example.categorias.service.CategoriaServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;

@RestController
@RequestMapping({"/categorias/", "/categorias"})
public class CategoriaController {
    private final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
    private final CategoriaServiceImpl categoriaService;

    @Autowired
    public CategoriaController(CategoriaServiceImpl categoriaService) {
        this.categoriaService = categoriaService;
    }

    @GetMapping("")
    public ResponseEntity<List<CategoriaResponse>> getAll() {
        logger.info("Obteniendo categorias");
        return ResponseEntity.ok(categoriaService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaResponse> getById(@PathVariable Long id) {
        logger.info("Obteniendo categoria con id: " + id);
        return ResponseEntity.ok(categoriaService.getById(id));
    }

    @GetMapping({"/nombre/{nombre}", "nombre/{nombre}"})
    public ResponseEntity<CategoriaResponse> getByNombre(@PathVariable String nombre) {
        logger.info("Obteniendo categoria con nombre: " + nombre);
        return ResponseEntity.ok(categoriaService.findByNombreIgnoreCase(nombre));
    }

    @PostMapping("")
    public ResponseEntity<CategoriaResponse> save(@Valid @RequestBody CategoriaPostPutRequest categoria) {
        logger.info("Obteniendo categoria");
        return ResponseEntity.status(HttpStatus.CREATED).body(categoriaService.save(categoria));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaResponse> update(@PathVariable Long id, @Valid @RequestBody CategoriaPostPutRequest categoria) {
        logger.info("Actualizando categoria con id: " + id);
        return ResponseEntity.ok(categoriaService.update(id, categoria));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CategoriaResponse> patch(@PathVariable Long id, @Valid @RequestBody CategoriaPatchRequest categoria) {
        logger.info("Actualizando categoria con id: " + id);
        return ResponseEntity.ok(categoriaService.patch(id, categoria));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CategoriaDeleteResponse> delete(@PathVariable Long id) {
        logger.info("Borrando categoria con id: " + id);
        return ResponseEntity.ok(categoriaService.delete(id));
    }
}
