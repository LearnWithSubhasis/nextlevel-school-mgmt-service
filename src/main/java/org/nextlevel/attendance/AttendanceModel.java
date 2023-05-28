package org.nextlevel.attendance;

import org.nextlevel.student.Student;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;

public class AttendanceModel extends RepresentationModel<AttendanceModel> {
	private Long attendanceId;
	private AttendanceStatus attendanceStatus;
	private Student student;
	private LocalDate attendanceDate;
	private LocalDate attendanceCollectionDate;
	//private Teacher attendanceCollector;
	private String term;
	private int weekDayNo;

	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}

	public AttendanceStatus getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public LocalDate getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(LocalDate attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public LocalDate getAttendanceCollectionDate() {
		return attendanceCollectionDate;
	}

	public void setAttendanceCollectionDate(LocalDate attendanceCollectionDate) {
		this.attendanceCollectionDate = attendanceCollectionDate;
	}

//	public Teacher getAttendanceCollector() {
//		return attendanceCollector;
//	}
//
//	public void setAttendanceCollector(Teacher attendanceCollector) {
//		this.attendanceCollector = attendanceCollector;
//	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public int getWeekDayNo() {
		return weekDayNo;
	}

	public void setWeekDayNo(int weekDayNo) {
		this.weekDayNo = weekDayNo;
	}

}
