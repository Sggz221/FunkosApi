package org.example.funkos.mappers;

import org.example.categorias.mappers.CategoriaMapper;
import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoResponse;
import org.example.funkos.models.Funko;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class FunkoMapper {

    public static Funko postPutToModel(FunkoPostPutRequest dto) {
        Funko funko = new Funko();

        // UUID: generar uno si no se pasa
        if (dto.getUuid() != null && !dto.getUuid().isBlank()) funko.setUuid(UUID.fromString(dto.getUuid()));
        else funko.setUuid(UUID.randomUUID());

        funko.setNombre(dto.getNombre());
        funko.setPrecio(dto.getPrecio());
        //funko.setCategoria(CategoriaMapper.postPutToModel(dto.getCategoria()));


        if (dto.getFechaLanzamiento() != null && !dto.getFechaLanzamiento().isBlank()) {
            funko.setFechaLanzamiento(LocalDate.parse(dto.getFechaLanzamiento()));
        }

        funko.setCreatedAt(LocalDateTime.now());
        funko.setUpdatedAt(LocalDateTime.now());
        return funko;
    }

    public static FunkoResponse toResponse(Funko funko) {
        return new  FunkoResponse(
                funko.getId(),
                funko.getUuid().toString(),
                funko.getNombre(),
                funko.getPrecio(),
                CategoriaMapper.toResponse(funko.getCategoria()),
                funko.getFechaLanzamiento()
        );
    }
}
