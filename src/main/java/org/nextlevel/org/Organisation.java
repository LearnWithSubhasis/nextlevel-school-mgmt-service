package org.nextlevel.org;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.event.Event;
import org.nextlevel.school.School;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "organisations", uniqueConstraints =
		{@UniqueConstraint(name = "unique_org_name", columnNames = {"name"})})
public class Organisation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "org_id")
	private Long orgId;

//	@NotBlank(message = "Org Name is Mandatory!")
//	@Column(unique = true, nullable = false)
//	@Unique
//	private String name;

	@NotBlank(message = "Org Name is Mandatory!")
	@Column(unique = true)
	private String name;

	private String county;

	private List<School> schools;

	private List<Event> events;

	@Column(name = "org_admin_email", unique = true, nullable = false)
	private String orgAdminEmail;

//	@Column(name = "org_phone", unique = true, nullable = false)
//	private long orgContactPhone;

	@Column(name = "org_address")
	private String address;

	@Column(name="date_created")
	@CreationTimestamp
	private LocalDateTime dateCreated;

	@Column(name="last_updated")
	@UpdateTimestamp
	private LocalDateTime lastUpdated;

	@Column(name="total_schools")
	private int totalSchools;

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

	@Column(name = "org_signup_completed")
	private Boolean orgSignupCompleted = false;

//	@OneToOne(mappedBy = "organisation")
//	private User user;

//	@OneToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
//	@JoinColumn(name = "orgUserId")
//	private User user;

//	@OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//	@JoinColumn(name = "address_id")
//	private Address address;

//	private List<Event> events;

	public Organisation() {
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getOrgId() {
		return orgId;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
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

	@OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JsonIgnore
	public List<School> getSchools() {
		return schools;
	}

	public void setSchools(List<School> schools) {
		this.schools = schools;
	}

	public String getOrgAdminEmail() {
		return orgAdminEmail;
	}

	public void setOrgAdminEmail(String orgAdminEmail) {
		this.orgAdminEmail = orgAdminEmail;
	}

//	public long getOrgContactPhone() {
//		return orgContactPhone;
//	}
//
//	public void setOrgContactPhone(long orgContactPhone) {
//		this.orgContactPhone = orgContactPhone;
//	}

	public String getAddress() {
		if (address == null || address.trim().length() == 0) {
			//TODO:: concatenate street address and other fields
		}

		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	@OneToMany(mappedBy = "organisation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnore
	public List<Event> getEvents() {
		return events;
	}

	public void setEvents(List<Event> events) {
		this.events = events;
	}

//	public User getUser() {
//		return user;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}

	public int getTotalSchools() {
		return totalSchools;
	}

 	public void setTotalSchools(int totalSchools) {
		try {
			this.totalSchools = this.getSchools().size();
		} catch (Exception ex) {
			this.totalSchools = totalSchools;
		}
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

	public Boolean getOrgSignupCompleted() {
		return orgSignupCompleted;
	}

	public void setOrgSignupCompleted(Boolean orgSignupCompleted) {
		this.orgSignupCompleted = orgSignupCompleted;
	}

//
//	@OneToMany(mappedBy = "organisation")
//	public List<Event> getEvents() {
//		return events;
//	}
//
//	public void setEvents(List<Event> events) {
//		this.events = events;
//	}

}
