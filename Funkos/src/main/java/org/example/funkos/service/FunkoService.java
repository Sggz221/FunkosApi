package org.example.funkos.service;

import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.dto.response.FunkoDeleteResponse;
import org.example.funkos.dto.response.FunkoResponse;

import java.util.List;

public interface FunkoService {
    public List<FunkoResponse> getAll();
    public FunkoResponse getById(Long id);
    public FunkoResponse save(FunkoPostPutRequest funko);
    public FunkoResponse update(FunkoPostPutRequest funko, Long id);
    public FunkoResponse patch(FunkoPatchRequest funko, Long id);
    public FunkoDeleteResponse delete(Long id);
}
