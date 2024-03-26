package com.io.mountblue.blogapplication.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class LoginController {
    @GetMapping("/customlogin")
    public String showLoginPage(){
        System.out.println("hi");
        return "login";
    }
}
