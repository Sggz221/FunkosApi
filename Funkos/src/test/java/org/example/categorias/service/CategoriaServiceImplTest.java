package org.example.categorias.service;

import org.example.categorias.dto.request.CategoriaPatchRequest;
import org.example.categorias.dto.request.CategoriaPostPutRequest;
import org.example.categorias.dto.response.CategoriaDeleteResponse;
import org.example.categorias.dto.response.CategoriaResponse;
import org.example.categorias.exceptions.CategoriaException;
import org.example.categorias.models.Categoria;
import org.example.categorias.repositories.CategoriaRepository;
import org.example.funkos.models.Funko;
import org.example.funkos.repository.FunkoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceImplTest {

    private CategoriaRepository categoriaRepository;
    private FunkoRepository funkoRepository;
    private CategoriaServiceImpl categoriaService;

    private Funko funkoPrueba = new Funko(1L, UUID.randomUUID(), "Pepe", 10.0, "ANIME", LocalDate.now(), LocalDateTime.now(), LocalDateTime.now());

    @BeforeEach
    void setUp() {
        categoriaRepository = mock(CategoriaRepository.class);
        funkoRepository = mock(FunkoRepository.class);
        categoriaService = new CategoriaServiceImpl(categoriaRepository, funkoRepository);
    }

    @Test
    void testGetAll() {
        Categoria cat = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.findAll()).thenReturn(List.of(cat));

        List<CategoriaResponse> all = categoriaService.getAll();

        assertEquals(1, all.size());
        assertEquals("ANIME", all.get(0).getNombre());
    }

    @Test
    void testGetByIdFound() {
        Categoria cat = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));

        CategoriaResponse response = categoriaService.getById(1L);

        assertEquals(1L, response.getId());
        assertEquals("ANIME", response.getNombre());
    }

    @Test
    void testGetByIdNotFound() {
        when(categoriaRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(CategoriaException.NotFoundException.class,
                () -> categoriaService.getById(1L));
    }

    @Test
    void testSave() {
        CategoriaPostPutRequest request = new CategoriaPostPutRequest("VIDEOJUEGOS");
        Categoria saved = new Categoria(1L, "VIDEOJUEGOS", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.save(ArgumentMatchers.any(Categoria.class))).thenReturn(saved);

        CategoriaResponse response = categoriaService.save(request);

        assertEquals("VIDEOJUEGOS", response.getNombre());
        assertEquals(1L, response.getId());
    }

    @Test
    void testUpdateConflict() {
        CategoriaPostPutRequest request = new CategoriaPostPutRequest("ANIME");
        when(categoriaRepository.findByNombreIgnoreCase("ANIME")).thenReturn(new Categoria());

        assertThrows(CategoriaException.ConflictException.class,
                () -> categoriaService.update(1L, request));
    }

    @Test
    void testPatchConflict() {
        CategoriaPatchRequest request = new CategoriaPatchRequest("ANIME");
        Categoria existing = new Categoria(1L, "OLD", LocalDateTime.now(), LocalDateTime.now());

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoriaRepository.findByNombreIgnoreCase("ANIME")).thenReturn(new Categoria());

        assertThrows(CategoriaException.ConflictException.class,
                () -> categoriaService.patch(1L, request));
    }

    @Test
    void testDeleteWithFunkos() {
        Categoria cat = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(funkoRepository.findByCategoria("ANIME")).thenReturn(List.of(funkoPrueba));

        assertThrows(CategoriaException.ConflictException.class,
                () -> categoriaService.delete(1L));
    }

    @Test
    void testDeleteSuccess() {
        Categoria cat = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(cat));
        when(funkoRepository.findByCategoria("ANIME")).thenReturn(Collections.emptyList());

        CategoriaDeleteResponse response = categoriaService.delete(1L);

        assertEquals("Categoria borrada correctamente", response.getMensaje());
        verify(categoriaRepository, times(1)).delete(cat);
    }

    @Test
    void testFindByNombreIgnoreCaseNotFound() {
        when(categoriaRepository.findByNombreIgnoreCase("NOEXISTE")).thenReturn(null);

        assertThrows(CategoriaException.NotFoundException.class,
                () -> categoriaService.findByNombreIgnoreCase("NOEXISTE"));
    }

    @Test
    void testFindByNombreIgnoreCaseFound() {
        Categoria cat = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.findByNombreIgnoreCase("ANIME")).thenReturn(cat);

        CategoriaResponse response = categoriaService.findByNombreIgnoreCase("ANIME");

        assertEquals("ANIME", response.getNombre());
    }

    @Test
    void testUpdateCategoria_Correcto() {
        Categoria existing = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoriaRepository.findByNombreIgnoreCase("MANGA")).thenReturn(null);
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(i -> i.getArguments()[0]);

        CategoriaPostPutRequest request = new CategoriaPostPutRequest("MANGA");
        CategoriaResponse response = categoriaService.update(1L, request);

        assertEquals("MANGA", response.getNombre());
        verify(categoriaRepository).save(existing);
    }

    @Test
    void testPatchCategoria_Correcto() {
        Categoria existing = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(categoriaRepository.findByNombreIgnoreCase("MANGA")).thenReturn(null);
        when(categoriaRepository.save(any(Categoria.class))).thenAnswer(i -> i.getArguments()[0]);

        CategoriaPatchRequest request = new CategoriaPatchRequest("MANGA");
        CategoriaResponse response = categoriaService.patch(1L, request);

        assertEquals("MANGA", response.getNombre());
        verify(categoriaRepository).save(existing);
    }

    @Test
    void testDeleteCategoria_ConFunkosAsociados_LanzaException() {
        Categoria existing = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(funkoRepository.findByCategoria("ANIME"))
                .thenReturn(List.of(mock(Funko.class))); // simulamos funkos asociados

        CategoriaException.ConflictException ex = assertThrows(
                CategoriaException.ConflictException.class,
                () -> categoriaService.delete(1L)
        );

        assertEquals("Esta categoria tiene recursos asociados", ex.getMessage());
        verify(categoriaRepository, never()).delete(any());
    }
}
