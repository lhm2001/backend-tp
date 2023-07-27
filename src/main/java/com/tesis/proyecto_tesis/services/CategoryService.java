package com.tesis.proyecto_tesis.services;

import com.tesis.proyecto_tesis.entities.Category;

import java.util.List;

public interface CategoryService extends CrudService<Category,Long>{
    Category createCategory(Category category, Long userId);
    List<Category> getAllCategoriesByUserIdUser(Long userId);
}
