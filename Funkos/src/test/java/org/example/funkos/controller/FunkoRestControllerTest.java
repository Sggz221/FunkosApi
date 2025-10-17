package org.example.funkos.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.models.Categoria;
import org.example.funkos.models.Funko;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class FunkoRestControllerTest {

    // Como SpringBoot usa Jackson para serializar JSONs, hay que crear un mapeador nuevo para las fechas (LocalDate y  LocalDateTime)
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule()) // <- El modulo de tiempo
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // <- Cambia el formato predeterminado de Jackson, el cual son timestamps (fecha en milisegundos totales)
                                                                       // para que sea un formato legible como 2025-12-12

    @Autowired
    MockMvc mockMvc; // Permite simular solicitudes HTTP para el controlador

    final String endpoint = "/funkos";
    static Long savedId;

    @Test
    @Order(1) // Especifica el orden de ejecucion de los tests, este ira primero, porque como son tests de integracion,
    void getAllVacioAlInicio() throws Exception {
        MockHttpServletResponse response = mockMvc.perform( // Simula la peticion HTTP...
                        get(endpoint) // Simula un GET al endpoint pasado por parametro...
                                .accept(MediaType.APPLICATION_JSON)) // Decimos que la respuesta sera en JSON
                .andReturn()
                .getResponse(); // Obtener la respeusta

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()), // Que sea un 2XX
                () -> assertTrue(response.getContentAsString().contains("[]")) // Body vacio
        );
    }

    @Test
    @Order(2) // Segundo en ejecutarse
    void saveOkUnFunko() throws Exception {
        FunkoPostPutRequest request = new FunkoPostPutRequest();
        request.setUuid(UUID.randomUUID().toString());
        request.setNombre("Gyro Zeppeli");
        request.setPrecio(29.99);
        request.setCategoria(Categoria.ANIME.name());
        request.setFechaLanzamiento(LocalDate.now().toString());

        MockHttpServletResponse response = mockMvc.perform(
                        post(endpoint)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)) // Escribe el JSON a partir del objeto
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Funko res = mapper.readValue(response.getContentAsString(), Funko.class); //Convierte el JSON a un objeto
        savedId = res.getId(); // Se guarda en una variable "global" (en este contexto) para poder usarla en los siguientes tests, puesto que son de integracion

        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), response.getStatus()), // Devolvio 201 Created ?
                () -> assertNotNull(res.getId()), // Que no sea nulo
                () -> assertEquals("Gyro Zeppeli", res.getNombre())
        );
    }

    @Test
    @Order(3)
    void getByIdOk() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint + "/" + savedId) // <- Get usando el id que se guardo antes construyendo la URI sobre la marcha
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Funko res = mapper.readValue(response.getContentAsString(), Funko.class); // De nuevo se crea el objeto

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals(savedId, res.getId()),
                () -> assertEquals("Gyro Zeppeli", res.getNombre())
        );
    }

    @Test
    @Order(4)
    void updateOk() throws Exception {
        FunkoPostPutRequest request = new FunkoPostPutRequest();
        request.setUuid(UUID.randomUUID().toString());
        request.setNombre("Vegeta SSJ Blue");
        request.setPrecio(35.99);
        request.setCategoria(Categoria.ANIME.name());
        request.setFechaLanzamiento(LocalDate.now().minusDays(10).toString());

        MockHttpServletResponse response = mockMvc.perform(
                        put(endpoint + "/" + savedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Funko res = mapper.readValue(response.getContentAsString(), Funko.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals("Vegeta SSJ Blue", res.getNombre()),
                () -> assertEquals(Categoria.ANIME, res.getCategoria())
        );
    }

    @Test
    @Order(5)
    void patchNombreOk() throws Exception {
        FunkoPatchRequest patch = new FunkoPatchRequest();
        patch.setNombre("Funko Patch");
        patch.setPrecio(19.99);

        MockHttpServletResponse response = mockMvc.perform(
                        patch(endpoint + "/" + savedId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(patch))
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        Funko res = mapper.readValue(response.getContentAsString(), Funko.class);

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertEquals("Funko Patch", res.getNombre()),
                () -> assertEquals(19.99, res.getPrecio())
        );
    }

    @Test
    @Order(6)
    void deleteOk() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        delete(endpoint + "/" + savedId)
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertAll(
                () -> assertEquals(HttpStatus.OK.value(), response.getStatus()),
                () -> assertTrue(response.getContentAsString().contains("Funko eliminado correctamente"))
        );
    }

    @Test
    @Order(7)
    void getByIdNotFound() throws Exception {
        MockHttpServletResponse response = mockMvc.perform(
                        get(endpoint + "/9999")
                                .accept(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse();

        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }
}
