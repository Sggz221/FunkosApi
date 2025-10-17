package org.example.funkos.service;

import ch.qos.logback.classic.Logger;
import lombok.val;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.exceptions.FunkoException;
import org.example.funkos.mappers.FunkoMapper;
import org.example.funkos.models.Categoria;
import org.example.funkos.models.Funko;
import org.example.funkos.repository.FunkoRepository;
import org.example.funkos.repository.OldRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class FunkoServiceImpl implements FunkoService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(FunkoServiceImpl.class);
    private final FunkoRepository repository;

    @Autowired
    public FunkoServiceImpl(OldRepository repository) {
        this.repository = repository;
    }

    private void initData(){
        val funko = new Funko(null, UUID.randomUUID(), "Gyro Zeppeli", 20.0, Categoria.ANIME, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        val funko2 = new Funko(null, UUID.randomUUID(), "Johnny Joestar", 20.0, Categoria.ANIME, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        val funko3 = new Funko(null, UUID.randomUUID(), "Funny Valentine", 20.0, Categoria.ANIME, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
    }

    @Override
    public List<Funko> getAll() {
        logger.info("Devolviendo todos los Funkos");
        return repository.findAll();
    }

    @Override
    @CachePut(value = {"funkos"}, key = "#id")
    public Funko getById(Long id) {
        logger.info("Buscando funko por id: " + id);
        var funko = repository.findById(id);
        if (funko.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko con id" + id);
        return funko.get();
    }

    @Override
    public FunkoPostPutRequest save(FunkoPostPutRequest funko) {
        logger.info("Guardando funko: " + funko);
        return FunkoMapper.toPostPut(repository.save(FunkoMapper.postPutToModel(funko)));
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
