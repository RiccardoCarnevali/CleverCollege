package com.clevercollege.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.clevercollege.model.Student;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

@Controller
public class LoginController {

    @GetMapping("/doLogout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession();
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest req) {
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
                Student student = DatabaseManager.getInstance().getStudentDao().findByPrimaryKey(cf);
                if(student != null) {
                	type = "student";
                	request.getSession().setAttribute("student_number", student.getStudentNumber());
                }
                else if(DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(cf) != null)
                	type = "professor";
                else if(DatabaseManager.getInstance().getAdministratorDao().findByPrimaryKey(cf) != null)
                	type = "admin";
                request.getSession().setAttribute("user_type", type);
                
                String afterLoginUri = (String) request.getSession().getAttribute("after-login");
                
                if(afterLoginUri != null)
                	return "redirect:" + afterLoginUri;
                else
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
