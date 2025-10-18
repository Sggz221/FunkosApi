package org.example.funkos.service;

import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.exceptions.FunkoException;
import org.example.funkos.mappers.FunkoMapper;
import org.example.funkos.models.Funko;
import org.example.funkos.repository.FunkoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FunkoServiceImplTest {

    @Mock
    private FunkoRepository repository;

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
        funko.setCategoria("ANIME");
        funko.setFechaLanzamiento(LocalDate.of(2021, 1, 1));
        funko.setCreatedAt(LocalDateTime.now());
        funko.setUpdatedAt(LocalDateTime.now());

        requestPostPut = new FunkoPostPutRequest();
        requestPostPut.setUuid(UUID.randomUUID().toString());
        requestPostPut.setNombre("Funko Vegeta");
        requestPostPut.setPrecio(30.0);
        requestPostPut.setCategoria("ANIME");
        requestPostPut.setFechaLanzamiento(LocalDate.of(2023, 5, 10).toString());

        requestPatch = new FunkoPatchRequest();
        requestPatch.setNombre("Funko Patch");
        requestPatch.setPrecio(40.0);
    }

    @Test
    void getAll_ShouldReturnListOfFunkos() {
        when(repository.findAll()).thenReturn(List.of(funko));

        var result = service.getAll();

        assertEquals(1, result.size());
        assertEquals("Funko Goku", result.get(0).getNombre());
        verify(repository, times(1)).findAll();
    }

    @Test
    void getById_ShouldReturnFunko_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(funko));

        var result = service.getById(1L);

        assertNotNull(result);
        assertEquals("Funko Goku", result.getNombre());
        verify(repository).findById(1L);
    }

    @Test
    void getById_ShouldThrowException_WhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FunkoException.NotFoundException.class, () -> service.getById(999L));
        verify(repository).findById(999L);
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
        when(repository.findById(1L)).thenReturn(Optional.of(funko));
        when(repository.save(any(Funko.class))).thenAnswer(invocation -> {
            Funko arg = invocation.getArgument(0);
            arg.setId(1L);
            return arg;
        });

        var result = service.update(requestPostPut, 1L);

        assertEquals("Funko Vegeta", result.getNombre());
        verify(repository).findById(1L);
        verify(repository).save(any(Funko.class));
    }

    @Test
    void update_ShouldThrowException_WhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FunkoException.NotFoundException.class, () -> service.update(requestPostPut, 999L));
        verify(repository).findById(999L);
    }

    @Test
    void patch_ShouldReturnPatchedFunko_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(funko));
        when(repository.save(any(Funko.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = service.patch(requestPatch, 1L);

        assertEquals("Funko Patch", result.getNombre());
        assertEquals(40.0, result.getPrecio());
        verify(repository).findById(1L);
        verify(repository).save(any(Funko.class));
    }

    @Test
    void patch_ShouldThrowException_WhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FunkoException.NotFoundException.class, () -> service.patch(requestPatch, 999L));
        verify(repository).findById(999L);
    }

    @Test
    void delete_ShouldReturnDeletedFunko_WhenExists() {
        when(repository.findById(1L)).thenReturn(Optional.of(funko));

        var result = service.delete(1L);

        assertEquals("Funko Goku", result.getDeleted().getNombre());
        verify(repository).findById(1L);
        verify(repository).delete(funko);
    }

    @Test
    void delete_ShouldThrowException_WhenNotFound() {
        when(repository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(FunkoException.NotFoundException.class, () -> service.delete(999L));
        verify(repository).findById(999L);
    }
}
