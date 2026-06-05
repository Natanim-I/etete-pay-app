package com.oasis.FIFAFanWallet.service;

import com.oasis.FIFAFanWallet.model.User;
import com.oasis.FIFAFanWallet.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);

    //User registration with password being hashed
    public User registerUser(User user) {
        //Setting hashed password
        user.setPasswordHash(encoder.encode(user.getPasswordHash()));
        //Saving user to database
        return userRepository.save(user);
    }
}
