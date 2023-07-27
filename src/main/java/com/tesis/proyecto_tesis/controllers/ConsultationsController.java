package com.tesis.proyecto_tesis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesis.proyecto_tesis.entities.Category;
import com.tesis.proyecto_tesis.entities.Consultation;
import com.tesis.proyecto_tesis.services.CategoryService;
import com.tesis.proyecto_tesis.services.ConsultationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/consultations")
public class ConsultationsController {

    @Autowired
    private ConsultationService consultationService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Consultation>> fetchAll() {
        try {
            List<Consultation> consultations = consultationService.findAll();
            return new ResponseEntity<>(consultations, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping(value = "/{consultationId}")
    public ResponseEntity<?> updateConsultation(@PathVariable("consultationId") Long consultationId, @Valid @RequestBody Consultation consultation) throws Exception {
        log.info("Updating Consultation with Id {}", consultationId);
        Consultation currentConsultation = consultationService.update(consultation, consultationId);
        return ResponseEntity.ok(currentConsultation);
    }

    @DeleteMapping(value = "/{consultationId}")
    public void deleteConsultation(@PathVariable("consultationId") Long consultationId) throws Exception {
        log.info("Eliminando Recipe con Id {}", consultationId);
        consultationService.deleteById(consultationId);
    }

    private String formatMessage( BindingResult result){
        List<Map<String,String>> errors = result.getFieldErrors().stream()
                .map(err ->{
                    Map<String,String> error =  new HashMap<>();
                    error.put(err.getField(), err.getDefaultMessage());
                    return error;
                }).collect(Collectors.toList());
        ErrorMessage errorMessage = ErrorMessage.builder()
                .code("01")
                .messages(errors).build();
        ObjectMapper mapper = new ObjectMapper();
        String jsonString="";
        try {
            jsonString = mapper.writeValueAsString(errorMessage);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
