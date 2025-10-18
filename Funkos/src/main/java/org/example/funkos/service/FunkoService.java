package org.example.funkos.service;

import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoDeleteResponse;
import org.example.funkos.dto.response.FunkoResponse;
import org.example.funkos.models.Funko;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface FunkoService {
    public List<FunkoResponse> getAll();
    public FunkoResponse getById(Long id);
    public FunkoResponse save(FunkoPostPutRequest funko);
    public FunkoResponse update(FunkoPostPutRequest funko, Long id);
    public FunkoResponse patch(FunkoPatchRequest funko, Long id);
    public FunkoDeleteResponse delete(Long id);

    public List<FunkoResponse> findByNombreContainingIgnoreCase(String nombre);
    public List<FunkoResponse> findByPrecioLessThan(Double precio);
    public List<FunkoResponse> findByCategoria(String categoria);
    public FunkoResponse findByUuid(String uuid);

    List<FunkoResponse> findByNombreQuery(String nombre);
    List<FunkoResponse> findByPrecioLessThanQuery(Double precio);
    List<FunkoResponse> findByCategoriaQuery( String categoria);
    FunkoResponse findByUuidQuery(String uuid);

}
