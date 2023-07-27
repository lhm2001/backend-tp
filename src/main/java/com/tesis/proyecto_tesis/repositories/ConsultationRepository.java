package com.tesis.proyecto_tesis.repositories;

import com.tesis.proyecto_tesis.entities.Category;
import com.tesis.proyecto_tesis.entities.Consultation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ConsultationRepository extends JpaRepository<Consultation,Long> {
    public Optional<List<Consultation>> findConsultationsByCategoryIdCategory(Long categoryId);

}
