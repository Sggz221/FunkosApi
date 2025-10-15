package org.example.funkos.service;

import ch.qos.logback.classic.Logger;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.exceptions.FunkoException;
import org.example.funkos.mappers.FunkoMapper;
import org.example.funkos.models.Funko;
import org.example.funkos.repository.FunkoRepositoryImpl;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FunkoServiceImpl implements FunkoService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(FunkoServiceImpl.class);
    private final FunkoRepositoryImpl repository;

    @Autowired
    public FunkoServiceImpl(FunkoRepositoryImpl repository) { this.repository = repository; }

    @Override
    public List<Funko> getAll() {
        logger.info("Devolviendo todos los Funkos");
        return repository.getAll();
    }

    @Override
    @CachePut(value = {"funkos"}, key = "#id")
    public Funko getById(Long id) {
        logger.info("Buscando funko por id: " + id);
        var funko = repository.getById(id);
        if (funko.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko con id" + id);
        return funko.get();
    }

    @Override
    public Funko save(FunkoPostPutRequest funko) {
        logger.info("Guardando funko: " + funko);
        return repository.save(FunkoMapper.postPutToModel(funko));
    }

    @Override
    @CachePut(value = "funkos", key = "#id")
    public Funko update(FunkoPostPutRequest funko, Long id) {
        logger.info("Actualizando funko con id: " + id);
        var funkoUpdated = repository.update(FunkoMapper.postPutToModel(funko), id);
        if (funkoUpdated.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko " + id);
        return funkoUpdated.get();
    }

    @Override
    @CachePut(value = "funkos", key = "#id")
    public Funko patch(FunkoPatchRequest funko, Long id) {
        logger.info("Actualizando funko: " + funko);
        var funkoPatched = repository.patch(FunkoMapper.patchToModel(funko), id);
        if (funkoPatched.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko" + id);
        return funkoPatched.get();
    }

    @Override
    @CacheEvict(value = "funkos", key = "#id")
    public Funko delete(Long id) {
        logger.info("Eliminando funko: " + id);
        var funkoDeleted = repository.delete(id);
        if (funkoDeleted.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko " + id);
        return funkoDeleted.get();
    }
}
