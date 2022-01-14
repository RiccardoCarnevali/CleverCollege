package com.clevercollege.controller;

import java.sql.SQLException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Activity;
import com.clevercollege.model.CheckInCheckOut;
import com.clevercollege.model.Location;
import com.clevercollege.model.Student;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;

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
            e.printStackTrace();
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
        if(user_type == null || !user_type.equals("professor")) {
            return null;
        }

        try {
            List<Student> checkedInStudents = DatabaseManager.getInstance().getCheckInCheckOutDao().findCheckInStudentsByLocation(classroomChecked.getId());
            return checkedInStudents;
        } catch (SQLException e) {
            e.printStackTrace();
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
            Activity a = DatabaseManager.getInstance().getSingleLessonDao().findByDateTimeClassroomProfessor(u.getCf(), classroomChecked.getId());
            if(a == null)
                a = DatabaseManager.getInstance().getSeminarDao().findByDateTimeClassroomProfessor(u.getCf(), classroomChecked.getId());
            if(a == null)
                return new ArrayList<>();
            return a.getBookers();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    @PostMapping("/forcedStudentCheckOut")
    public String forcedCheckOut(HttpServletRequest request, String studentId) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return null;
        }
        String user_type = (String) session.getAttribute("user_type");
        if(user_type == null || !user_type.equals("professor"))
            return null;

        try {
            CheckInCheckOut c = DatabaseManager.getInstance().getCheckInCheckOutDao().findActiveByUser(studentId);
            c.setOutTime(LocalTime.now().toString().substring(0,8));
            DatabaseManager.getInstance().getCheckInCheckOutDao().saveOrUpdate(c);
            DatabaseManager.getInstance().commit();
            return "check-out done";
        } catch (SQLException e) {
            e.printStackTrace();
            return "server error";
        }
    }

}
