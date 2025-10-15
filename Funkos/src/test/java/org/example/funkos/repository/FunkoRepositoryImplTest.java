package org.example.funkos.repository;

import org.example.funkos.models.Categoria;
import org.example.funkos.models.Funko;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FunkoRepositoryImplTest {

    private FunkoRepositoryImpl repository;
    private Funko funkoBase;

    @BeforeEach
    void setUp() {
        repository = new FunkoRepositoryImpl();
        funkoBase = new Funko();
        funkoBase.setUuid(UUID.randomUUID());
        funkoBase.setNombre("Funko Goku");
        funkoBase.setPrecio(15.99);
        funkoBase.setCategoria(Categoria.ANIME);
        funkoBase.setFechaLanzamiento(LocalDate.of(2025, 1, 1));
    }

    @Test
    void saveOk() {
        Funko saved = repository.save(funkoBase);

        assertNotNull(saved.getId());
        assertEquals(0L, saved.getId());
        assertEquals(1, repository.getAll().size());
        assertEquals(saved, repository.getAll().get(0));
    }

    @Test
    void getAllOkk() {
        repository.save(funkoBase);
        Funko funko2 = new Funko();
        funko2.setUuid(UUID.randomUUID());
        funko2.setNombre("Funko Mario");
        funko2.setPrecio(20.0);
        funko2.setCategoria(Categoria.VIDEOJUEGOS);
        funko2.setFechaLanzamiento(LocalDate.of(2022, 5, 15));
        repository.save(funko2);

        List<Funko> result = repository.getAll();
        assertEquals(2, result.size());
    }

    @Test
    void getByIdOk() {
        Funko saved = repository.save(funkoBase);
        Optional<Funko> found = repository.getById(saved.getId());

        assertTrue(found.isPresent());
        assertEquals(saved.getNombre(), found.get().getNombre());
    }

    @Test
    void getByIdNotFound() {
        Optional<Funko> found = repository.getById(999L);
        assertTrue(found.isEmpty());
    }

    @Test
    void updateOk() {
        Funko saved = repository.save(funkoBase);

        Funko nuevo = new Funko();
        nuevo.setNombre("Funko Vegeta");
        nuevo.setPrecio(25.99);
        nuevo.setCategoria(Categoria.ANIME);
        nuevo.setFechaLanzamiento(LocalDate.of(2023, 3, 10));

        Optional<Funko> updated = repository.update(nuevo, saved.getId());

        assertTrue(updated.isPresent());
        assertEquals("Funko Vegeta", updated.get().getNombre());
        assertEquals(25.99, updated.get().getPrecio());
    }

    @Test
    void updateNotFound() {
        Funko nuevo = new Funko();
        nuevo.setNombre("No Existe");

        Optional<Funko> result = repository.update(nuevo, 999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void patchNotFound() {
        Funko cambios = new Funko();
        cambios.setNombre("No Existe");

        Optional<Funko> result = repository.patch(cambios, 999L);
        assertTrue(result.isEmpty());
    }

    @Test
    void deleteOk() {
        Funko saved = repository.save(funkoBase);
        Optional<Funko> deleted = repository.delete(saved.getId());

        assertTrue(deleted.isPresent());
        assertEquals(saved.getId(), deleted.get().getId());
        assertTrue(repository.getAll().isEmpty());
    }

    @Test
    void deleteNotFound() {
        Optional<Funko> deleted = repository.delete(999L);
        assertTrue(deleted.isEmpty());
    }

    @Test
    void patchAllNull() {
        // Guardamos un funko base
        Funko saved = repository.save(funkoBase);

        // Creamos un objeto "parche" con todos los campos nulos
        Funko cambios = new Funko();

        Optional<Funko> patched = repository.patch(cambios, saved.getId());

        assertTrue(patched.isPresent());

        // Ningún campo debería haber cambiado
        assertEquals(funkoBase.getUuid(), patched.get().getUuid());
        assertEquals(funkoBase.getNombre(), patched.get().getNombre());
        assertEquals(funkoBase.getPrecio(), patched.get().getPrecio());
        assertEquals(funkoBase.getCategoria(), patched.get().getCategoria());
        assertEquals(funkoBase.getFechaLanzamiento(), patched.get().getFechaLanzamiento());
    }

    @Test
    void patchSomeNull() {
        Funko saved = repository.save(funkoBase);

        Funko cambios = new Funko();
        cambios.setNombre("Funko Modificado"); // solo nombre cambia
        cambios.setPrecio(null); // no debe modificar el precio
        cambios.setCategoria(null); // no debe modificar la categoría
        cambios.setFechaLanzamiento(null); // no debe modificar la fecha

        Optional<Funko> patched = repository.patch(cambios, saved.getId());

        assertTrue(patched.isPresent());
        assertEquals("Funko Modificado", patched.get().getNombre());
        assertEquals(funkoBase.getPrecio(), patched.get().getPrecio());
        assertEquals(funkoBase.getCategoria(), patched.get().getCategoria());
        assertEquals(funkoBase.getFechaLanzamiento(), patched.get().getFechaLanzamiento());
    }
}
