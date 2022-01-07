class Activity {
	constructor(id, time, length, description, manager, bookers, classroom) {
		this.id = id;
		this.time = time;
		this.length = length;
		this.description = description;
		this.manager = manager;
		this.bookers = bookers;
		this.classroom = classroom;
	}
}

class Lesson extends Activity {
	constructor(id, time, length, description, manager, bookers, classroom, course) {
		super(id, time, length, description, manager, bookers, classroom);
		this.course = course;
	}
}

class SingleLesson extends Lesson {
	constructor(id, time, length, description, manager, bookers, classroom, course, date) {
		super(id, time, length, description, manager, bookers, classroom, course);
		this.date = date;
	}
}

class WeeklyLesson extends Lesson {
	constructor(id, time, length, description, manager, bookers, classroom, course, weekDay) {
		super(id, time, length, description, manager, bookers, classroom, course);
		this.weekDay = weekDay;
	}
}

class Seminar extends Activity {
	constructor(id, time, length, description, manager, bookers, classroom, date) {
		super(id, time, length, description, manager, bookers, classroom);
		this.date = date;
	}
}

class Course {
	constructor(id,name,lecturer) {
		this.id = id;
		this.name = name;
		this.lecturer = lecturer;
	}
} 

class Location {
	constructor(id,name,capacity) {
		this.id = id;
		this.name = name;
		this.capacity = capacity;
	}
}
