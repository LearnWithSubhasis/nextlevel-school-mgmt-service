package org.nextlevel.grade;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.school.School;
import org.nextlevel.section.Section;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "grades")
public class Grade {
	@Id
	@Column(name="grade_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long gradeId;

	@NotBlank(message = "Grade Name is Mandatory!")
	@Column(unique = true)
	private String name;

	private School school;

	private List<Section> sections;

	@Column(name="date_created")
	@CreationTimestamp
	private LocalDateTime dateCreated;

	@Column(name="last_updated")
	@UpdateTimestamp
	private LocalDateTime lastUpdated;

	public Grade() {}

//	public Grade(Long gradeId, School school) {
//		this.gradeId=gradeId;
//		this.school=school;
//	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getGradeId() {
		return gradeId;
	}

	public void setGradeId(Long gradeId) {
		this.gradeId = gradeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.ALL)
//	//@JsonIgnore
//	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//	@JoinColumn(name = "school_id", nullable = false)
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "school_id", nullable = false)
	//@JsonIgnore //TODO: Otherwise POST payload won't detect organisation
	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	@OneToMany(mappedBy = "grade", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	public List<Section> getSections() {
		return sections;
	}

	public void setSections(List<Section> sections) {
		this.sections = sections;
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
