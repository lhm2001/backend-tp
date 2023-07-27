package com.tesis.proyecto_tesis.services.impls;

import com.tesis.proyecto_tesis.entities.Category;
import com.tesis.proyecto_tesis.entities.Consultation;
import com.tesis.proyecto_tesis.exception.ResourceNotFoundException;
import com.tesis.proyecto_tesis.repositories.CategoryRepository;
import com.tesis.proyecto_tesis.repositories.ConsultationRepository;
import com.tesis.proyecto_tesis.services.ConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationServiceImpl implements ConsultationService {

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Override
    public Consultation createConsultation(Consultation consultation, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","Id",categoryId));
        return consultationRepository.save(consultation.setCategory(category));
    }

    @Override
    public List<Consultation> getAllConsultationsByCategoryIdCategory(Long categoryId) {
        return consultationRepository.findConsultationsByCategoryIdCategory(categoryId)
                .orElseThrow(()->new ResourceNotFoundException("Category", "Id", categoryId));
    }

    @Override
    public List<Consultation> findAll() throws Exception {
        return consultationRepository.findAll();
    }

    @Override
    public Consultation findById(Long aLong) throws Exception {
        return consultationRepository.findById(aLong)
                .orElseThrow(()->new ResourceNotFoundException("Consultation","Id",aLong));
    }

    @Override
    public Consultation update(Consultation entity, Long aLong) throws Exception {
        Consultation consultation1 = consultationRepository.findById(aLong)
                .orElseThrow(()->new ResourceNotFoundException("Consultation","Id",aLong));
        consultation1.setDysplastic(entity.getDysplastic());
        consultation1.setPhoto(entity.getPhoto());
        consultation1.setResultAssymetry(entity.getResultAssymetry());
        consultation1.setResultBorder(entity.getResultBorder());
        consultation1.setResultColor(entity.getResultColor());
        consultation1.setResultDiameter(entity.getResultDiameter());

        return consultationRepository.save(consultation1);
    }

    @Override
    public void deleteById(Long aLong) throws Exception {
        consultationRepository.deleteById(aLong);
    }
}
