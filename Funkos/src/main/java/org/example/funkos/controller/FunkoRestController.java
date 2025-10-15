package org.example.funkos.controller;


import jakarta.validation.Valid;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoDeleteResponse;
import org.example.funkos.models.Funko;
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
    public FunkoRestController(FunkoServiceImpl funkoService) { this.funkoService = funkoService; }

    @GetMapping("")
    public ResponseEntity<List<Funko>> getAll() {
        logger.info("Obteniendo funkos...");
        return ResponseEntity.ok(funkoService.getAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Funko> getById(@PathVariable Long id) {
        logger.info("Obteniendo funko con id: " + id);
        return ResponseEntity.ok(funkoService.getById(id));
    }

    @PostMapping("")
    public ResponseEntity<Funko> save(@Valid @RequestBody FunkoPostPutRequest funko) {
        logger.info("Guardando funko: " + funko);
        return ResponseEntity.status(HttpStatus.CREATED).body(funkoService.save(funko));
    }

    @PutMapping("{id}")
    public ResponseEntity<Funko> update(@PathVariable Long id, @Valid @RequestBody FunkoPostPutRequest funko) {
        logger.info("Actualizando funko con id: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(funkoService.save(funko));
    }

    @PatchMapping("{id}")
    public ResponseEntity<Funko> patch(@PathVariable Long id, @Valid @RequestBody FunkoPatchRequest funko) {
        logger.info("Actualizando funko con id: " + id);
        return ResponseEntity.status(HttpStatus.OK).body(funkoService.patch(funko, id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<FunkoDeleteResponse> delete(@PathVariable Long id) {
        logger.info("Eliminando funko con id: " + id);
        return ResponseEntity.ok(new FunkoDeleteResponse("Funko eliminado correctamente", funkoService.delete(id)));
    }
}
