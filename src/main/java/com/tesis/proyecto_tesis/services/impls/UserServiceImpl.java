package com.tesis.proyecto_tesis.services.impls;

import com.tesis.proyecto_tesis.entities.User;
import com.tesis.proyecto_tesis.exception.ResourceNotFoundException;
import com.tesis.proyecto_tesis.repositories.UserRepository;
import com.tesis.proyecto_tesis.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> findAll() throws Exception {
        return userRepository.findAll();
    }

    @Override
    public User findById(Long aLong) throws Exception {
        return userRepository.findById(aLong)
                .orElseThrow(()->new ResourceNotFoundException("User","Id",aLong));
    }

    @Override
    public User update(User entity, Long aLong) throws Exception {
        User user1 = userRepository.findById(aLong)
                .orElseThrow(()->new ResourceNotFoundException("User","Id",aLong));
        user1.setPassword(entity.getPassword());
        user1.setEmail(entity.getEmail());
        user1.setName(entity.getName());
        user1.setLastName(entity.getLastName());

        return userRepository.save(user1);
    }

    @Override
    public void deleteById(Long aLong) throws Exception {
        userRepository.deleteById(aLong);
    }

    @Override
    public User createUser(User user) {

        List<User> userList=userRepository.findAll();
        if(userList!=null){
            System.out.println(userList);
            userList.forEach(user1->{
                if(user1.getEmail().equals(user.getEmail())){
                    System.out.println("email repeated");
                    throw new ResourceNotFoundException("There is already another user with the email "+ user.getEmail());
                }
                    }

            );
        }

        System.out.println("encode");
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }


}
