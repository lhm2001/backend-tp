package com.tesis.proyecto_tesis.services;

import java.util.List;

public interface CrudService <T,ID> {
    List<T> findAll() throws Exception;
    T findById(ID id) throws Exception;
    T update(T entity, ID id) throws Exception;
    void deleteById(ID id) throws Exception;
}