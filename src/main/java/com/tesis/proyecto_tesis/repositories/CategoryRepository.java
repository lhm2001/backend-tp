package com.tesis.proyecto_tesis.repositories;

import com.tesis.proyecto_tesis.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    public Optional<List<Category>> findCategoriesByUserIdUser(Long userId);

}
