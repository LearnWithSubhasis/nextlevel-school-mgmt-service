package org.nextlevel.student;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.attendance.Attendance;
import org.nextlevel.eventregistration.EventRegistration;
import org.nextlevel.section.Section;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Table(name = "students")
public class Student {
	@Column(name="student_id")
	private Long studentId;

	@NotBlank(message = "Student Name is Mandatory!")
	@Column
	private String name;

	@Column(nullable = false)
	private Integer age;

	@NotBlank(message = "Sex field is Mandatory!")
	@Column
	private String sex;

	@Column
	private String religion;

	@Column
	private String nationality;

	@Column(name="orphan_status")
	private String orphanStatus;

	@Column(name="social_needs")
	private String socialNeeds;

	private String repeaters;

	@Column(name="parent_name")
	private String parentName;

	@Column(name = "parent_email")
	private String parentEmail;

	@Column(name = "parent_contact_no")
	private Long parentContactNo;

	@Column(name = "role_no")
	//@NotBlank(message = "Role No field is Mandatory!")
	private Integer roleNo;

	@Column
	private String interests;

	private Section section;

	private List<Attendance> attendance;

	private List<EventRegistration> eventsRegistered;

	@Column(name="date_created")
	@CreationTimestamp
	private LocalDateTime dateCreated;

	@Column(name="last_updated")
	@UpdateTimestamp
	private LocalDateTime lastUpdated;

	@Column(name = "street_name_or_area")
	private String streetNameOrArea;

	@Column(name = "district")
	private String district;

	@Column(name = "city")
	private String city;
	@Column(name = "country")
	private String country;

	@Column(name = "pin_code")
	private Integer pinCode;

	@Column
	private String profileImageURL;

	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
	@JsonIgnore
	public List<EventRegistration> getEventsRegistered() {
		return eventsRegistered;
	}

	public void setEventsRegistered(List<EventRegistration> eventsRegistered) {
		this.eventsRegistered = eventsRegistered;
	}

//	@OneToOne(optional = true, mappedBy = "student", fetch = FetchType.EAGER)
//	@JoinColumn(name = "studentId")
//	private EventRegistration eventRegistration;

	public Student() {}

	public Student(Long studentId, Section section) {
		this.studentId=studentId;
		this.section=section;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getStudentId() {
		return studentId;
	}

	public void setStudentId(Long studentId) {
		this.studentId = studentId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "section_id", nullable = false)
	public Section getSection() {
		return section;
	}

	public void setSection(Section section) {
		this.section = section;
	}

	public String getSex() {
		if(null == sex) {
			sex = "NA";
		}
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public Integer getRoleNo() {
		return roleNo;
	}

	public void setRoleNo(Integer roleNo) {
		this.roleNo = roleNo;
	}

	@OneToMany(mappedBy = "student", fetch = FetchType.LAZY)
	@JsonIgnore
	public List<Attendance> getAttendance() {
		return attendance;
	}

	public void setAttendance(List<Attendance> attendance) {
		this.attendance = attendance;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getReligion() {
		return religion;
	}

	public void setReligion(String religion) {
		this.religion = religion;
	}

	public String getNationality() {
		return nationality;
	}

	public void setNationality(String nationality) {
		this.nationality = nationality;
	}

	public String getOrphanStatus() {
		return orphanStatus;
	}

	public void setOrphanStatus(String orphanStatus) {
		this.orphanStatus = orphanStatus;
	}

	public String getSocialNeeds() {
		return socialNeeds;
	}

	public void setSocialNeeds(String socialNeeds) {
		this.socialNeeds = socialNeeds;
	}

	public String getRepeaters() {
		return repeaters;
	}

	public void setRepeaters(String repeaters) {
		this.repeaters = repeaters;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentEmail() {
		return parentEmail;
	}

	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
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

	public Long getParentContactNo() {
		return parentContactNo;
	}

	public void setParentContactNo(Long parentContactNo) {
		this.parentContactNo = parentContactNo;
	}

	public String getStreetNameOrArea() {
		return streetNameOrArea;
	}

	public void setStreetNameOrArea(String streetNameOrArea) {
		this.streetNameOrArea = streetNameOrArea;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Integer getPinCode() {
		return pinCode;
	}

	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}

	public String getInterests() {
		return interests;
	}

	public void setInterests(String interests) {
		this.interests = interests;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

//	public EventRegistration getEventRegistration() {
//		return eventRegistration;
//	}
//
//	public void setEventRegistration(EventRegistration eventRegistration) {
//		this.eventRegistration = eventRegistration;
//	}
}
