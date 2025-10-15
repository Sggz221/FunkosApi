package org.example.funkos.mappers;

import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.models.Categoria;
import org.example.funkos.models.Funko;

import java.time.LocalDate;
import java.util.UUID;

public class FunkoMapper {
    public static Funko postPutToModel(FunkoPostPutRequest postPutFunko) {
        Funko funko = new Funko();

        funko.setUuid(UUID.fromString(postPutFunko.getUuid()));
        funko.setNombre(postPutFunko.getNombre());
        funko.setPrecio(postPutFunko.getPrecio());
        funko.setCategoria(Categoria.valueOf(postPutFunko.getCategoria()));
        funko.setFechaLanzamiento(LocalDate.parse(postPutFunko.getFechaLanzamiento()));
        return funko;
    }

    public static Funko patchToModel(FunkoPatchRequest funkoPatchRequest) {
        Funko funko = new Funko();
        if (funko.getUuid() != null) funko.setUuid(UUID.fromString(funkoPatchRequest.getUuid()));
        if (funko.getNombre() != null) funko.setNombre(funkoPatchRequest.getNombre());
        if (funko.getPrecio() != null) funko.setPrecio(funkoPatchRequest.getPrecio());
        if (funko.getCategoria() != null) funko.setCategoria(Categoria.valueOf(funkoPatchRequest.getCategoria()));
        if (funko.getFechaLanzamiento() != null) funko.setFechaLanzamiento(LocalDate.parse(funkoPatchRequest.getFechaLanzamiento()));
        return funko;
    }

}
