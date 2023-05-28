package org.nextlevel.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.nextlevel.language.Language;
import org.nextlevel.teacher.Teacher;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column
	@Size(min = 4, max = 50)
	private String username;

	@NotNull
	@Size(min = 4, max = 100)
	private String password;

	@Column (unique = true, length = 50)
	@Size(max = 50)
	private String email;

	@Column(name = "phone_no", length = 15/*, unique = true, nullable = true*/)
	private Long phoneNo = -0L;

	@Column
	private Boolean enabled;

	@Column(name = "profile_image_url", length = 200)
	private String profileImageURL;

	//@Column(name = "oauth2_client_name")
	private String opauth2ClientName;

	@OneToOne(optional = true, mappedBy = "user", fetch = FetchType.EAGER,
			cascade = {CascadeType.MERGE})
	@JoinColumn(name = "userId")
	@JsonIgnore
	private Teacher teacher;

	@Column(name = "street_name_or_area")
	private String streetNameOrArea;

	@Column(name = "district")
	private String district;

	@Column(name = "city")
	private String city;
	@Column(name = "country")
	private String country;

	@Column(name = "contact_number")
	private Long contactNumber;

	@Column(name = "pin_code")
	private Integer pinCode;

//	@ManyToOne(optional = false, fetch = FetchType.EAGER,
//			cascade = {CascadeType.MERGE})
//	@JoinColumn(name = "orgUserId")
//	@JsonIgnore
//	private Organisation organisation;

//	@OneToOne
//	@PrimaryKeyJoinColumn(name = "orgId")
//	private Organisation organisation;

//	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
//	@JoinTable(
//			name = "users_roles",
//			joinColumns = @JoinColumn(name = "user_id"),
//			inverseJoinColumns = @JoinColumn(name = "role_id")
//			)
//	private Set<Role> roles = new HashSet<>();

	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
	@JoinTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id"))
	private Collection<Role> roles;

	@Enumerated(EnumType.STRING)
	@Column(name = "auth_type")
	private AuthenticationType authType;

	@Column(name = "last_login_datetime")
	@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss a")
	private Date userLoginDateTime;

	@Column(name = "first_name", length = 50)
	private String firstName;
	@Column(name = "last_name", length = 50)
	private String lastName;
	@Column(name = "mobile_no", length = 15)
	private String mobileNo;
	@Column
	private String designation;

	@Column(name = "license_aggr_acptd")
	private Boolean licenseAgreementAccepted = false;

	@ManyToMany
	@JoinTable(name = "users_languages",
			joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "language_id", referencedColumnName = "language_id"))
			//joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "language_id"))
	private List<Language> languages = new ArrayList<>();

//	@OneToOne (optional = false)
//	@JoinColumn(name = "event_reg_id")
//	private EventRegistration eventRegistration;

//	public List<Language> getLanguages() {
//		return languages;
//	}
//
//	public void setLanguages(List<Language> languages) {
//		this.languages = languages;
//	}
//
//	public Teacher getTeacher() {
//		return teacher;
//	}
//
//	public void setTeacher(Teacher teacher) {
//		this.teacher = teacher;
//	}
//
//	public Long getUserId() {
//		return userId;
//	}
//
//	public void setUserId(Long userId) {
//		this.userId = userId;
//	}
//
//	public String getUsername() {
//		return username;
//	}
//
//	public void setUsername(String username) {
//		this.username = username;
//	}
//
	//@JsonIgnore
	public String getPassword() {
		return password;
	}
//
//	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public boolean isEnabled() {
//		return enabled;
//	}
//
//	public void setEnabled(boolean enabled) {
//		this.enabled = enabled;
//	}
//
//	public Collection<Role> getRoles() {
//		return roles;
//	}
//
//	public void setRoles(Set<Role> roles) {
//		this.roles = roles;
//	}
//
//	public AuthenticationType getAuthType() {
//		return authType;
//	}
//
//	public void setAuthType(AuthenticationType authType) {
//		this.authType = authType;
//	}
//
//	public String getProfileImageURL() {
//		return profileImageURL;
//	}
//
//	public void setProfileImageURL(String profileImageURL) {
//		this.profileImageURL = profileImageURL;
//	}
//
//	public String getOpauth2ClientName() {
//		return opauth2ClientName;
//	}
//
//	public void setOpauth2ClientName(String opauth2ClientName) {
//		this.opauth2ClientName = opauth2ClientName;
//	}
//
//	public String getFirstName() {
//		return firstName;
//	}
//
//	public void setFirstName(String firstName) {
//		this.firstName = firstName;
//	}
//
//	public String getLastName() {
//		return lastName;
//	}
//
//	public void setLastName(String lastName) {
//		this.lastName = lastName;
//	}
//
//	public String getMobileNo() {
//		return mobileNo;
//	}
//
//	public void setMobileNo(String mobileNo) {
//		this.mobileNo = mobileNo;
//	}
//
//	public String getEmail() {
//		return email;
//	}
//
//	public void setEmail(String email) {
//		this.email = email;
//	}
//
//	public long getPhoneNo() {
//		return phoneNo;
//	}
//
//	public void setPhoneNo(long phoneNo) {
//		this.phoneNo = phoneNo;
//	}
//
////	public Organisation getOrganisation() {
////		return organisation;
////	}
////
////	public void setOrganisation(Organisation organisation) {
////		this.organisation = organisation;
////	}
//
//	public void setRoles(Collection<Role> roles) {
//		this.roles = roles;
//	}
//
//	public Date getUserLoginDateTime() {
//		return userLoginDateTime;
//	}
//
//	public void setUserLoginDateTime(Date userLoginDateTime) {
//		this.userLoginDateTime = userLoginDateTime;
//	}
}

/*
sub:113346740730685403004
name:Subhasis Khatua
given_name:Subhasis
family_name:Khatua
picture:https://lh3.googleusercontent.com/a/ALm5wu0dq0s4lPL2jdLbgWlQY8-zmlUxProO9hmlkqpa=s96-c
email:subhasis.khatua@gmail.com
email_verified:true
locale:en-GB
 */
