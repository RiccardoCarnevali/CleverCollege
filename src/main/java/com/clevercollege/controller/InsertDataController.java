package com.clevercollege.controller;

import com.clevercollege.model.Student;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

@Controller
public class InsertDataController {
    @PostMapping("/insertData")
    public String insertData(String name, Integer capacity) {

        //if the radio button checked is a place
            //check if the type of place is a classroom
            //create place and check if it already exists
        //if the radio button checked is a course
            //create course and check if it already exists
        //add into database
        return null;
    }

    @PostMapping("/insertUser")
    @ResponseBody
    public String insertUser(String kindOfUser, String cf, String name, String surname, String email, String idStudent) {

        String token = UUID.randomUUID().toString();
        String tmpPassword = BCrypt.hashpw(token, BCrypt.gensalt(12));

        if(!checkValidCf(name, surname, cf))
            return "cf not valid";

            try {
                if(DatabaseManager.getInstance().getUserDao().findByEmail(email) != null)
                    return "email already exists";
                if(DatabaseManager.getInstance().getUserDao().findByPrimaryKey(cf) != null)
                    return "user already exists";

                if(kindOfUser.equals("student")) {
                    Student s = new Student(cf, name, surname, email, tmpPassword, null, null, idStudent);
                    if(DatabaseManager.getInstance().getStudentDao().findByIdStudent(idStudent) != null)
                        return "idStudent already exists";
                    else {
                        DatabaseManager.getInstance().getStudentDao().saveOrUpdate(s);
                        DatabaseManager.getInstance().commit();
                        EmailService.getInstance().sendFirstPassword(email, token);
                        return "user inserted";
                    }
                }
                else {
                    User u = new User(cf, name, surname, email, tmpPassword, null, null);
                    if(kindOfUser.equals("professor")) {
                        DatabaseManager.getInstance().getProfessorDao().saveOrUpdate(u);
                        DatabaseManager.getInstance().commit();
                        EmailService.getInstance().sendFirstPassword(email, token);
                        return "user inserted";

                    }
                    else {
                       DatabaseManager.getInstance().getAdministratorDao().saveOrUpdate(u);
                       DatabaseManager.getInstance().commit();
                       EmailService.getInstance().sendFirstPassword(email, token);
                       return "user inserted";
                    }
                }
            } catch (SQLException e) {
                return "server error";
            }
    }

    private boolean checkValidCf(String name, String surname, String cf) {
        cf = cf.trim(); name = name.trim(); surname = surname.trim();
        cf = cf.replaceAll(" ", ""); name = name.replaceAll(" ", ""); surname = surname.replaceAll(" ", "");
        cf = cf.toLowerCase(); name = name.toLowerCase(); surname = surname.toLowerCase();

        String first3 = cf.substring(0, 3);
        String second3 = cf.substring(3, 6);

        return checkForCf(surname, first3, 3) && checkForCf(name, second3, 4);
    }

    private boolean checkForCf(String nameOrSurname, String char3, int numChar) {
        String valid3 = "";
        if(nameOrSurname.length() < 3) {
            valid3 = nameOrSurname;
            for(int i = nameOrSurname.length(); i < 3; i++)
                valid3 = valid3.concat("x");
        }
        else {
            int numCons = 0;
            ArrayList<Character> cons = new ArrayList<>();
            ArrayList<Character> voc = new ArrayList<>();
            for(int i = 0; i < nameOrSurname.length(); i++) {
                if(nameOrSurname.charAt(i) != 'a' && nameOrSurname.charAt(i) != 'e' && nameOrSurname.charAt(i) != 'i' && nameOrSurname.charAt(i) != 'o' && nameOrSurname.charAt(i) != 'u') {
                    cons.add(nameOrSurname.charAt(i));
                    numCons++;
                }
                else {
                    voc.add(nameOrSurname.charAt(i));
                }
            }
            if(numCons >= numChar) {
                valid3 = valid3.concat(cons.get(0).toString());
                valid3 = valid3.concat(cons.get(numChar - 2).toString());
                valid3 = valid3.concat(cons.get(numChar - 1).toString());
            }
            else {
                for(int i = 0; i < cons.size(); i++)
                    valid3 = valid3.concat(cons.get(i).toString());
                for(int i = valid3.length(), j = 0; i < 3 && j < voc.size(); i++)
                    valid3 = valid3.concat(voc.get(j).toString());
            }
        }
        return valid3.equals(char3);
    }

}
