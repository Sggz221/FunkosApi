package org.example.funkos.service;

import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoDeleteResponse;
import org.example.funkos.dto.response.FunkoResponse;
import java.util.List;

public interface FunkoService {
    List<FunkoResponse> getAll();
    FunkoResponse getById(Long id);
    FunkoResponse save(FunkoPostPutRequest funko);
    FunkoResponse update(FunkoPostPutRequest funko, Long id);
    FunkoResponse patch(FunkoPatchRequest funko, Long id);
    FunkoDeleteResponse delete(Long id);

    List<FunkoResponse> findByNombreContainingIgnoreCase(String nombre);
    List<FunkoResponse> findByPrecioLessThan(Double precio);
    List<FunkoResponse> findByCategoria(String categoria);
    FunkoResponse findByUuid(String uuid);

    List<FunkoResponse> findByNombreQuery(String nombre);
    List<FunkoResponse> findByPrecioLessThanQuery(Double precio);
    List<FunkoResponse> findByCategoriaQuery( String categoria);
    FunkoResponse findByUuidQuery(String uuid);
}
