package com.tesis.proyecto_tesis.services.impls;

import com.tesis.proyecto_tesis.entities.Category;
import com.tesis.proyecto_tesis.entities.Consultation;
import com.tesis.proyecto_tesis.entities.User;
import com.tesis.proyecto_tesis.exception.ResourceNotFoundException;
import com.tesis.proyecto_tesis.repositories.CategoryRepository;
import com.tesis.proyecto_tesis.repositories.ConsultationRepository;
import com.tesis.proyecto_tesis.repositories.UserRepository;
import com.tesis.proyecto_tesis.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Category> findAll() throws Exception {
        return categoryRepository.findAll();
    }

    @Override
    public Category findById(Long aLong) throws Exception {
        return categoryRepository.findById(aLong)
                .orElseThrow(()->new ResourceNotFoundException("Category","Id",aLong));
    }

    @Override
    public List<Category> getAllCategoriesByUserIdUser(Long userId) {
        return categoryRepository.findCategoriesByUserIdUser(userId)
                .orElseThrow(()->new ResourceNotFoundException("User", "Id", userId));
    }

    @Override
    public Category update(Category entity, Long aLong) throws Exception {
        Category category1 = categoryRepository.findById(aLong)
                .orElseThrow(()->new ResourceNotFoundException("Category","Id",aLong));
        category1.setName(entity.getName());

        return categoryRepository.save(category1);
    }

    @Override
    public void deleteById(Long aLong) throws Exception {

        List<Consultation> consulationList=consultationRepository.findConsultationsByCategoryIdCategory(aLong);

        if(consulationList!=null)
            consulationList.forEach(consultation -> consultationRepository.deleteById(consultation.getIdConsultation()));

        categoryRepository.deleteById(aLong);
    }

    @Override
    public Category createCategory(Category category, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("User","Id",userId));
        return categoryRepository.save(category.setUser(user));
    }

}
