package com.io.mountblue.blogapplication.controller;

import com.io.mountblue.blogapplication.model.User;
import com.io.mountblue.blogapplication.model.Role;
import com.io.mountblue.blogapplication.service.RoleService;
import com.io.mountblue.blogapplication.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/register")
public class RegistrationController {
    private UserService userService;
    private RoleService roleService;

    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/showRegistrationForm")
    public String showMyLoginPage(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/processregister")
    public String processRegistrationForm(
            @ModelAttribute("user") User user) {
        String name = user.getName();
        System.out.println("hi user name is : " + name);
        User exsistingUser = userService.findByName(name);
        if(exsistingUser!=null){
            System.out.println("user already exsists");
            return "redirect:/showRegistrationForm";
        }
        System.out.println("hi exsisting user  name is : " + exsistingUser);
        Role role = new Role();
        role.setRole("ROLE_AUTHOR");
        role.setUsername(user.getName());

        user.setPassword("{noop}" + user.getPassword());

        roleService.saveRole(role);
        userService.save(user);

        return "home";
    }
}
