package org.nextlevel.teacher;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import org.nextlevel.school.School;
import org.nextlevel.subject.Subject;
import org.nextlevel.user.User;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "teachers")
@Embeddable
public class Teacher {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="teacher_id")
	private Long teacherId;

	@ManyToOne(fetch = FetchType.EAGER, optional = true)
	@JoinColumn(name = "school_id", nullable = true)
	//@JsonIgnore
	private School school;

	@Column(nullable = false)
	private String name;

	@ManyToMany(fetch = FetchType.LAZY, targetEntity = Subject.class)
	@JoinTable(name = "teachers_subjects", joinColumns = @JoinColumn(name = "teacher_id", referencedColumnName = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "subject_id", referencedColumnName = "subject_id"))
	@JsonIgnore
	private List<Subject> subjects;

//	@Embedded
//	@OneToOne(optional = false, mappedBy = "teacher")
//	private User user;

	@OneToOne(optional = false, fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	@OneToOne(mappedBy = "teacher")
	private Principal teacherPrincipal;

	@Column(name="date_created")
	@CreationTimestamp
	private LocalDateTime dateCreated;

	@Column(name="last_updated")
	@UpdateTimestamp
	private LocalDateTime lastUpdated;

	@Column(name = "teacher_email")
	private String teacherEmail;

	@Column(name = "teacher_contact_no")
	private String teacherContactNo;

	@Column(name = "years_exp", columnDefinition="Decimal(4,1) default '0.0'")
	private Float yearsOfExperience;

	@Column
	private String qualification;

	@Column
	private String subject;

	@CsvDate(value = "dd/MM/yyyy")
	@CsvBindByPosition(position = 8)
	@Column(name = "dob")
	private LocalDate dateOfBirth;

	@Column
	private String gender;

	@Column(name = "employment_type")
	private String employmentType;

//	private Section section;

//	@OneToOne(optional = true)
//	@JoinColumn(name = "event_reg_id")
//	private EventRegistration eventRegistration;

//	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinTable(name = "teachers_principal",
//			joinColumns = @JoinColumn(name = "teacher_id"),
//			inverseJoinColumns = @JoinColumn(name = "principal_id"))
//	private Principal teacherPrincipal;
//
	public Principal getTeacherPrincipal() {
		return teacherPrincipal;
	}

	public void setTeacherPrincipal(Principal teacherPrincipal) {
		this.teacherPrincipal = teacherPrincipal;
	}

//	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinTable(name = "teachers_principal_school",
//			joinColumns = @JoinColumn(name = "teacher_teacher_id"),
//			inverseJoinColumns = @JoinColumn(name = "school_school_id"))
//	private School schoolPrincipal;

//	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinTable(name = "teachers_ap_school",
//			joinColumns = @JoinColumn(name = "teacher_teacher_id"),
//			inverseJoinColumns = @JoinColumn(name = "school_school_id"))
//	private School schoolAssistantPrincipal;

//	public School getSchoolAssistantPrincipal() {
//		return schoolAssistantPrincipal;
//	}
//
//	public void setSchoolAssistantPrincipal(School schoolAssistantPrincipal) {
//		this.schoolAssistantPrincipal = schoolAssistantPrincipal;
//	}

//	public School getSchoolPrincipal() {
//		return schoolPrincipal;
//	}
//
//	public void setSchoolPrincipal(School schoolPrincipal) {
//		this.schoolPrincipal = schoolPrincipal;
//	}

	public Teacher() {
	}

	public Teacher(Long teacherId, School school) {
		this.teacherId=teacherId;
		this.school=school;
	}

	public Long getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Long teacherId) {
		this.teacherId = teacherId;
	}

	public List<Subject> getSubjects() {
		return subjects;
	}

	public void setSubjects(List<Subject> subjects) {
		this.subjects = subjects;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

//	@ManyToOne(fetch = FetchType.LAZY, optional = false, cascade = CascadeType.ALL)
//	@JoinColumn(name = "school_id", nullable = false)
//	@JsonIgnore
	public School getSchool() {
		return school;
	}

	public void setSchool(School school) {
		this.school = school;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
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

	public String getTeacherEmail() {
		return teacherEmail;
	}

	public void setTeacherEmail(String teacherEmail) {
		this.teacherEmail = teacherEmail;
	}

	public Float getYearsOfExperience() {
		if(this.yearsOfExperience == null) {
			this.yearsOfExperience = 0.0f;
		}
		return yearsOfExperience;
	}

	public void setYearsOfExperience(Float yearsOfExperience) {
		if(yearsOfExperience == null) {
			yearsOfExperience = 0.0f;
		}
		this.yearsOfExperience = yearsOfExperience;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getEmploymentType() {
		return employmentType;
	}

	public void setEmploymentType(String employmentType) {
		this.employmentType = employmentType;
	}

	public String getTeacherContactNo() {
		return teacherContactNo;
	}

	public void setTeacherContactNo(String teacherContactNo) {
		this.teacherContactNo = teacherContactNo;
	}

//	@ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
//	@JoinColumn(name = "section_id", nullable = false)
//	//@JsonIgnore //TODO: Otherwise POST payload won't detect organisation
//	public Section getSection() {
//		return section;
//	}
//
//	public void setSection(Section section) {
//		this.section = section;
//	}

//	public EventRegistration getEventRegistration() {
//		return eventRegistration;
//	}
//
//	public void setEventRegistration(EventRegistration eventRegistration) {
//		this.eventRegistration = eventRegistration;
//	}


//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JsonIgnore
//	@JoinColumn(name = "subject_id", nullable = true)
//	//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//	public Section getSubject() {
//		return section;
//	}
//
//	public void setSection(Section section) {
//		this.section = section;
//	}
}
