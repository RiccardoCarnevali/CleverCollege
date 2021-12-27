package com.clevercollege.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @GetMapping("/tmpPage")
    public String tmp() {
        return "tmpPage";
    }

    @GetMapping("/loginPage")
    public String login() {
        return "loginPage";
    }

    @PostMapping("/doLogin")
    public String login(HttpServletResponse response, HttpServletRequest request, String cf, String password) {
        //if user is inside db do something
        //if user isn't inside db do something else
        return "tmpPage";
    }
}
