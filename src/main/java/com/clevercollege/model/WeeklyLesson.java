package com.clevercollege.model;

import java.sql.Time;
import java.util.List;

public class WeeklyLesson extends Lesson {

	private int weekDay;
	private boolean disabled;
	private boolean disabledIndefinitely;

	public WeeklyLesson() {
		super();
	}

	public WeeklyLesson(long id, Time time, int length, String description, User manager,
			Location classroom, Course course, int weekDay, boolean disabled, boolean disabledIndefinitely) {
		super(id, time, length, description, manager, null, classroom, course);
		this.weekDay = weekDay;
		this.disabled = disabled;
		this.disabledIndefinitely = disabledIndefinitely;
	}

	public int getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(int weekDay) {
		this.weekDay = weekDay;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public boolean isDisabledIndefinitely() {
		return disabledIndefinitely;
	}

	public void setDisabledIndefinitely(boolean disabledIndefinitely) {
		this.disabledIndefinitely = disabledIndefinitely;
	}

	@Override
	public List<Student> getBookers() {
		return null;
	}
}
