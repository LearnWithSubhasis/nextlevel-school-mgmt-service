package org.nextlevel.school;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.grade.Grade;
import org.nextlevel.org.Organisation;
import org.nextlevel.teacher.Teacher;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "schools")
public class School {
	@Id
	@Column(name="school_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long schoolId;

	@NotBlank(message = "School Name is Mandatory!")
	@Column(unique = true)
	private String name;
	//@NotBlank(message = "County Name is Mandatory!")
	private String county;

	private Organisation organisation;

	private List<Grade> grades;

	private List<Teacher> teachers;

	@Column(name = "school_address")
	private String address;

	@Column(name = "school_admin_email", unique = true, nullable = false)
	private String schoolAdminEmail;

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

	@Column(name = "contact_number")
	private Long contactNumber = 0L;

	@Column(name = "pin_code")
	private Integer pinCode;

	@Column
	private String profileImageURL;

	@Column(name = "school_signup_completed")
	private Boolean schoolSignupCompleted = false;

//	@OneToOne(mappedBy = "school")
//	private Principal schoolPrincipal;

//	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//	@JoinTable(name = "schools_principal",
//			joinColumns = @JoinColumn(name = "school_id"),
//			inverseJoinColumns = @JoinColumn(name = "principal_id"))
//	private Principal schoolPrincipal;
//
//	public Principal getSchoolPrincipal() {
//		return schoolPrincipal;
//	}
//
//	public void setSchoolPrincipal(Principal schoolPrincipal) {
//		this.schoolPrincipal = schoolPrincipal;
//	}

//	@OneToOne(optional = true, mappedBy = "schoolPrincipal", fetch = FetchType.EAGER,
//			cascade = {CascadeType.MERGE})
//	@JoinColumn(name = "teacherId")
//	@Column(name = "principal")
//	//@JsonIgnore
//	private Teacher principal;

//	@OneToOne(optional = true, mappedBy = "schoolAssistantPrincipal", fetch = FetchType.EAGER,
//			cascade = {CascadeType.MERGE})
//	@JoinColumn(name = "teacherId")
//	@Column(name = "assistant_principal")
//	//@JsonIgnore
//	private Teacher assistantPrincipal;

//	@OneToOne(optional = true, fetch = FetchType.LAZY)
//	@JoinColumn(name = "addressId")
//	@JsonIgnore
//	private Address address;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public School() {}

//	public School(Long schoolId, String county, Organisation organisation) {
//		this.schoolId=schoolId;
//		this.county=county;
//		this.organisation=organisation;
//	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(Long schoolId) {
		this.schoolId = schoolId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

//	@ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinColumn(name = "org_id", referencedColumnName = "orgId")
//	@JsonIgnore
//	public Organisation getOrganisation() {
//		return organisation;
//	}

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JoinColumn(name = "org_id", nullable = false)
	//@JsonIgnore //TODO: Otherwise POST payload won't detect organisation
	public Organisation getOrganisation() {
		return organisation;
	}
	
	public void setOrganisation(Organisation organisation) {
		this.organisation = organisation;
	}

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<Grade> getGrades() {
		return grades;
	}

	public void setGrades(List<Grade> grades) {
		this.grades = grades;
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

	@OneToMany(mappedBy = "school", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<Teacher> getTeachers() {
		return teachers;
	}

	public void setTeachers(List<Teacher> teachers) {
		this.teachers = teachers;
	}

	public String getSchoolAdminEmail() {
		return schoolAdminEmail;
	}

	public void setSchoolAdminEmail(String schoolAdminEmail) {
		this.schoolAdminEmail = schoolAdminEmail;
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

	public Long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(Long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public Integer getPinCode() {
		return pinCode;
	}

	public void setPinCode(Integer pinCode) {
		this.pinCode = pinCode;
	}

	public String getProfileImageURL() {
		return profileImageURL;
	}

	public void setProfileImageURL(String profileImageURL) {
		this.profileImageURL = profileImageURL;
	}

	public Boolean getSchoolSignupCompleted() {
		return schoolSignupCompleted;
	}

	public void setSchoolSignupCompleted(Boolean schoolSignupCompleted) {
		this.schoolSignupCompleted = schoolSignupCompleted;
	}

//	public Teacher getPrincipal() {
//		return principal;
//	}
//
//	public void setPrincipal(Teacher principal) {
//		this.principal = principal;
//	}

//	public Teacher getAssistantPrincipal() {
//		return assistantPrincipal;
//	}
//
//	public void setAssistantPrincipal(Teacher assistantPrincipal) {
//		this.assistantPrincipal = assistantPrincipal;
//	}
}
