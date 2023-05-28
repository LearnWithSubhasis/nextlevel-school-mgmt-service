package org.nextlevel.security.jwt;

import org.nextlevel.teacher.Teacher;
import org.nextlevel.user.User;

import java.io.Serializable;

public class JwtResponseMobile implements Serializable {

	private static final long serialVersionUID = -8091879091924046844L;
	private final String jwttoken;

	private User user;

	private Teacher teacher;

	public JwtResponseMobile(String jwttoken, Teacher teacher) {
		this.jwttoken = jwttoken;
		this.user = user;
		this.teacher = teacher;
	}

	public String getToken() {
		return this.jwttoken;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Teacher getTeacher() {
		return teacher;
	}

	public void setTeacher(Teacher teacher) {
		this.teacher = teacher;
	}
}