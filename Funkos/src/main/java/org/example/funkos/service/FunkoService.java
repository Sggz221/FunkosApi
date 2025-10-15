package org.example.funkos.service;

import org.example.funkos.dto.request.FunkoPatchRequest;
import org.example.funkos.dto.request.FunkoPostPutRequest;
import org.example.funkos.models.Funko;

import java.util.List;

public interface FunkoService {
    public List<Funko> getAll();
    public Funko getById(Long id);
    public Funko save(FunkoPostPutRequest funko);
    public Funko update(FunkoPostPutRequest funko, Long id);
    public Funko patch(FunkoPatchRequest funko, Long id);
    public Funko delete(Long id);
}
