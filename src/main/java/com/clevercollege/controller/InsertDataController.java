package com.clevercollege.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.clevercollege.model.Course;
import com.clevercollege.model.Location;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.Student;
import com.clevercollege.model.User;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.services.EmailService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
public class InsertDataController {

    @PostMapping("/insertData")
    public String insertData(HttpServletRequest request, String dataFromForm, String kindOfData, String kindOfPlace, String professorCf, Long id) {
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if(u == null) {
            return "no logged user";
        }
        
        String user_type = (String) session.getAttribute("user_type");
        
        if(user_type == null || !user_type.equals("admin"))
        	return "server error";

        if(dataFromForm == null || kindOfData == null) {
        	return "server error";
        }

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            Location l;
            Course c;
            if(kindOfData.equals("place")) {
                l = mapper.readValue(dataFromForm, Location.class);
                if (id == null && DatabaseManager.getInstance().getLocationDao().findByName(l.getName()) != null)
                    return "place already exists";
                else {
                	if(id == null) {
                		l.setId(DatabaseManager.getInstance().getIdBroker().getNextLocationId());
                	}
                	else {
						l.setId(id);
					}
                    if(kindOfPlace != null && id == null)
                        DatabaseManager.getInstance().getClassroomDao().saveOrUpdate(l);
                    else
                        DatabaseManager.getInstance().getLocationDao().saveOrUpdate(l);
                    DatabaseManager.getInstance().commit();
                    return "data inserted";
                }
            }
            else {
                c = mapper.readValue(dataFromForm, Course.class);
                
                if(c == null) {
                	return "server error";
                }
                
                if(professorCf == null || professorCf.isEmpty())
                	return "no prof selected";
                
                if(DatabaseManager.getInstance().getCourseDao().findByNameAndProfessor(c.getName(), professorCf) != null)
                    return "course already exists";
                else {
                	if(id == null) {
                		c.setId(DatabaseManager.getInstance().getIdBroker().getNextCourseId());
                	}
                	else {
                		c.setId(id);
                	}
                	User professor = DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(professorCf);
                	c.setLecturer(professor);
                    DatabaseManager.getInstance().getCourseDao().saveOrUpdate(c);
                    DatabaseManager.getInstance().commit();
                    return "data inserted";
                }
            }
        } catch (Exception e) {
        	e.printStackTrace();
            return "server error";
        }
    }

    @PostMapping("/insertUser")
    public String insertUser(HttpServletRequest request, String userFromForm, String kindOfUser, Boolean update) {
        HttpSession session = request.getSession();
        User userFromSession = (User) session.getAttribute("user");
        if(userFromSession == null) {
            return "no logged user";
        }
                
        String user_type = (String) session.getAttribute("user_type");
        
        if(user_type == null || !user_type.equals("admin"))
        	return "server error";

        if(userFromForm == null || kindOfUser == null || update == null)
            return "server error";

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User u;
        try {
            u = mapper.readValue(userFromForm, User.class);
        } catch (JsonProcessingException e) {
            return "server error";
        }
        
        if(u.getCf() == null || u.getFirstName() == null || u.getLastName() == null || u.getEmail() == null)
        	return "server error";

        String token = UUID.randomUUID().toString();
        String tmpPassword = BCrypt.hashpw(token, BCrypt.gensalt(12));

        if(!checkValidCf(u.getFirstName(), u.getLastName(), u.getCf()))
            return "cf not valid";

            try {
                if(!update && DatabaseManager.getInstance().getUserDao().findByEmail(u.getEmail()) != null)
                    return "email already exists";
                if(!update && DatabaseManager.getInstance().getUserDao().findByPrimaryKey(u.getCf()) != null)
                    return "user already exists";

                if(kindOfUser.equals("student")) {
                    Student s;
                    try {
                        s = mapper.readValue(userFromForm, Student.class);
                    } catch (JsonProcessingException e) {
                        return "server error";
                    }
                    if(s.getStudentNumber() == null)
                    	return "server error";
                    
                    if(!update && DatabaseManager.getInstance().getStudentDao().findByIdStudent(s.getStudentNumber()) != null)
                        return "idStudent already exists";
                    else {
                    	if(!update) {
	                        s.setPassword(tmpPassword);
	                        EmailService.getInstance().sendFirstPassword(s.getEmail(), token);
                    	}
                    	else {
                    		User oldUser = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(u.getCf());
                    		if(oldUser == null)
                    			return "server error";
                    		s.setPassword(oldUser.getPassword());
                    		s.setDescription(oldUser.getDescription());
                    		s.setProfilePicture(oldUser.getProfilePicture());
                    	}
                        DatabaseManager.getInstance().getStudentDao().saveOrUpdate(s);
                        DatabaseManager.getInstance().commit();
                        return "user inserted";
                    }
                }
                else {
                	if(!update) {
	                    u.setPassword(tmpPassword);
	                    EmailService.getInstance().sendFirstPassword(u.getEmail(), token);
                	}
                	else {
                		User oldUser = DatabaseManager.getInstance().getUserDao().findByPrimaryKey(u.getCf());
                		if(oldUser == null)
                			return "server error";
                		u.setPassword(oldUser.getPassword());
                		u.setDescription(oldUser.getDescription());
                		u.setProfilePicture(oldUser.getProfilePicture());
                	}
                    if(kindOfUser.equals("professor"))
                        DatabaseManager.getInstance().getProfessorDao().saveOrUpdate(u);
                    else
                       DatabaseManager.getInstance().getAdministratorDao().saveOrUpdate(u);
                    DatabaseManager.getInstance().commit();
                    return "user inserted";
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
    
	@PostMapping("/setFollowedCourse")
	public String setFollowedCourse(Long courseId, Boolean followed, HttpServletRequest req) {

		HttpSession session = req.getSession();

		String user_type = (String) session.getAttribute("user_type");

		if (user_type == null || !user_type.equals("student") || courseId == null || followed == null)
			return "error";

		Student student = (Student) session.getAttribute("user");

		if (student == null)
			return "error";

		try {
			if (followed) {
				DatabaseManager.getInstance().getStudentDao().followCourseForStudent(courseId, student.getCf());
				student.getFollowedCourses()
						.add(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(courseId));
			} else {
				DatabaseManager.getInstance().getStudentDao().unfollowCourseForStudent(courseId, student.getCf());
				student.getFollowedCourses()
						.remove(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(courseId));
			}
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			return "error";
		}

		return "ok";
	}

	@PostMapping("/checkCollidingActivities")
	public boolean checkCollidingActivities(Long activityId, HttpServletRequest req) {

		HttpSession session = req.getSession();

		String user_type = (String) session.getAttribute("user_type");

		if (user_type == null || !user_type.equals("student") || activityId == null)
			return false;

		Student student = (Student) session.getAttribute("user");

		if (student == null)
			return false;

		List<Seminar> collidingSeminars = null;
		List<SingleLesson> collidingSingleLessons = null;

		try {
			SingleLesson singleLesson = DatabaseManager.getInstance().getSingleLessonDao().findByPrimaryKey(activityId,
					true);

			if (singleLesson != null) {
				collidingSingleLessons = DatabaseManager.getInstance().getSingleLessonDao()
						.findByCollidingTimeForStudent(singleLesson.getDate(), singleLesson.getTime(),
								singleLesson.getLength(), student.getCf(), true);

				if (collidingSingleLessons.size() != 0)
					return true;

				collidingSeminars = DatabaseManager.getInstance().getSeminarDao().findByCollidingTimeForStudent(
						singleLesson.getDate(), singleLesson.getTime(), singleLesson.getLength(), student.getCf(),
						true);

				if (collidingSeminars.size() != 0)
					return true;
			} else {
				Seminar seminar = DatabaseManager.getInstance().getSeminarDao().findByPrimaryKey(activityId, true);

				if (seminar == null)
					return false;

				collidingSingleLessons = DatabaseManager.getInstance().getSingleLessonDao()
						.findByCollidingTimeForStudent(seminar.getDate(), seminar.getTime(), seminar.getLength(),
								student.getCf(), true);

				if (collidingSingleLessons.size() != 0)
					return true;

				collidingSeminars = DatabaseManager.getInstance().getSeminarDao().findByCollidingTimeForStudent(
						seminar.getDate(), seminar.getTime(), seminar.getLength(), student.getCf(), true);
				
				if(collidingSeminars.size() != 0)
					return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return false;

	}

	@PostMapping("/setBookedActivity")
	public String bookLesson(Long activityId, Boolean booked, HttpServletRequest req) {
		HttpSession session = req.getSession();

		String user_type = (String) session.getAttribute("user_type");

		if (user_type == null || !user_type.equals("student") || activityId == null || booked == null)
			return "error";

		Student student = (Student) session.getAttribute("user");

		if (student == null)
			return "error";

		try {
			if (booked) {
				if (!DatabaseManager.getInstance().getStudentDao().bookActivityForStudent(activityId, student.getCf())) {
					return "maximum capacity";
				}
			} else {
				DatabaseManager.getInstance().getStudentDao().unbookActivityForStudent(activityId, student.getCf());
			}
			DatabaseManager.getInstance().commit();
		} catch (SQLException e) {
			e.printStackTrace();
			return "error";
		}
		return "ok";
	}

}
