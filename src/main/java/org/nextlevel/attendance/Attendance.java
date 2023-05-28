package org.nextlevel.attendance;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.student.Student;
import org.nextlevel.teacher.Teacher;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "attendance")
public class Attendance {
	@Column(name="attendance_id")
	private Long attendanceId;

	@Column(name = "attendance_status")
	private AttendanceStatus attendanceStatus;
	//@JsonIgnore
	private Student student;

	@Column(name = "attendance_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate attendanceDate;

	@Column(name = "attendance_collection_date")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate attendanceCollectionDate;

	@Column(name = "attendance_collector")
	private Teacher attendanceCollector;

	private String term;

	@Column(name = "week_day_no")
	private int weekDayNo;

	public Attendance() {}

	public Attendance(Long attendanceId, Student student) {
		this.attendanceId=attendanceId;
		this.student=student;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getAttendanceId() {
		return attendanceId;
	}

	public void setAttendanceId(Long attendanceId) {
		this.attendanceId = attendanceId;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
	@JoinColumn(name = "student_id", nullable = false)
	//@JsonIgnore
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
	}

	public AttendanceStatus getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
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

	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "teacher_id", nullable = true)
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	@JsonIgnore
	public Teacher getAttendanceCollector() {
		return attendanceCollector;
	}

	public void setAttendanceCollector(Teacher attendanceCollector) {
		this.attendanceCollector = attendanceCollector;
	}

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
