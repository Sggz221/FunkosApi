package org.example.funkos.service;

import org.example.funkos.exceptions.FunkoException;
import org.example.funkos.mappers.FunkoMapper;
import org.example.funkos.models.Categoria;
import org.example.funkos.models.Funko;
import org.example.funkos.repository.FunkoRepositoryImpl;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FunkoServiceImplTest {

    @Mock
    private FunkoRepositoryImpl repository;

    @InjectMocks
    private FunkoServiceImpl service;

    private Funko funko;
    private FunkoPostPutRequest requestPostPut;
    private FunkoPatchRequest requestPatch;

    @BeforeEach
    void setUp() {
        funko = new Funko();
        funko.setId(1L);
        funko.setUuid(UUID.randomUUID());
        funko.setNombre("Funko Goku");
        funko.setPrecio(25.0);
        funko.setCategoria(Categoria.ANIME);
        funko.setFechaLanzamiento(LocalDate.of(2021, 1, 1));

        requestPostPut = new FunkoPostPutRequest();
        requestPostPut.setUuid(UUID.randomUUID().toString()); // 👈 Añadido
        requestPostPut.setNombre("Funko Vegeta");
        requestPostPut.setPrecio(30.0);
        requestPostPut.setCategoria(String.valueOf(Categoria.ANIME));
        requestPostPut.setFechaLanzamiento(String.valueOf(LocalDate.of(2023, 5, 10)));

        requestPatch = new FunkoPatchRequest();
        requestPatch.setNombre("Funko Patch");
        requestPatch.setPrecio(40.0);
    }


    @Test
    void getAll_ShouldReturnListOfFunkos() {
        when(repository.getAll()).thenReturn(List.of(funko));

        var result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("Funko Goku", result.get(0).getNombre());
        verify(repository, times(1)).getAll();
    }

    @Test
    void getById_ShouldReturnFunko_WhenExists() {
        when(repository.getById(1L)).thenReturn(Optional.of(funko));

        var result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Funko Goku", result.getNombre());
        verify(repository).getById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(repository.getById(999L)).thenReturn(Optional.empty());

        assertThrows(FunkoException.NotFoundException.class, () -> service.getById(999L));
        verify(repository).getById(999L);
    }

    @Test
    void save_ShouldReturnSavedFunko() {
        Funko mapped = FunkoMapper.postPutToModel(requestPostPut);
        when(repository.save(any(Funko.class))).thenReturn(mapped);

        var result = service.save(requestPostPut);

        assertNotNull(result);
        assertEquals("Funko Vegeta", result.getNombre());
        verify(repository).save(any(Funko.class));
    }

    @Test
    void update_ShouldReturnUpdatedFunko_WhenExists() {
        Funko mapped = FunkoMapper.postPutToModel(requestPostPut);
        when(repository.update(any(Funko.class), eq(1L))).thenReturn(Optional.of(mapped));

        var result = service.update(requestPostPut, 1L);

        assertEquals("Funko Vegeta", result.getNombre());
        verify(repository).update(any(Funko.class), eq(1L));
    }

    @Test
    void update_ShouldThrowException_WhenNotFound() {
        when(repository.update(any(Funko.class), eq(999L))).thenReturn(Optional.empty());

        assertThrows(FunkoException.NotFoundException.class, () -> service.update(requestPostPut, 999L));
        verify(repository).update(any(Funko.class), eq(999L));
    }

    @Test
    void patch_ShouldReturnPatchedFunko_WhenExists() {
        Funko mapped = FunkoMapper.patchToModel(requestPatch);
        when(repository.patch(any(Funko.class), eq(1L))).thenReturn(Optional.of(mapped));

        var result = service.patch(requestPatch, 1L);

        assertEquals("Funko Patch", result.getNombre());
        assertEquals(40.0, result.getPrecio());
        verify(repository).patch(any(Funko.class), eq(1L));
    }

    @Test
    void patch_ShouldThrowException_WhenNotFound() {
        when(repository.patch(any(Funko.class), eq(999L))).thenReturn(Optional.empty());

        assertThrows(FunkoException.NotFoundException.class, () -> service.patch(requestPatch, 999L));
        verify(repository).patch(any(Funko.class), eq(999L));
    }

    @Test
    void delete_ShouldReturnDeletedFunko_WhenExists() {
        when(repository.delete(1L)).thenReturn(Optional.of(funko));

        var result = service.delete(1L);

        assertEquals("Funko Goku", result.getNombre());
        verify(repository).delete(1L);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(repository.delete(999L)).thenReturn(Optional.empty());

        assertThrows(FunkoException.NotFoundException.class, () -> service.delete(999L));
        verify(repository).delete(999L);
    }
}
