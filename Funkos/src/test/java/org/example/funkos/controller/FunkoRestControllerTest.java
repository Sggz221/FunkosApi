package org.example.funkos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.categorias.dto.request.CategoriaPostPutRequest;
import org.example.categorias.dto.response.CategoriaResponse;
import org.example.categorias.mappers.CategoriaMapper;
import org.example.categorias.models.Categoria;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoDeleteResponse;
import org.example.funkos.dto.response.FunkoResponse;
import org.example.funkos.mappers.FunkoMapper;
import org.example.funkos.models.Funko;
import org.example.funkos.service.FunkoServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class FunkoRestControllerTest {

    @MockitoBean
    private FunkoServiceImpl funkoService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }


    // Datos de ejemplo
    private final Categoria categoria = new Categoria(1L, "ANIME", LocalDateTime.now(), LocalDateTime.now());
    private final CategoriaResponse categoriaResponse = CategoriaMapper.toResponse(categoria);
    private final CategoriaPostPutRequest categoriaPostPutRequest = CategoriaMapper.toPostPut(categoria);

    private final UUID uuid = UUID.randomUUID();
    private final FunkoResponse funkoResponse = new FunkoResponse(
            1L,
            uuid.toString(),
            "Spider-Man",
            25.99,
            categoriaResponse,
            LocalDate.of(2022, 10, 5)
    );

    private final FunkoPostPutRequest funkoRequest = new FunkoPostPutRequest(
            1L,
            uuid.toString(),
            "Spider-Man",
            25.99,
            categoriaPostPutRequest,
            "2022-10-05"
    );

    private final FunkoPatchRequest funkoPatch = new FunkoPatchRequest();
    private final String myEndpoint = "/funkos";

    @Test
    void getAllTest() throws Exception {
        when(funkoService.getAll()).thenReturn(List.of(funkoResponse));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<FunkoResponse> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, FunkoResponse.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(res.size() > 0),
                () -> assertTrue(response.getContentAsString().contains("Spider-Man"))
        );

        verify(funkoService, times(1)).getAll();
    }


    @Test
    void getByIdTest() throws Exception {
        when(funkoService.getById(funkoResponse.getId())).thenReturn(funkoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/" + funkoResponse.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        FunkoResponse res = mapper.readValue(response.getContentAsString(), FunkoResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoResponse.getId(), res.getId())
        );

        verify(funkoService, times(1)).getById(funkoResponse.getId());
    }


    @Test
    void getByIdNotFoundTest() throws Exception {
        when(funkoService.getById(-1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Funko no encontrado"));

        MockHttpServletResponse response = mockMvc.perform(
                        get(myEndpoint + "/-1")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        verify(funkoService, times(1)).getById(-1L);
    }


    @Test
    void createFunkoTest() throws Exception {
        // Mockeamos el servicio
        when(funkoService.save(funkoRequest)).thenReturn(funkoResponse);

        // Ejecutamos el POST
        MockHttpServletResponse response = mockMvc.perform(
                post("/funkos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(funkoRequest))
                        .accept(MediaType.APPLICATION_JSON)
        ).andReturn().getResponse();

        // Deserializamos la respuesta
        FunkoResponse res = mapper.readValue(response.getContentAsString(), FunkoResponse.class);

        // Validaciones
        assertEquals(201, response.getStatus());
        assertEquals(funkoResponse.getId(), res.getId());
        assertTrue(response.getContentAsString().contains("Spider-Man"));

        // Verificamos interacción con el servicio
        verify(funkoService, times(1)).save(funkoRequest);
    }


    @Test
    void updateFunkoTest() throws Exception {
        when(funkoService.update(funkoRequest, funkoResponse.getId())).thenReturn(funkoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        put(myEndpoint + "/" + funkoResponse.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoRequest))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        FunkoResponse res = mapper.readValue(response.getContentAsString(), FunkoResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoResponse.getId(), res.getId())
        );

        verify(funkoService, times(1)).update(funkoRequest, funkoResponse.getId());
    }


    @Test
    void patchFunkoTest() throws Exception {
        funkoPatch.setPrecio(30.0);
        funkoPatch.setCategoria("ANIME");

        when(funkoService.patch(funkoPatch, funkoResponse.getId())).thenReturn(funkoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(myEndpoint + "/" + funkoResponse.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(funkoPatch))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        FunkoResponse res = mapper.readValue(response.getContentAsString(), FunkoResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(funkoResponse.getId(), res.getId())
        );

        verify(funkoService, times(1)).patch(funkoPatch, funkoResponse.getId());
    }


    @Test
    void deleteFunkoTest() throws Exception {
        Funko deleted = new Funko();
        deleted.setId(1L);
        deleted.setNombre("Spider-Man");
        deleted.setUuid(UUID.randomUUID());

        FunkoDeleteResponse deleteResponse = new FunkoDeleteResponse("Funko eliminado correctamente", FunkoMapper.toResponse(deleted));

        when(funkoService.delete(funkoResponse.getId())).thenReturn(deleteResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        delete(myEndpoint + "/" + funkoResponse.getId())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        FunkoDeleteResponse res = mapper.readValue(response.getContentAsString(), FunkoDeleteResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(res.getMensaje().contains("eliminado"))
        );

        verify(funkoService, times(1)).delete(funkoResponse.getId());
    }


    @Test
    void findByNombreContainingIgnoreCaseTest() throws Exception {
        when(funkoService.findByNombreContainingIgnoreCase("Spider")).thenReturn(List.of(funkoResponse));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/funkos/nombre/Spider")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<FunkoResponse> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, FunkoResponse.class));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(res.stream().anyMatch(f -> f.getNombre().equals("Spider-Man")));

        verify(funkoService, times(1)).findByNombreContainingIgnoreCase("Spider");
    }

    @Test
    void findByPrecioLessThanTest() throws Exception {
        when(funkoService.findByPrecioLessThan(30.0)).thenReturn(List.of(funkoResponse));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/funkos/precio/30.0")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<FunkoResponse> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, FunkoResponse.class));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(res.stream().allMatch(f -> f.getPrecio() <= 30.0));

        verify(funkoService, times(1)).findByPrecioLessThan(30.0);
    }

    @Test
    void findByCategoriaTest() throws Exception {
        when(funkoService.findByCategoriaName("PELICULAS")).thenReturn(List.of(funkoResponse));

        MockHttpServletResponse response = mockMvc.perform(
                        get("/funkos/categoria/PELICULAS")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<FunkoResponse> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, FunkoResponse.class));

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertTrue(res.stream().allMatch(f -> f.getCategoria().equals("PELICULAS")));

        verify(funkoService, times(1)).findByCategoriaName("PELICULAS");
    }

    @Test
    void findByUuidTest() throws Exception {
        when(funkoService.findByUuid(uuid.toString())).thenReturn(funkoResponse);

        MockHttpServletResponse response = mockMvc.perform(
                        get("/funkos/uuid/" + uuid.toString())
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        FunkoResponse res = mapper.readValue(response.getContentAsString(), FunkoResponse.class);

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(uuid.toString(), res.getUuid());

        verify(funkoService, times(1)).findByUuid(uuid.toString());
    }

}
