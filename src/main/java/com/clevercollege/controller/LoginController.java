package com.clevercollege.controller;

import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class LoginController {

    @GetMapping("/doLogout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/loginPage")
    public String login() {
        return "loginPage";
    }

    @PostMapping("/doLogin")
    public String login(Model model,HttpServletRequest request, String cf, String password) {
        try {
            User u = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(cf);
            
            if((u != null) && (BCrypt.checkpw(password, u.getPassword()))) {
                request.getSession().setAttribute("cf", cf);
                return "redirect:/";
            }
            else if(u != null) {
                model.addAttribute("password_error", true);
                return "loginPage";
            }
            else {
                model.addAttribute("no_existing_user_error", true);
                return "loginPage";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "loginPage";
    }
}
