package com.tesis.proyecto_tesis.services.impls;

import com.tesis.proyecto_tesis.dto.LoginDTO;
import com.tesis.proyecto_tesis.entities.User;
import com.tesis.proyecto_tesis.repositories.UserRepository;
import com.tesis.proyecto_tesis.response.LoginResponse;
import com.tesis.proyecto_tesis.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    public LoginResponse login(LoginDTO loginDTO) {

        String msg = "";
        User user1 = userRepository.findByEmail(loginDTO.getEmail());
        if (user1 != null) {
            System.out.println("serviceLogin");
            String password = loginDTO.getPassword();
            String encodedPassword = user1.getPassword();
            Boolean isPwdRight = passwordEncoder.matches(password, encodedPassword);
            if (isPwdRight) {
                System.out.println("pwdright");
                Optional<User> user = userRepository.findOneByEmailAndPassword(loginDTO.getEmail(), encodedPassword);
                if (user.isPresent()) {
                    Long idUser=user.get().getIdUser();
                    System.out.println(idUser);
                    return new LoginResponse("Login Success", true, idUser);
                } else {
                    return new LoginResponse("Login Failed", false,0L);
                }
            } else {
                return new LoginResponse("password Not Match", false,0L);
            }
        }else {
            System.out.println("error");
            return new LoginResponse("Email not exits", false,0L);
        }
    }
}
