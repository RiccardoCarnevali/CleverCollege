package com.clevercollege.controller;

import com.clevercollege.model.Activity;
import com.clevercollege.model.Location;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;

@Controller
public class StudentInClassroomController {

    @PostMapping("/findClassroomChecked")
    @ResponseBody
    public Location findClass(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return null;
        }
        String user_type = (String) session.getAttribute("user_type");
        if(user_type == null || !user_type.equals("professor"))
            return null;

        try {
            Location l = DatabaseManager.getInstance().getCheckInCheckOutDao().findPlaceOfCheckIn(u.getCf());
            return l;
        } catch (SQLException e) {
            return null;
        }
    }

    @PostMapping("/findStudentsCheckedIn")
    @ResponseBody
    public Activity findClass(HttpServletRequest request, @RequestBody Location classroomChecked) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return null;
        }
        String user_type = (String) session.getAttribute("user_type");
        if(user_type == null || !user_type.equals("professor"))
            return null;

        try {
          //prendi gli studenti dall'aula->e in base alla data e all'ora corrente cerca l'attività
            //data l'attività verifica se sono prenotati
        } catch (SQLException e) {
            return null;
        }
    }

}
