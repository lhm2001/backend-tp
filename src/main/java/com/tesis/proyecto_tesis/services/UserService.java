package com.tesis.proyecto_tesis.services;

import com.tesis.proyecto_tesis.entities.Category;
import com.tesis.proyecto_tesis.entities.User;

import java.util.List;

public interface UserService extends CrudService<User,Long>{

    User createUser(User user);

}
