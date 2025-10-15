package org.example.funkos.repository;

import org.example.funkos.models.Funko;

import java.util.List;
import java.util.Optional;

public interface FunkoRepository {
    public List<Funko> getAll();
    public Optional<Funko> getById(Long id);
    public Funko save(Funko funko);
    public Optional<Funko> update(Funko funko, Long id);
    public Optional<Funko> patch(Funko funko, Long id);
    public Optional<Funko> delete(Long id);
}
