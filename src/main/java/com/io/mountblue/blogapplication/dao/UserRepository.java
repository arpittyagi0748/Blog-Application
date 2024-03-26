package com.io.mountblue.blogapplication.dao;

import com.io.mountblue.blogapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Integer> {
    User findByName(String name);
}
