package org.example.categorias.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.categorias.dto.request.CategoriaPatchRequest;
import org.example.categorias.dto.request.CategoriaPostPutRequest;
import org.example.categorias.dto.response.CategoriaDeleteResponse;
import org.example.categorias.dto.response.CategoriaResponse;
import org.example.categorias.exceptions.CategoriaException;
import org.example.categorias.service.CategoriaServiceImpl;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
public class CategoriaControllerTest {

    @MockitoBean
    private CategoriaServiceImpl categoriaService;

    @MockitoBean
    private FunkoServiceImpl funkoService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper;

    private final String endpoint = "/categorias";
    private final CategoriaResponse categoriaResponse = new CategoriaResponse(1L, "ANIME");
    private final CategoriaPostPutRequest postPutRequest = new CategoriaPostPutRequest(1L, "MANGA");
    private final CategoriaPatchRequest patchRequest = new CategoriaPatchRequest("MANGA");

    @BeforeEach
    void setUp() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllTest() throws Exception {
        when(categoriaService.getAll()).thenReturn(List.of(categoriaResponse));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        List<CategoriaResponse> res = mapper.readValue(response.getContentAsString(),
                mapper.getTypeFactory().constructCollectionType(List.class, CategoriaResponse.class));

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1, res.size()),
                () -> assertTrue(response.getContentAsString().contains("ANIME"))
        );

        verify(categoriaService, times(1)).getAll();
    }

    @Test
    void getByIdTest() throws Exception {
        when(categoriaService.getById(1L)).thenReturn(categoriaResponse);

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        CategoriaResponse res = mapper.readValue(response.getContentAsString(), CategoriaResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(1L, res.getId()),
                () -> assertEquals("ANIME", res.getNombre())
        );

        verify(categoriaService, times(1)).getById(1L);
    }

    @Test
    void getByIdNotFoundTest() throws Exception {
        when(categoriaService.getById(-1L))
                .thenThrow(new CategoriaException.NotFoundException("Categoria no encontrada"));

        MockHttpServletResponse response = mockMvc.perform(get(endpoint + "/-1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
        assertTrue(response.getContentAsString().contains("Categoria no encontrada"));

        verify(categoriaService, times(1)).getById(-1L);
    }


    @Test
    void saveTest() throws Exception {
        when(categoriaService.save(postPutRequest)).thenReturn(new CategoriaResponse(2L, "MANGA"));

        MockHttpServletResponse response = mockMvc.perform(post(endpoint)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postPutRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        CategoriaResponse res = mapper.readValue(response.getContentAsString(), CategoriaResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus()),
                () -> assertEquals(2L, res.getId()),
                () -> assertEquals("MANGA", res.getNombre())
        );

        verify(categoriaService, times(1)).save(postPutRequest);
    }

    @Test
    void updateTest() throws Exception {
        when(categoriaService.update(1L, postPutRequest)).thenReturn(new CategoriaResponse(1L, "MANGA"));

        MockHttpServletResponse response = mockMvc.perform(put(endpoint + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(postPutRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        CategoriaResponse res = mapper.readValue(response.getContentAsString(), CategoriaResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals("MANGA", res.getNombre())
        );

        verify(categoriaService, times(1)).update(1L, postPutRequest);
    }

    @Test
    void patchTest() throws Exception {
        when(categoriaService.patch(1L, patchRequest)).thenReturn(new CategoriaResponse(1L, "MANGA"));

        MockHttpServletResponse response = mockMvc.perform(patch(endpoint + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(patchRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        CategoriaResponse res = mapper.readValue(response.getContentAsString(), CategoriaResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals("MANGA", res.getNombre())
        );

        verify(categoriaService, times(1)).patch(1L, patchRequest);
    }

    @Test
    void deleteTest() throws Exception {
        CategoriaDeleteResponse deleteResponse = new CategoriaDeleteResponse("Categoria eliminada correctamente", categoriaResponse);
        when(categoriaService.delete(1L)).thenReturn(deleteResponse);

        MockHttpServletResponse response = mockMvc.perform(delete(endpoint + "/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();

        CategoriaDeleteResponse res = mapper.readValue(response.getContentAsString(), CategoriaDeleteResponse.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(res.getMensaje().contains("eliminada")),
                () -> assertEquals(categoriaResponse.getNombre(), res.getDeleted().getNombre())
        );

        verify(categoriaService, times(1)).delete(1L);
    }
}
