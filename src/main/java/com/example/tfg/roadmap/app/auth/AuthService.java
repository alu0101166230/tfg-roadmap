package com.example.tfg.roadmap.app.auth;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.tfg.roadmap.app.user.User;
import com.example.tfg.roadmap.app.user.UserRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AuthService {

    private final UserRepository userRepository;

    public void register(String username, String email, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        String hashPass = Base64.getEncoder().encodeToString(hashedBytes);
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(hashPass);
        userRepository.save(user);
    }

    public boolean login(String username, String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = md.digest(password.getBytes());
        String hashPass = Base64.getEncoder().encodeToString(hashedBytes);

        Optional<User> user =  userRepository.findByUsernameAndPassword(username, hashPass);
        

        return user.isPresent()? true: false; 


    }


}
