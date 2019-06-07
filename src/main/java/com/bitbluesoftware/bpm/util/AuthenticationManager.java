package com.bitbluesoftware.bpm.util;

import com.bitbluesoftware.bpm.model.User;
import org.mindrot.jbcrypt.BCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.security.SecureRandom;

@Configuration
public class AuthenticationManager {
    Logger log = LoggerFactory.getLogger(AuthenticationManager.class);

    @Autowired
    DAO dao;

    @Bean
    @Primary
    public AuthenticationManager getAuthenticationManager(){
        return this;
    }

    public User authenticate(String username,String password) {
        User user=null;
        for(User user1 : dao.getUsers().values()) {
            if (user1.getUsername().equalsIgnoreCase(username)) {
                user = user1;
                break;
            }
        }

        if(user!=null && checkPassword(password,user.getPassword())) {
            return user;
        }
        return null;
    }

    public String hashPassword(String password_plaintext) {
        String salt = BCrypt.gensalt(12);
        String hashed_password = BCrypt.hashpw(password_plaintext, salt);
        log.error(hashed_password);
        return(hashed_password);
    }

    private boolean checkPassword(String password_plaintext, String stored_hash) {
        boolean password_verified = false;

        if(null == stored_hash || !stored_hash.startsWith("$2a$"))
            throw new java.lang.IllegalArgumentException("Invalid hash provided for comparison");

        password_verified = BCrypt.checkpw(password_plaintext, stored_hash);
        return(password_verified);
    }

    public String generateToken() {
        SecureRandom random = new SecureRandom();
        String CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        int SECURE_TOKEN_LENGTH = 256;
        char[] symbols = CHARACTERS.toCharArray();
        String token="";
        for(int i = 0; i<30;i++)
            token+=symbols[random.nextInt(CHARACTERS.length())];
        log.error(token);
        return token;
    }
}
