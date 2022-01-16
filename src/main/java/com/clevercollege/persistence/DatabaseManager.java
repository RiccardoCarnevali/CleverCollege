package com.clevercollege.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.clevercollege.persistence.dao.*;
import com.clevercollege.persistence.dao.jdbc.*;

public class DatabaseManager {

	private static DatabaseManager instance = null;
	
	private Connection conn;
	
	private ActivityDao activityDao = null;
	private AdministratorDao administratorDao = null;
	private CheckInCheckOutDao checkInCheckOutDao = null;
	private ClassroomDao classroomDao = null;
	private CourseDao courseDao = null;
	private LessonDao lessonDao = null;
	private LocationDao locationDao = null;
	private NotificationTokenDao notificationTokenDao = null;
	private ProfessorDao professorDao = null;
	private RecoveryTokenDao recoveryTokenDao = null;
	private SeminarDao seminarDao = null;
	private SingleLessonDao singleLessonDao = null;
	private StudentDao studentDao = null;
	private UserDao userDao = null;
	private WeeklyLessonDao weeklyLessonDao = null;
	private MessageDao messageDao = null;
	
	private IdBroker idBroker = null;
	
	private DatabaseManager() {
		try {
			conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/clever_college_database", "postgres", "postgres");
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			System.err.println("Failed to connect to the database");
		}
	}
	
	public static DatabaseManager getInstance() {
		if(instance == null)
			instance = new DatabaseManager();
		return instance;
	}
	
	public ActivityDao getActivityDao() {
		if(activityDao == null)
			activityDao = new ActivityDaoJDBC(conn);
		return activityDao;
	}
	
	public AdministratorDao getAdministratorDao() {
		if(administratorDao == null)
			administratorDao = new AdministratorDaoJDBC(conn);
		return administratorDao;
	}
	
	public CheckInCheckOutDao getCheckInCheckOutDao() {
		if(checkInCheckOutDao == null)
			checkInCheckOutDao = new CheckInCheckOutDaoJDBC(conn);
		return checkInCheckOutDao;
	}
	
	public ClassroomDao getClassroomDao() {
		if(classroomDao == null)
			classroomDao = new ClassroomDaoJDBC(conn);
		return classroomDao;
	}
	
	public CourseDao getCourseDao() {
		if(courseDao == null)
			courseDao = new CourseDaoJDBC(conn);
		return courseDao;
	}
	
	public LessonDao getLessonDao() {
		if(lessonDao == null)
			lessonDao = new LessonDaoJDBC(conn);
		return lessonDao;
	}
	
	public LocationDao getLocationDao() {
		if(locationDao == null)
			locationDao = new LocationDaoJDBC(conn);
		return locationDao;
	}
	
	public NotificationTokenDao getNotificationTokenDao() {
		if(notificationTokenDao == null)
			notificationTokenDao = new NotificationTokenDaoJDBC(conn);
		return notificationTokenDao;
	}
	
	public ProfessorDao getProfessorDao() {
		if(professorDao == null)
			professorDao = new ProfessorDaoJDBC(conn);
		return professorDao;
	}
	
	public RecoveryTokenDao getRecoveryTokenDao() {
		if(recoveryTokenDao == null)
			recoveryTokenDao = new RecoveryTokenDaoJDBC(conn);
		return recoveryTokenDao;
	}
	
	public SeminarDao getSeminarDao() {
		if(seminarDao == null)
			seminarDao = new SeminarDaoJDBC(conn);
		return seminarDao;
	}
	
	public SingleLessonDao getSingleLessonDao() {
		if(singleLessonDao == null)
			singleLessonDao = new SingleLessonDaoJDBC(conn);
		return singleLessonDao;
	}
	
	public StudentDao getStudentDao() {
		if(studentDao == null)
			studentDao = new StudentDaoJDBC(conn);
		return studentDao;
	}
	
	public UserDao getUserDao() {
		if(userDao == null)
			userDao = new UserDaoJDBC(conn);
		return userDao;
	}
	
	public WeeklyLessonDao getWeeklyLessonDao() {
		if(weeklyLessonDao == null)
			weeklyLessonDao = new WeeklyLessonDaoJDBC(conn);
		return weeklyLessonDao;
	}

	public MessageDao getMessageDao() {
		if(messageDao == null)
			messageDao = new MessageDaoJDBC(conn);
		return messageDao;
	}
	
	public IdBroker getIdBroker() {
		if(idBroker == null)
			idBroker = new IdBrokerJDBC(conn);
		return idBroker;
	}
	
	public void commit() throws SQLException {
		conn.commit();
	}
}
