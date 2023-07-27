package com.tesis.proyecto_tesis.services;

import com.tesis.proyecto_tesis.dto.LoginDTO;
import com.tesis.proyecto_tesis.response.LoginResponse;

public interface AuthenticationService {

    LoginResponse login(LoginDTO loginDTO);
}
