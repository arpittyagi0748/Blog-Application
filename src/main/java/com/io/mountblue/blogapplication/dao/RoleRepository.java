package com.io.mountblue.blogapplication.dao;

import com.io.mountblue.blogapplication.model.Role;
import com.io.mountblue.blogapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,Integer> {

}
