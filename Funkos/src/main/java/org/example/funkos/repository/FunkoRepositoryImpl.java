package org.example.funkos.repository;

import ch.qos.logback.classic.Logger;
import org.example.funkos.models.Funko;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class FunkoRepositoryImpl implements FunkoRepository {
    private final Logger logger = (Logger) LoggerFactory.getLogger(FunkoRepositoryImpl.class);
    private Long nextId = 0L;
    private final HashMap<Long, Funko> funkos = new HashMap<>();

    @Override
    public List<Funko> getAll() {
        logger.info("Obteniendo todos los funkos...");
        return funkos.values().stream().toList();
    }

    @Override
    public Optional<Funko> getById(Long id) {
        logger.info("Buscando funko por id: " + id);
        return funkos.get(id) == null ? Optional.empty() : Optional.of(funkos.get(id));
    }

    @Override
    public Funko save(Funko funko) {
        logger.info("Guardando funko: " + funko);
        funko.setId(nextId++);
        funkos.put(funko.getId(), funko);
        return funko;
    }

    @Override
    public Optional<Funko> update(Funko funko, Long id) {
        logger.info("Actualizando funko con id: " + id);
        Funko funkoActualizado = funkos.get(id);
        if (funkoActualizado == null) return Optional.empty();

        if (funko.getNombre() != null) funkoActualizado.setNombre(funko.getNombre());
        if (funko.getUuid() != null) funkoActualizado.setUuid(funko.getUuid());
        if (funko.getPrecio() != null) funkoActualizado.setPrecio(funko.getPrecio());
        if (funko.getCategoria() != null) funkoActualizado.setCategoria(funko.getCategoria());
        if (funko.getFechaLanzamiento() != null) funkoActualizado.setFechaLanzamiento(funko.getFechaLanzamiento());

        funkoActualizado.setUpdatedAt(LocalDateTime.now());
        funkos.put(id, funkoActualizado);
        return Optional.of(funkoActualizado);
    }

    @Override
    public Optional<Funko> patch(Funko funko, Long id) {
        logger.info("Actualizando funko con id: " + id);
        Funko funkoActual = funkos.get(id);
        if (funkoActual == null) return Optional.empty();

        if (funko.getUuid() != null) funkoActual.setUuid(funko.getUuid());
        if (funko.getNombre() != null) funkoActual.setNombre(funko.getNombre());
        if (funko.getPrecio() != null) funkoActual.setPrecio(funko.getPrecio());
        if (funko.getCategoria() != null) funkoActual.setCategoria(funko.getCategoria());
        if(funko.getFechaLanzamiento() != null) funkoActual.setFechaLanzamiento(funko.getFechaLanzamiento());
        funkos.put(id, funkoActual);
        return Optional.of(funkoActual);
    }

    @Override
    public Optional<Funko> delete(Long id) {
        logger.info("Eliminando funko con id: " + id);
        Funko funkoEliminado = funkos.remove(id);
        return funkoEliminado == null ? Optional.empty() : Optional.of(funkoEliminado);
    }
}
