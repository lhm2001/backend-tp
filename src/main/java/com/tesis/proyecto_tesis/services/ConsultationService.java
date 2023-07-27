package com.tesis.proyecto_tesis.services;

import com.tesis.proyecto_tesis.entities.Category;
import com.tesis.proyecto_tesis.entities.Consultation;

import java.util.List;

public interface ConsultationService extends CrudService<Consultation,Long>{
    Consultation createConsultation(Consultation consultation, Long categoryId);
    List<Consultation> getAllConsultationsByCategoryIdCategory(Long categoryId);
}
