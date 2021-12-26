package com.clevercollege.model;

public class WeeklyLesson extends Lesson {

	private int weekDay;
	private boolean disabled;
	private boolean disabledIndefinitely;

	public WeeklyLesson() {
		super();
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

}
