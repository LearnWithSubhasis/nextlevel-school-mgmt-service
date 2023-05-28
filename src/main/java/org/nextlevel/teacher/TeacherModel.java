package org.nextlevel.teacher;

import org.nextlevel.school.School;
import org.nextlevel.subject.Subject;
import org.nextlevel.user.User;
import org.springframework.hateoas.RepresentationModel;

import java.util.List;

public class TeacherModel extends RepresentationModel<TeacherModel> {
	private Long teacherId;
	private School school;
	private String name;
	private List<Subject> subjects;
	private User user;
}
