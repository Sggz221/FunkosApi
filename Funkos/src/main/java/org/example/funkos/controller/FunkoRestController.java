package org.example.funkos.controller;

import jakarta.validation.Valid;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoDeleteResponse;
import org.example.funkos.dto.response.FunkoResponse;
import org.example.funkos.service.FunkoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping({"/funkos", "/funkos/"})
public class FunkoRestController {
    private final Logger logger = Logger.getLogger(FunkoRestController.class.getName());
    private final FunkoServiceImpl funkoService;

    @Autowired
    public FunkoRestController(FunkoServiceImpl funkoService) {
        this.funkoService = funkoService;
    }

    @GetMapping("")
    public ResponseEntity<List<FunkoResponse>> getAll() {
        logger.info("Obteniendo todos los Funkos...");
        return ResponseEntity.ok(funkoService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<FunkoResponse> getById(@PathVariable Long id) {
        logger.info("Obteniendo Funko con id: " + id);
        return ResponseEntity.ok(funkoService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<FunkoResponse> save(@Valid @RequestBody FunkoPostPutRequest funko) {
        logger.info("Guardando Funko: " + funko);
        FunkoResponse saved = funkoService.save(funko);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping("{id}")
    public ResponseEntity<FunkoResponse> update(@PathVariable Long id, @Valid @RequestBody FunkoPostPutRequest funko) {
        logger.info("Actualizando Funko con id: " + id);
        FunkoResponse updated = funkoService.update(funko, id);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("{id}")
    public ResponseEntity<FunkoResponse> patch(@PathVariable Long id, @Valid @RequestBody FunkoPatchRequest funko) {
        logger.info("Haciendo patch al Funko con id: " + id);
        FunkoResponse patched = funkoService.patch(funko, id);
        return ResponseEntity.ok(patched);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<FunkoDeleteResponse> delete(@PathVariable Long id) {
        logger.info("Eliminando Funko con id: " + id);
        FunkoDeleteResponse deleted = funkoService.delete(id);
        return ResponseEntity.ok(deleted);
    }

    // Metodos Extra
    @GetMapping({"nombre/{nombre}", "/nombre/{nombre}"})
    public ResponseEntity<List<FunkoResponse>> findByNombreContainingIgnoreCase(@PathVariable String nombre) {
        logger.info("Buscando Funkos con por nombre: " + nombre);
        return ResponseEntity.ok(funkoService.findByNombreContainingIgnoreCase(nombre));
    }

    @GetMapping({"precio/{precio}", "/precio/{precio}"})
    public ResponseEntity<List<FunkoResponse>> findByPrecioLessThan(@PathVariable double precio) {
        logger.info("Buscando Funkos con precio: " + precio);
        return ResponseEntity.ok(funkoService.findByPrecioLessThan(precio));
    }

    @GetMapping({"categoria/{categoria}", "/categoria/{categoria}"})
    public ResponseEntity<List<FunkoResponse>> findByCategoriaName(@PathVariable String nombre) {
        logger.info("Buscando Funkos con categoria: " + nombre);
        return ResponseEntity.ok(funkoService.findByCategoriaName(nombre));
    }

    @GetMapping({"uuid/{uuid}", "/uuid/{uuid}"})
    public ResponseEntity<FunkoResponse> findByUuid(@PathVariable String uuid) {
        logger.info("Buscando Funko con uuid: " + uuid);
        return ResponseEntity.ok(funkoService.findByUuid(uuid));
    }
}
