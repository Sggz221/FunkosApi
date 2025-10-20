package org.example.categorias.repositories;

import org.example.categorias.models.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    Categoria findByNombreIgnoreCase(String nombre); // encuentra por nombre ignorando mayusculas y minusculas pero tiene que ser el nombre exacto
}
