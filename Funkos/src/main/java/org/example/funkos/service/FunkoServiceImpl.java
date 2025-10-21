package org.example.funkos.service;

import ch.qos.logback.classic.Logger;
import lombok.val;
import org.example.categorias.dto.request.CategoriaPostPutRequest;
import org.example.categorias.exceptions.CategoriaException;
import org.example.categorias.models.Categoria;
import org.example.categorias.repositories.CategoriaRepository;
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
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FunkoServiceImpl implements FunkoService {
    private final Logger logger = (Logger) LoggerFactory.getLogger(FunkoServiceImpl.class);
    private final FunkoRepository repository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public FunkoServiceImpl(FunkoRepository funkoRepository, CategoriaRepository categoriaRepository) {
        this.repository = funkoRepository;
        this.categoriaRepository = categoriaRepository;
        initData();
    }

    private void initData(){
        val categoria = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        val categoria2 = new Categoria(2L, "VIDEOJUEGOS", LocalDateTime.now(), LocalDateTime.now());
        val categoria3 = new Categoria(3L, "PELICULAS", LocalDateTime.now(), LocalDateTime.now());

        val funko = new Funko(null, UUID.randomUUID(), "Gyro Zeppeli", 20.0, categoria, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        val funko2 = new Funko(null, UUID.randomUUID(), "Johnny Joestar", 20.0,  categoria2, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());
        val funko3 = new Funko(null, UUID.randomUUID(), "Funny Valentine", 20.0, categoria3, LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());

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
        val funko = repository.findById(id);
        if (funko.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko con id: " + id);
        return FunkoMapper.toResponse(funko.get());
    }

    @Override
    public FunkoResponse save(FunkoPostPutRequest funko) {
        logger.info("Guardando funko: " + funko);
        if (!isCategoriaValid(funko.getCategoria())) throw new IllegalArgumentException("La categoría no es válida. La categoría no puede tener campos nulos, se debe asegurar que los datos son los correctos y de que la categoría existe.");
        val categoria = categoriaRepository.findById(funko.getCategoria().getId());
        var funkoToSave = FunkoMapper.postPutToModel(funko);
        funkoToSave.setCategoria(categoria.get());
        return FunkoMapper.toResponse(repository.save(funkoToSave));
    }

    @Override
    @CachePut(value = "funkos", key = "#id")
    public FunkoResponse update(FunkoPostPutRequest funko, Long id) {
        logger.info("Actualizando funko con id: " + id);
        if (repository.findById(id).isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko con id: " + id);

        val funkoToUpdate = FunkoMapper.postPutToModel(funko);
        funkoToUpdate.setId(id);
        funkoToUpdate.setUpdatedAt(LocalDateTime.now());
        return FunkoMapper.toResponse(repository.save(funkoToUpdate)); //  EL save funciona como update pero hibernate infiere si auieres actualizar o guardar
    }

    @Override
    @CachePut(value = "funkos", key = "#id")
    public FunkoResponse patch(FunkoPatchRequest funko, Long id) {
        logger.info("Haciendo PATCH del funko con id: {}", id);

        var existing = repository.findById(id)
                .orElseThrow(() -> new FunkoException.NotFoundException("No se ha encontrado el funko con id: " + id));

        // Solo actualiza los campos que vengan no nulos en el patch request
        if (funko.getUuid() != null) existing.setNombre(funko.getUuid());
        if (funko.getNombre() != null) existing.setNombre(funko.getNombre());
        if (funko.getPrecio() != null) existing.setPrecio(funko.getPrecio());
        if (funko.getCategoria() != null) {
            val categoria = categoriaRepository.findByNombreIgnoreCase(funko.getCategoria());
            if (categoria == null) throw new CategoriaException.NotFoundException("La categoría que se intentó poner en el funko no existe.");
            existing.setCategoria(categoria); // Si existe se inserta en el funko
        }
        if (funko.getFechaLanzamiento() != null) existing.setFechaLanzamiento(LocalDate.parse(funko.getFechaLanzamiento()));

        existing.setUpdatedAt(LocalDateTime.now());

        Funko updated = repository.save(existing);

        logger.info("Funko actualizado parcialmente: "+ updated);
        return FunkoMapper.toResponse(updated);
    }

    @Override
    @CacheEvict(value = "funkos", key = "#id")
    public FunkoDeleteResponse delete(Long id) {
        logger.info("Eliminando funko: " + id);
        var existing  = repository.findById(id);
        if (existing.isEmpty()) throw new FunkoException.NotFoundException("No se ha encontrado el funko con id: " + id);
        repository.delete(existing.get());
        return new FunkoDeleteResponse("Funko borrado correctamente", FunkoMapper.toResponse(existing.get()));
    }

    // Metodos extra
    @Override
    public List<FunkoResponse> findByNombreContainingIgnoreCase(String nombre) {
        logger.info("Buscando funkos por nombre: " + nombre);
        val funkoList = repository.findByNombreContainingIgnoreCase(nombre);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FunkoResponse> findByPrecioLessThan(Double precio) {
        logger.info("Buscando funkos por precio menor que: " + precio);
        var funkoList = repository.findByPrecioLessThan(precio);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public List<FunkoResponse> findByCategoriaName(String nombre) {
        logger.info("Buscando funkos por categoria: " + nombre);
        val categoria = categoriaRepository.findByNombreIgnoreCase(nombre);
        val funkoList = repository.findByCategoria(categoria);
        return funkoList.stream().map(FunkoMapper::toResponse).collect(Collectors.toList());
    }

    @Override
    @CachePut(cacheNames = "funkos", key = "#result.id") // El Id del objeto que devuelve la funcion
    public FunkoResponse findByUuid(String uuid) {
        logger.info("Buscando funko por uuid: " + uuid);
        val funko = repository.findByUuid(UUID.fromString(uuid));
        if (funko == null) throw new FunkoException.NotFoundException("No se ha encontrado el funko con uuid: " + uuid);
        return FunkoMapper.toResponse(funko);
    }

    private boolean isCategoriaValid (CategoriaPostPutRequest categoria) {
        if (categoria == null) return false;
        else if (categoria.getNombre() == null) return false;
        else if (categoria.getId() == null) return false;
        else if (categoriaRepository.findByNombreIgnoreCase(categoria.getNombre()) == null) return false;
        else if (categoriaRepository.findById(categoria.getId()).isEmpty()) return false;
        // Este troncho sirve para verificar que el cliente no te ha pasado una categoría válida pero el id pertenece a una categoría distinta del nombre
        else if (!Objects.equals(categoriaRepository.findById(categoria.getId()).get().getNombre(), categoriaRepository.findByNombreIgnoreCase(categoria.getNombre()).getNombre())) return false;
        else return true;
    }
}
