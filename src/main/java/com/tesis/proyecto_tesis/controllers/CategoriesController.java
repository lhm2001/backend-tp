package com.tesis.proyecto_tesis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesis.proyecto_tesis.entities.Category;
import com.tesis.proyecto_tesis.entities.Consultation;
import com.tesis.proyecto_tesis.entities.User;
import com.tesis.proyecto_tesis.services.CategoryService;
import com.tesis.proyecto_tesis.services.ConsultationService;
import com.tesis.proyecto_tesis.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import javax.validation.Valid;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Base64;

@Slf4j
@RestController
@RequestMapping("/categories")
public class CategoriesController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ConsultationService consultationService;

    private final String FLASK_PREDICT_ENDPOINT = "http://18.218.200.224/predict";

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Category>> fetchAll() {
        try {
            List<Category> categories = categoryService.findAll();
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{categoryId}/consultations")
    public List<Consultation> getAllConsultationsByCategoryId(@PathVariable("categoryId") Long categoryId){
        List<Consultation> consultations = consultationService.getAllConsultationsByCategoryIdCategory(categoryId);
        return consultations;
    }

    @PutMapping(value = "/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") Long categoryId, @Valid @RequestBody Category category) throws Exception {
        log.info("Updating Category with Id {}", categoryId);
        Category currentCategory = categoryService.update(category, categoryId);
        return ResponseEntity.ok(currentCategory);
    }

    @DeleteMapping(value = "/{categoryId}")
    public void deleteCategory(@PathVariable("categoryId") Long categoryId) throws Exception {
        log.info("Eliminando Recipe con Id {}", categoryId);
        categoryService.deleteById(categoryId);
    }

    @PostMapping(path = "/{categoryId}/consultations")
    public ResponseEntity<Consultation> createConsultation(@Valid @RequestBody Consultation consultation, @PathVariable("categoryId") Long categoryId , BindingResult result){
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,this.formatMessage(result));
        }

        try {
//            // Establecer la fecha y hora actual
//            Date currentDate = new Date();;
//
//            // Asignar la fecha y hora actual a la variable createdDate de la consulta
//            consultation.setCreatedDate(currentDate);

            // Establecer la fecha y hora actual
            Date currentDate = new Date();

            // Crear una instancia de SimpleDateFormat para definir el formato deseado
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

            // Formatear la fecha actual a String
            String dateString = sdf.format(currentDate);

            // Asignar la fecha y hora actual a la variable createdDate de la consulta
            consultation.setCreatedDate(dateString);

            // Usar ObjectMapper para convertir el objeto Java a JSON
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonBody = objectMapper.writeValueAsString(Map.of("image", consultation.getPhoto()));

            // Crear HttpHeaders y establecer el tipo de contenido como "application/json"
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Agregar la autenticación básica a la cabecera "Authorization"
            String username = "usuario"; // Reemplaza por el nombre de usuario de Basic Auth
            String password = "contraseña"; // Reemplaza por la contraseña de Basic Auth
            String credentials = username + ":" + password;
            String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
            headers.set("Authorization", "Basic " + encodedCredentials);

            // Conectar con el servicio Flask
            HttpEntity<String> requestEntity = new HttpEntity<>(jsonBody, headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> responseEntity = restTemplate.exchange(FLASK_PREDICT_ENDPOINT, HttpMethod.POST, requestEntity, String.class);

            // Deserializar la respuesta JSON en un Map<String, Object>
            Map<String, Object> responseMap = objectMapper.readValue(responseEntity.getBody(), new TypeReference<Map<String, Object>>() {});

            // Acceder a los valores de cada campo en la respuesta JSON
            Boolean assymetryValueObject = (Boolean) responseMap.get("asymmetry");
            String resultAssymetry = assymetryValueObject.equals(true) ? "Asymmetric" : "Symmetrical";

            Boolean borderValueObject = (Boolean) responseMap.get("border");
            String resultBorder = borderValueObject.equals(true) ? "Irregular" : "Regular";

            Boolean colorValueObject = (Boolean) responseMap.get("color");
            String resultColor = colorValueObject.equals(true) ? "Heterogeneous" : "Homogeneous";

//            Double diameterValueObject = (Double) responseMap.get("diameter");
//            String resultDiameter = diameterValueObject.toString();

            String resultDiameter = "The diameter can measure manually. If the diameter is greater than 6mm, a specialist should be consulted.";

            String photoValueObject = (String) responseMap.get("photo");
            String photo = photoValueObject.toString();

            Integer predictValueObject = (Integer) responseMap.get("prediction");
            Boolean predict = (predictValueObject != null && predictValueObject.equals(1)) ? true : false;

            consultation.setDysplastic(predict)
                    .setResultAssymetry(resultAssymetry)
                    .setResultBorder(resultBorder)
                    .setResultColor(resultColor)
                    .setResultDiameter(resultDiameter)
                    .setPhoto(photo);


            Consultation consultationDB = consultationService.createConsultation(consultation, categoryId);
            return ResponseEntity.status(HttpStatus.CREATED).body(consultationDB);
        } catch (Exception e) {
            // Manejar cualquier excepción que pueda ocurrir al convertir a JSON
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during request");
        }
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
