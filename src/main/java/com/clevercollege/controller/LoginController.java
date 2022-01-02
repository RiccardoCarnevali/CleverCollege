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

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/doLogin")
    public String login(Model model,HttpServletRequest request, String cf, String password) {
        try {
            User u = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(cf);
            
            if((u != null) && (BCrypt.checkpw(password, u.getPassword()))) {
                request.getSession().setAttribute("user", u);
                request.getSession().setAttribute("user_cf", u.getCf());
                String type = null;
                if(DatabaseManager.getInstance().getStudentDao().findByPrimaryKey(cf) != null)
                	type = "student";
                else if(DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(cf) != null)
                	type = "professor";
                else if(DatabaseManager.getInstance().getAdministratorDao().findByPrimaryKey(cf) != null)
                	type = "admin";
                request.getSession().setAttribute("user_type", type);
                return "redirect:/";
            }
            else if(u != null) {
                model.addAttribute("password_error", true);
                return "login";
            }
            else {
                model.addAttribute("no_existing_user_error", true);
                return "login";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "login";
    }
}
