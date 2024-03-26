package com.io.mountblue.blogapplication;

import com.io.mountblue.blogapplication.dao.PostRepository;
import com.io.mountblue.blogapplication.dao.UserRepository;
import com.io.mountblue.blogapplication.model.Post;
import com.io.mountblue.blogapplication.model.User;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.sql.Date;
import java.util.Calendar;

@SpringBootApplication
public class BlogapplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogapplicationApplication.class, args);
	}

}
