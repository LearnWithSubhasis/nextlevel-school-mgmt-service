package org.nextlevel.training;

//import org.codehaus.jackson.annotate.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.nextlevel.eventregistration.EventRegistration;
import org.nextlevel.grade.Grade;
import org.nextlevel.org.Organisation;
import org.nextlevel.school.School;
import org.nextlevel.section.Section;
import org.nextlevel.student.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "trainings")
public class Training {
	@Column(name="training_id")
	private Long trainingId;

	@NotBlank(message = "Training Name is Mandatory!")
	private String name;

	@Column(name = "training_desc")
	@NotBlank(message = "Training Description is Mandatory!")
	private String trainingDescription;

	@Column(name = "training_type")
	@NotBlank(message = "Training Type is Mandatory! (Global | Organisation | School | Grade | Section)")
	private String trainingType;

	@Column(name = "training_cost")
	private Integer trainingCost;

	@Column(name = "who_can_participate")
	@NotBlank(message = "Who can participate - is Mandatory! (Student | Teacher | Parents | NGO | Public)")
	private String whoCanParticipate;

	@Column(name = "training_start_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date trainingStartTime;

	@Column(name = "training_end_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date trainingEndTime;

	private Organisation organisation;
	private School school;
	private Grade grade;
	private Section section;
	private Student student;

	@Column(name="date_created")
	@CreationTimestamp
	private LocalDateTime dateCreated;

	@Column(name="last_updated")
	@UpdateTimestamp
	private LocalDateTime lastUpdated;

	private List<EventRegistration> registrations;

	public Training(Long trainingId) {
		this.trainingId=trainingId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(Long trainingId) {
		this.trainingId = trainingId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTrainingDescription() {
		return trainingDescription;
	}

	public void setTrainingDescription(String trainingDescription) {
		this.trainingDescription = trainingDescription;
	}

	public String getTrainingType() {
		return trainingType;
	}

	public void setTrainingType(String trainingType) {
		this.trainingType = trainingType;
	}

	public Integer getTrainingCost() {
		return trainingCost;
	}

	public void setTrainingCost(Integer trainingCost) {
		this.trainingCost = trainingCost;
	}

	public String getWhoCanParticipate() {
		return whoCanParticipate;
	}

	public void setWhoCanParticipate(String whoCanParticipate) {
		this.whoCanParticipate = whoCanParticipate;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "org_id", nullable = true)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public Organisation getOrganisation() {
		return organisation;
	}

	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}


	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "school_id", nullable = true)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public Date getTrainingStartTime() {
		return trainingStartTime;
	}

	public void setTrainingStartTime(Date trainingStartTime) {
		this.trainingStartTime = trainingStartTime;
	}

	public Date getTrainingEndTime() {
		return trainingEndTime;
	}

	public void setThainingEndTime(Date trainingEndTime) {
		this.trainingEndTime = trainingEndTime;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "grade_id", nullable = true)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        this.grade = grade;
    }

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "section_id", nullable = true)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name = "student_id", nullable = true)
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	public Student getStudent() {
		return student;
	}

	public void setStudent(Student student) {
		this.student = student;
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

	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EventRegistration> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<EventRegistration> registrations) {
		this.registrations = registrations;
	}
}
