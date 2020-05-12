package com.esan.mohammad.ppmtool.services;

import com.esan.mohammad.ppmtool.domain.User;
import com.esan.mohammad.ppmtool.exceptions.UsernameAlreadyExistException;
import com.esan.mohammad.ppmtool.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public User saveUser(User newUser){
        try{
            newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
            newUser.setUsername(newUser.getUsername());
            return userRepository.save(newUser);

        } catch (Exception ex){
            throw new UsernameAlreadyExistException("Username "+newUser.getUsername()+" already exist");
        }
    }


}
