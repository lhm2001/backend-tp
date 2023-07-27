package com.tesis.proyecto_tesis.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tesis.proyecto_tesis.entities.Category;
import com.tesis.proyecto_tesis.entities.User;
import com.tesis.proyecto_tesis.services.CategoryService;
import com.tesis.proyecto_tesis.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/users")
public class UsersController {
    @Autowired
    private UserService userService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> fetchAll() {
        System.out.println("info");
        try {
            List<User> users = userService.findAll();
            return new ResponseEntity<>(users, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{userId}")
    public ResponseEntity<User> getUserById(@PathVariable("userId") Long userId) {
        try {
            User user = userService.findById(userId);
            if (user != null) {
                return new ResponseEntity<User>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(path = "/{userId}/categories")
    public List<Category> getAllCategoriesByUserId(@PathVariable("userId") Long userId){
        List<Category> categories = categoryService.getAllCategoriesByUserIdUser(userId);
        return categories;
    }

    @PutMapping(value = "/{userId}")
    public ResponseEntity<?> updateUser(@PathVariable("userId") Long userId, @Valid @RequestBody User user) throws Exception {
        log.info("Updating User with Id {}", userId);
        User currentUser = userService.update(user, userId);
        return ResponseEntity.ok(currentUser);
    }

    @PostMapping(path = "{userId}/categories")
    public ResponseEntity<Category> createCategory(@Valid @RequestBody Category category,@PathVariable("userId") Long userId , BindingResult result){
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,this.formatMessage(result));
        }
        Category categoryDB= categoryService.createCategory(category,userId);
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDB);
    }

    @PostMapping()
    public ResponseEntity<User> createUser(@Valid @RequestBody User user, BindingResult result){
        if (result.hasErrors()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,this.formatMessage(result));
        }
        User userDB= userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userDB);
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
