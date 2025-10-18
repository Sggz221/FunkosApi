package org.example.funkos.service;

import ch.qos.logback.classic.Logger;
import lombok.val;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoDeleteResponse;
import org.example.funkos.dto.response.FunkoResponse;
import org.example.funkos.exceptions.FunkoException;
import org.example.funkos.mappers.FunkoMapper;
import org.example.funkos.models.Funko;
import org.example.funkos.repository.FunkoRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FunkoServiceImpl implements FunkoService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(FunkoServiceImpl.class);
    private final FunkoRepository repository;

    @Autowired
    public FunkoServiceImpl(FunkoRepository funkoRepository) {
        this.repository = funkoRepository;
        initData();
    }

    private void initData(){
        val funko = new Funko(null, UUID.randomUUID(), "Gyro Zeppeli", 20.0, "ANIME", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        val funko2 = new Funko(null, UUID.randomUUID(), "Johnny Joestar", 20.0, "ANIME", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        val funko3 = new Funko(null, UUID.randomUUID(), "Funny Valentine", 20.0, "ANIME", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());

        repository.save(funko);
        repository.save(funko2);
        repository.save(funko3);
    }

    @Override
    public List<FunkoResponse> getAll() {
        logger.info("Devolviendo todos los Funkos");
        return repository.findAll()
                .stream()
                .map(FunkoMapper::toResponse) // asumiendo que toResponse devuelve FunkoResponse
                .collect(Collectors.toList());
    }

    @Override
    @CachePut(value = {"funkos"}, key = "#id")
    public FunkoResponse getById(Long id) {
        logger.info("Buscando funko por id: " + id);
        var funko = repository.findById(id);
        if (funko.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko con id: " + id);
        return FunkoMapper.toResponse(funko.get());
    }

    @Override
    public FunkoResponse save(FunkoPostPutRequest funko) {
        logger.info("Guardando funko: " + funko);
        var funkoToSave = repository.save(FunkoMapper.postPutToModel(funko));
        return FunkoMapper.toResponse(funkoToSave);
    }

    @Override
    @CachePut(value = "funkos", key = "#id")
    public FunkoResponse update(FunkoPostPutRequest funko, Long id) {
        logger.info("Actualizando funko con id: " + id);
        if (repository.findById(id).isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko con id: " + id);

        var funkoToUpdate = FunkoMapper.postPutToModel(funko);
        funkoToUpdate.setId(id);
        return FunkoMapper.toResponse(repository.save(funkoToUpdate)); //  EL save funciona como update pero hibernate infiere si auieres actualizar o guardar
    }

    @Override
    @CachePut(value = "funkos", key = "#id")
    public FunkoResponse patch(FunkoPatchRequest funko, Long id) {
        logger.info("Haciendo PATCH del funko con id: {}", id);

        var existing = repository.findById(id)
                .orElseThrow(() -> new FunkoException.NotFoundException("No se ha encontrado el funko con id: " + id));

        // Solo actualiza los campos que vengan no nulos en el patch request
        if (funko.getNombre() != null) existing.setNombre(funko.getNombre());
        if (funko.getPrecio() != null) existing.setPrecio(funko.getPrecio());
        if (funko.getCategoria() != null) existing.setCategoria(funko.getCategoria());
        if (funko.getFechaLanzamiento() != null) existing.setFechaLanzamiento(LocalDate.parse(funko.getFechaLanzamiento()));

        existing.setUpdatedAt(LocalDateTime.now());

        Funko updated = repository.save(existing);

        logger.info("Funko actualizado parcialmente: {}", updated);
        return FunkoMapper.toResponse(updated);
    }

    @Override
    @CacheEvict(value = "funkos", key = "#id")
    public FunkoDeleteResponse delete(Long id) {
        logger.info("Eliminando funko: " + id);
        var existing  = repository.findById(id);
        if (existing.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko con id: " + id);
        repository.delete(existing.get());
        return new FunkoDeleteResponse("Funko borrado correctamente", existing.get());
    }

    // Metodos extra
    @Override
    public List<FunkoResponse> findByNombreContainingIgnoreCase(String nombre) {
        logger.info("Buscando funkos por nombre: " + nombre);
        var funkoList = repository.findByNombreContainingIgnoreCase(nombre);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FunkoResponse> findByPrecioLessThan(Double precio) {
        logger.info("Buscando funkos por precio menor que: " + precio);
        var funkoList = repository.findByPrecioLessThan(precio);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FunkoResponse> findByCategoria(String categoria) {
        logger.info("Buscando funkos por categoria: " + categoria);
        var funkoList = repository.findByCategoria(categoria);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @CachePut(cacheNames = "funkos", key = "#result.id") // El Id del objeto que devuelve la funcion
    public FunkoResponse findByUuid(String uuid) {
        logger.info("Buscando funko por uuid: " + uuid);
        var funko = repository.findByUuid(UUID.fromString(uuid));
        if (funko == null) throw new FunkoException.NotFoundException("No se ha encontrado el funko con uuid: " + uuid);
        return FunkoMapper.toResponse(funko);
    }

    @Override
    public List<FunkoResponse> findByNombreQuery(String nombre) {
        logger.info("QUERY: Buscando funkos por nombre: " + nombre);
        var funkoList = repository.findByNombreQuery(nombre);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FunkoResponse> findByPrecioLessThanQuery(Double precio) {
        logger.info("QUERY: Buscando funkos por precio menor que: " + precio);
        var funkoList = repository.findByPrecioLessThanQuery(precio);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FunkoResponse> findByCategoriaQuery(String categoria) {
        logger.info("QUERY: Buscando funkos por categoria: " + categoria);
        var funkoList = repository.findByCategoriaQuery(categoria);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @CachePut(cacheNames = "funkos", key = "#result.id") // El Id del objeto que devuelve la funcion
    public FunkoResponse findByUuidQuery(String uuid) {
        logger.info("QUERY: Buscando funko por uuid: " + uuid);
        var funko = repository.findByUuidQuery(UUID.fromString(uuid));
        if (funko == null) throw new FunkoException.NotFoundException("No se ha encontrado el funko con uuid: " + uuid);
        return FunkoMapper.toResponse(funko);
    }
}
