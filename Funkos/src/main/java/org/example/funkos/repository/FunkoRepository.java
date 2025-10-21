package org.example.funkos.repository;

import org.example.categorias.models.Categoria;
import org.example.funkos.models.Funko;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface FunkoRepository extends JpaRepository<Funko, Long> {
    // Metodos extra porque el CRUD basico ya lo implementa JPA
    // Con nombre de metodos de JPA
    List<Funko> findByNombreContainingIgnoreCase(String nombre);
    List<Funko> findByPrecioLessThan(Double precio);
    List<Funko> findByCategoria(Categoria categoria);
    Funko findByUuid(UUID uuid);

    // Actualizacion de categoria en cascada
    @Modifying
    @Query("UPDATE Funko f SET f.categoria = :nuevaCategoria WHERE f.categoria = :categoriaAntigua")
    int actualizarCategorias(@Param("nuevaCategoria") Categoria nuevaCategoria, @Param("categoriaAntigua") Categoria categoriaAntigua);

}