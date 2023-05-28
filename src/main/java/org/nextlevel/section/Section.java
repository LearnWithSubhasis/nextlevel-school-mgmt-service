package org.nextlevel.section;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.grade.Grade;
import org.nextlevel.student.Student;
import org.nextlevel.subject.Subject;
import org.nextlevel.timetable.Timetable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;


@Entity
@Table(name = "sections")
public class Section {
	@Id
	@Column(name="section_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long sectionId;

	@NotBlank(message = "Section Name is Mandatory!")
	@Column(unique = true)
	private String name;

	private Grade grade;

	private List<Student> students;

//	public Timetable getTimetable() {
//		return timetable;
//	}

	@OneToOne(optional = true, mappedBy = "section", fetch = FetchType.LAZY)
	@JoinColumn(name = "sectionId")
	@JsonIgnore
	private Timetable timetable;

//	private List<Teacher> teachers;

	public Section() {}

	public Section(Long sectionId, Grade grade) {
		this.sectionId = sectionId;
		this.grade = grade;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId(Long sectionId) {
		this.sectionId = sectionId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	//@JsonIgnore
	@JoinColumn(name = "grade_id", nullable = false)
	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public Grade getGrade() {
		return grade;
	}

	public void setGrade(Grade grade) {
		this.grade = grade;
	}

	@OneToMany(mappedBy = "section", fetch = FetchType.LAZY)
	@JsonIgnore
	public List<Student> getStudents() {
		return students;
	}

	public void setStudents(List<Student> students) {
		this.students = students;
	}

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "sections_subjects", joinColumns = @JoinColumn(name = "section_id", referencedColumnName = "section_id"), inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "subject_id"))
	private Collection<Subject> subjects;

//	@OneToMany(mappedBy = "section", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//	@JsonIgnore
//	public List<Teacher> getTeachers() {
//		return teachers;
//	}
//
//	public void setTeachers(List<Teacher> teachers) {
//		this.teachers = teachers;
//	}
}
