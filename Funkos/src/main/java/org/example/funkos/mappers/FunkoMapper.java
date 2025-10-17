package org.example.funkos.mappers;

import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.models.Categoria;
import org.example.funkos.models.Funko;

import java.time.LocalDate;
import java.util.UUID;

public class FunkoMapper {

    public static Funko postPutToModel(FunkoPostPutRequest dto) {
        Funko funko = new Funko();

        // UUID: generar uno si no se pasa
        if (dto.getUuid() != null && !dto.getUuid().isBlank()) funko.setUuid(UUID.fromString(dto.getUuid()));
        else funko.setUuid(UUID.randomUUID());

        funko.setNombre(dto.getNombre());
        funko.setPrecio(dto.getPrecio());

        if (dto.getCategoria() != null && !dto.getCategoria().isBlank()) funko.setCategoria(Categoria.valueOf(dto.getCategoria()));


        if (dto.getFechaLanzamiento() != null && !dto.getFechaLanzamiento().isBlank()) {
            funko.setFechaLanzamiento(LocalDate.parse(dto.getFechaLanzamiento()));
        }

        return funko;
    }

    /**
     * Convierte un FunkoPatchRequest a Funko (para PATCH)
     */
    public static Funko patchToModel(FunkoPatchRequest dto) {
        Funko funko = new Funko();

        if (dto.getUuid() != null && !dto.getUuid().isBlank()) funko.setUuid(UUID.fromString(dto.getUuid()));
        if (dto.getNombre() != null && !dto.getNombre().isBlank()) funko.setNombre(dto.getNombre());
        if (dto.getPrecio() != null) funko.setPrecio(dto.getPrecio());
        if (dto.getCategoria() != null && !dto.getCategoria().isBlank()) funko.setCategoria(Categoria.valueOf(dto.getCategoria()));
        if (dto.getFechaLanzamiento() != null && !dto.getFechaLanzamiento().isBlank()) funko.setFechaLanzamiento(LocalDate.parse(dto.getFechaLanzamiento()));

        return funko;
    }

    public static FunkoPostPutRequest toPostPut(Funko funko) {
        return new FunkoPostPutRequest(
                funko.getUuid(),
                funko.get
        );
    }
}
