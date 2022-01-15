package com.clevercollege.persistence.dao.jdbc;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import com.clevercollege.model.Activity;
import com.clevercollege.model.Lesson;
import com.clevercollege.model.Seminar;
import com.clevercollege.model.SeminarProxy;
import com.clevercollege.model.SingleLesson;
import com.clevercollege.model.SingleLessonProxy;
import com.clevercollege.persistence.DatabaseManager;
import com.clevercollege.persistence.dao.SingleLessonDao;

public class SingleLessonDaoJDBC implements SingleLessonDao {

	private Connection conn;

	public SingleLessonDaoJDBC(Connection conn) {
		this.conn = conn;
	}

	@Override
	public List<SingleLesson> findAll(boolean lazy) throws SQLException {

		List<SingleLesson> singleLessons = new ArrayList<>();

		String query = "select * from single_lessons order by id";

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {

			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);

			SingleLesson singleLesson;

			if (lazy) {
				singleLesson = new SingleLessonProxy();
			} else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}

			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(lesson.getCourse());
			singleLesson.setDate(rs.getDate("lesson_date").toString());

			singleLessons.add(singleLesson);
		}

		return singleLessons;
	}

	@Override
	public SingleLesson findByPrimaryKey(long id, boolean lazy) throws SQLException {

		SingleLesson singleLesson = null;

		String query = "select * from single_lessons where id = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, id);

		ResultSet rs = st.executeQuery();

		if (rs.next()) {

			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);

			if (lazy) {
				singleLesson = new SingleLessonProxy();
			} else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}

			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(lesson.getCourse());
			singleLesson.setDate(rs.getDate("lesson_date").toString());
		}

		return singleLesson;
	}

	@Override
	public void saveOrUpdate(SingleLesson singleLesson) throws SQLException {

		String query = "select * from single_lessons where id = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, singleLesson.getId());

		ResultSet rs = st.executeQuery();

		DatabaseManager.getInstance().getLessonDao().saveOrUpdate(singleLesson);

		if (rs.next()) {

			query = "update single_lessons set " + "lesson_date = ?" + "where id = ?";

			PreparedStatement updateSt = conn.prepareStatement(query);

			updateSt.setDate(1, Date.valueOf(singleLesson.getDate()));
			updateSt.setLong(2, singleLesson.getId());

			updateSt.executeUpdate();
		} else {

			query = "insert into single_lessons values(?,?)";

			PreparedStatement insertSt = conn.prepareStatement(query);

			insertSt.setLong(1, singleLesson.getId());
			insertSt.setDate(2, Date.valueOf(singleLesson.getDate()));

			insertSt.executeUpdate();
		}
	}

	@Override
	public void delete(long id) throws SQLException {

		String query = "delete from single_lessons where id = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, id);

		st.executeUpdate();

		DatabaseManager.getInstance().getLessonDao().delete(id);
	}

	@Override
	public List<SingleLesson> findByProfessor(String cf, boolean lazy) throws SQLException {
		List<SingleLesson> singleLessons = new ArrayList<>();

		String query = "select * from single_lessons as x, activities as y, lessons as z where x.id = y.id and y.id = z.id and professor = ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			SingleLesson lesson;
			if (lazy) {
				lesson = new SingleLessonProxy();
			} else {
				lesson = new SingleLesson();
				lesson.setBookers(
						DatabaseManager.getInstance().getStudentDao().findBookersForActivity(rs.getLong("id"), true));
			}
			lesson.setId(rs.getLong("id"));
			lesson.setDate(rs.getDate("lesson_date").toString());
			lesson.setTime(rs.getTime("activity_time").toString());
			lesson.setLength(rs.getInt("activity_length"));
			lesson.setDescription(rs.getString("description"));
			lesson.setManager(DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(cf));
			lesson.setCourse(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(rs.getLong("course")));
			lesson.setClassroom(
					DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(rs.getLong("classroom")));

			singleLessons.add(lesson);
		}

		return singleLessons;
	}

	@Override
	public List<SingleLesson> findNotExpired(boolean lazy) throws SQLException {

		List<SingleLesson> singleLessons = new ArrayList<>();

		String query = "select * from single_lessons SL, activities A where SL.id = A.id and (lesson_date > current_date or (lesson_date = current_date and activity_time > current_time))";

		Statement st = conn.createStatement();

		ResultSet rs = st.executeQuery(query);

		while (rs.next()) {

			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);

			SingleLesson singleLesson;

			if (lazy) {
				singleLesson = new SingleLessonProxy();
			} else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}

			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(lesson.getCourse());
			singleLesson.setDate(rs.getDate("lesson_date").toString());

			singleLessons.add(singleLesson);
		}

		return singleLessons;
	}

	@Override
	public List<SingleLesson> findByCourseNotExpired(long courseId, boolean lazy) throws SQLException {

		List<SingleLesson> singleLessons = new ArrayList<>();

		String query = "select * from single_lessons SL, lessons L, activities A where SL.id = L.id and L.id = A.id and L.course = ? and (lesson_date > current_date or (lesson_date = current_date and activity_time > current_time))";

		PreparedStatement st = conn.prepareStatement(query);

		st.setLong(1, courseId);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);

			SingleLesson singleLesson;

			if (lazy) {
				singleLesson = new SingleLessonProxy();
			} else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}

			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(lesson.getCourse());
			singleLesson.setDate(rs.getDate("lesson_date").toString());

			singleLessons.add(singleLesson);
		}

		return singleLessons;
	}
	
	@Override
	public List<SingleLesson> findBookedByStudent(String studentCf, boolean lazy, int amount, int offset)
			throws SQLException {

		List<SingleLesson> singleLessons = new ArrayList<>();

		String query = "select * from single_lessons SL, books B where SL.id = B.activity and B.student = ? order by lesson_date desc limit ? offset ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, studentCf);
		st.setInt(2, amount);
		st.setInt(3, offset);
		
		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);

			SingleLesson singleLesson;

			if (lazy) {
				singleLesson = new SingleLessonProxy();
			} else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}

			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(lesson.getCourse());
			singleLesson.setDate(rs.getDate("lesson_date").toString());

			singleLessons.add(singleLesson);
		}
		return singleLessons;
	}

	@Override
	public List<SingleLesson> findBookedByStudentNotExpired(String studentCf, boolean lazy) throws SQLException {

		List<SingleLesson> singleLessons = new ArrayList<>();

		String query = "select * from activities A, single_lessons SL, books B where SL.id = A.id and A.id = B.activity and B.student = ? and (lesson_date > current_date or (lesson_date = current_date and activity_time > current_time))";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, studentCf);

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);

			SingleLesson singleLesson;

			if (lazy) {
				singleLesson = new SingleLessonProxy();
			} else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}

			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(lesson.getCourse());
			singleLesson.setDate(rs.getDate("lesson_date").toString());

			singleLessons.add(singleLesson);
		}

		return singleLessons;
	}

	@Override
	public List<SingleLesson> findBookedByStudentThisWeek(String studentCf, boolean lazy) throws SQLException {
		
		List<SingleLesson> singleLessons = new ArrayList<>();
		LocalDate today = LocalDate.now();
		
		String query = null;
		
		if (today.getDayOfWeek() == DayOfWeek.SUNDAY) {
			query = "select * from activities A, single_lessons SL, books B where SL.id = A.id and A.id = B.activity and B.student = ? and date_part('week', current_date) + 1 = date_part('week', SL.lesson_date) order by lesson_date, activity_time";
		}
		else {
			query = "select * from activities A, single_lessons SL, books B where SL.id = A.id and A.id = B.activity and B.student = ? and date_part('week', current_date) = date_part('week', SL.lesson_date) order by lesson_date, activity_time";
		}
		
		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, studentCf);
		
		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);
			
			SingleLesson singleLesson;

			if (lazy) {
				singleLesson = new SingleLessonProxy();
			} else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}

			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(lesson.getCourse());
			singleLesson.setDate(rs.getDate("lesson_date").toString());

			singleLessons.add(singleLesson);
		}
		
		return singleLessons;
	}
	
	@Override
	public List<SingleLesson> findByCollidingTimeForStudent(String date, String time, int length, String studentCf,
			boolean lazy) throws SQLException {

		List<SingleLesson> singleLessons = new ArrayList<>();

		String query = "select * from books B, single_lessons SL, activities A where B.activity = SL.id and SL.id = A.id and B.student = ? and "
				+ "lesson_date = ? and ((activity_time <= ? and ? < activity_time + interval '1 min' * activity_length) or "
				+ "(? <= activity_time and activity_time < ?))";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, studentCf);
		st.setDate(2, Date.valueOf(date));
		st.setTime(3, Time.valueOf(time));
		st.setTime(4, Time.valueOf(time));
		st.setTime(5, Time.valueOf(time));
		st.setTime(6, Time.valueOf(Time.valueOf(time).toLocalTime().plusMinutes(length)));

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			Lesson lesson = DatabaseManager.getInstance().getLessonDao().findByPrimaryKey(rs.getLong("id"), lazy);

			SingleLesson singleLesson;

			if (lazy) {
				singleLesson = new SingleLessonProxy();
			} else {
				singleLesson = new SingleLesson();
				singleLesson.setBookers(lesson.getBookers());
			}

			singleLesson.setId(lesson.getId());
			singleLesson.setTime(lesson.getTime());
			singleLesson.setLength(lesson.getLength());
			singleLesson.setDescription(lesson.getDescription());
			singleLesson.setManager(lesson.getManager());
			singleLesson.setClassroom(lesson.getClassroom());
			singleLesson.setCourse(lesson.getCourse());
			singleLesson.setDate(rs.getDate("lesson_date").toString());

			singleLessons.add(singleLesson);
		}

		return singleLessons;
	}

	@Override
	public List<SingleLesson> findActiveByProfessor(String cf, boolean lazy) throws SQLException {
		List<SingleLesson> singleLessons = new ArrayList<>();

		String query = "select * from single_lessons as x, activities as y, lessons as z "
				+ "where x.id = y.id and y.id = z.id and y.professor = ? and x.lesson_date >= ?";

		PreparedStatement st = conn.prepareStatement(query);

		st.setString(1, cf);
		st.setDate(2, Date.valueOf(LocalDate.now().minusDays(1)));

		ResultSet rs = st.executeQuery();

		while (rs.next()) {

			SingleLesson lesson;
			if (lazy) {
				lesson = new SingleLessonProxy();
			} else {
				lesson = new SingleLesson();
				lesson.setBookers(
						DatabaseManager.getInstance().getStudentDao().findBookersForActivity(rs.getLong("id"), true));
			}
			lesson.setId(rs.getLong("id"));
			lesson.setDate(rs.getDate("lesson_date").toString());
			lesson.setTime(rs.getTime("activity_time").toString());
			lesson.setLength(rs.getInt("activity_length"));
			lesson.setDescription(rs.getString("description"));
			lesson.setManager(DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(cf));
			lesson.setCourse(DatabaseManager.getInstance().getCourseDao().findByPrimaryKey(rs.getLong("course")));
			lesson.setClassroom(
					DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(rs.getLong("classroom")));

			singleLessons.add(lesson);
		}

		return singleLessons;
	}


	private List<Integer> findAllActivityLength() throws SQLException {
		List<Integer> lengths = new ArrayList<>();

		String query = "select a.activity_length from activities as a, single_lessons as s where s. id = a.id " +
				"and s.lesson_date = ?";

		PreparedStatement s = conn.prepareStatement(query);
		s.setDate(1, Date.valueOf(LocalDate.now()));
		ResultSet result = s.executeQuery();
		while(result.next()) {
			lengths.add(result.getInt(1));
		}
		return lengths;
	}

	@Override
	public Activity findByDateTimeClassroomProfessor(String cfProfessor, Long idClassroom) throws SQLException {

		Activity activity = null;
		List<Integer> activityLengths = findAllActivityLength();

		for(int i = 0; i < activityLengths.size(); i++) {

			String query = "select a.id, a.activity_time, a.activity_length, a.description, a.professor, a.classroom " +
					"from activities as a, single_lessons as s where s. id = a.id and s.lesson_date = ? and " +
					"classroom = ? and professor = ? and ((activity_time <= ? and ? < activity_time + interval '1 min' * activity_length) " +
					"or (? <= activity_time and activity_time < ?))";
			PreparedStatement st = conn.prepareStatement(query);

			st.setDate(1, Date.valueOf(LocalDate.now()));
			st.setLong(2, idClassroom);
			st.setString(3, cfProfessor);
			st.setTime(4, Time.valueOf(LocalTime.now()));
			st.setTime(5, Time.valueOf(LocalTime.now()));
			st.setTime(6, Time.valueOf(LocalTime.now()));
			st.setTime(7, Time.valueOf(LocalTime.now().plusMinutes(activityLengths.get(i))));


			ResultSet rs = st.executeQuery();
			if (rs.next()) {
				activity = new Activity();
				activity.setId(rs.getLong(1));
				activity.setTime(rs.getTime(2).toString());
				activity.setLength(rs.getInt(3));
				activity.setDescription(rs.getString(4));
				activity.setManager(DatabaseManager.getInstance().getProfessorDao().findByPrimaryKey(rs.getString(5)));
				activity.setClassroom(DatabaseManager.getInstance().getClassroomDao().findByPrimaryKey(rs.getLong(6)));
				activity.setBookers(DatabaseManager.getInstance().getStudentDao().findBookersForActivity(rs.getLong(1), true));
				return activity;
			}
		}
		return activity;
	}
}
