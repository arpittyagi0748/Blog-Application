package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.dao.UserRepository;
import com.io.mountblue.blogapplication.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService{
    private UserRepository userRepository;
    @Autowired
    public UserService(UserRepository thePostRepository) {
        userRepository = thePostRepository;
    }

    public User save(User thePost) {
        return userRepository.save(thePost);
    }
    public User findByName(String username) {
        return userRepository.findByName(username);
    }
}
