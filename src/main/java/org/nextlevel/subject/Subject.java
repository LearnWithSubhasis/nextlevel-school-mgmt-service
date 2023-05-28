package org.nextlevel.subject;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.school.School;
import org.nextlevel.teacher.Teacher;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "subjects")
public class Subject {
	@Id
	@Column(name="subject_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long subjectId;

	@NotBlank(message = "Subject Name is Mandatory!")
	private String name;

	@ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.ALL)
	@JoinColumn(name = "school_id", nullable = true)
	@JsonIgnore
	private School school;

	@ManyToMany(mappedBy = "subjects", targetEntity = Teacher.class, fetch = FetchType.LAZY)
	@JsonIgnore
	private List<Teacher> teachers;

	@Column(name="date_created")
	@CreationTimestamp
	private LocalDateTime dateCreated;

	@Column(name="last_updated")
	@UpdateTimestamp
	private LocalDateTime lastUpdated;

//	@ManyToOne(optional = true, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinColumn(name = "timetable_entity_id", unique = false)
//	@JsonIgnore
//	private TimetableEntry entry;

	public Subject() {}

	public Subject(Long subjectId) {
		this.subjectId=subjectId;
	}

	public Long getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Long subjectId) {
		this.subjectId = subjectId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public School getSchool() { return this.school; }

	public void setSchool(School school) { this.school = school; }

	public Collection<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}


	public LocalDateTime getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(LocalDateTime dateCreated) {
		this.dateCreated = dateCreated;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

}
