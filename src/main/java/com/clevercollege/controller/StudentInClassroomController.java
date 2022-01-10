package com.clevercollege.controller;

import com.clevercollege.model.*;
import com.clevercollege.persistence.DatabaseManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

@RestController
public class StudentInClassroomController {

    @PostMapping("/findClassroomChecked")
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

    @PostMapping("/findCheckedInStudents")
    public List<Student> findCheckedInStudents(HttpServletRequest request, @RequestBody Location classroomChecked) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return null;
        }
        String user_type = (String) session.getAttribute("user_type");
        if(user_type == null || !user_type.equals("professor"))
            return null;

        try {
            List<Student> checkedInStudents = DatabaseManager.getInstance().getCheckInCheckOutDao().findCheckInStudentsByLocation(classroomChecked.getName());
            return checkedInStudents;
        } catch (SQLException e) {
            return null;
        }
    }

    @PostMapping("/findActivityInClassroomAndCheckBookers")
    public List<Student> checkBookers(HttpServletRequest request, @RequestBody Location classroomChecked) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return null;
        }
        String user_type = (String) session.getAttribute("user_type");
        if(user_type == null || !user_type.equals("professor"))
            return null;

        try {
            CheckInCheckOut checkIn = DatabaseManager.getInstance().getCheckInCheckOutDao().findActiveByUser(u.getCf());
            Activity a = DatabaseManager.getInstance().getSingleLessonDao().findByDateTimeClassroomProfessor(u.getCf(), classroomChecked.getId());
            if(a == null)
                a = DatabaseManager.getInstance().getSeminarDao().findByDateTimeClassroomProfessor(u.getCf(), classroomChecked.getId());
            //devo gestire se qua ritorna null?
            return a.getBookers();
        } catch (SQLException e) {
            return null;
        }
    }

}
