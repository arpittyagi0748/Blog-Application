package com.io.mountblue.blogapplication.service;

import com.io.mountblue.blogapplication.model.User;

public interface UserServiceInterface {

    User save(User user);

    User findByName(String username);
}
