package org.nextlevel.event;

//import org.codehaus.jackson.annotate.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.nextlevel.eventregistration.EventRegistration;
import org.nextlevel.grade.Grade;
import org.nextlevel.org.Organisation;
import org.nextlevel.school.School;
import org.nextlevel.section.Section;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
	@Column(name="event_id")
	private Long eventId;

	@NotBlank(message = "Event Name is Mandatory!")
	private String name;

	@Column(name = "event_desc")
	@NotBlank(message = "Event Description is Mandatory!")
	private String eventDescription;

	@Column(name = "event_type")
	@NotBlank(message = "Event Type is Mandatory! (Global | Organisation | School | Grade | Section)")
	private String eventType;

	@Column(name = "event_cost")
	private Integer eventCost;

	@Column(name = "who_can_participate")
	@NotBlank(message = "Who can participate - is Mandatory! (Student | Teacher | Parents | NGO | Public)")
	private String whoCanParticipate;

	@Column(name = "event_start_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date eventStartTime;

	@Column(name = "event_end_time")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date eventEndTime;

	private Organisation organisation;
	private School school;
	private Grade grade;
	private Section section;
	private String color;

	@Column(name="date_created")
	@CreationTimestamp
	private LocalDateTime dateCreated;

	@Column(name="last_updated")
	@UpdateTimestamp
	private LocalDateTime lastUpdated;

	private List<EventRegistration> registrations;

	public Event() {}

	public Event(Long eventId) {
		this.eventId=eventId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Integer getEventCost() {
		return eventCost;
	}

	public void setEventCost(Integer eventCost) {
		this.eventCost = eventCost;
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

	public Date getEventStartTime() {
		return eventStartTime;
	}

	public void setEventStartTime(Date eventStartTime) {
		this.eventStartTime = eventStartTime;
	}

	public Date getEventEndTime() {
		return eventEndTime;
	}

	public void setEventEndTime(Date eventEndTime) {
		this.eventEndTime = eventEndTime;
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

	public String getColor() { return color; }
	public void setColor(String color) {
		this.color = color;
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
