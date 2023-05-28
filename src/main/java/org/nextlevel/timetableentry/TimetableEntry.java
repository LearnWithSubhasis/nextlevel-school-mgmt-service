package org.nextlevel.timetableentry;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.nextlevel.subject.Subject;
import org.nextlevel.teacher.Teacher;
import org.nextlevel.timetable.Timetable;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table (name = "timetable_entry")
public class TimetableEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_entry_id", nullable = false)
    private Long timetableEntryId;

    @Column(name = "start_time")
    @JsonFormat(pattern="HH:mm:ss")
    //@DateTimeFormat(pattern = "HH:mm")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime startTime;

    @Column(name = "end_time")
    @JsonFormat(pattern="HH:mm:ss")
    //@DateTimeFormat(pattern = "HH:mm:ss")
    @JsonSerialize(using = LocalTimeSerializer.class)
    @JsonDeserialize(using = LocalTimeDeserializer.class)
    private LocalTime endTime;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "subject_id", unique = false)
    private Subject subject;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "teacher_id", unique = false)
//    @OneToOne(optional = false, mappedBy = "timeTableEntry", fetch = FetchType.LAZY)
//    @JoinColumn(name = "timetableEntryId")
    private Teacher teacher;

    @ManyToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "timetable_id")
    //@JsonIgnore
    private Timetable timetable;

    @Column(name = "day_of_week")
    private DayOfWeek dayOfWeek;

    @Transient
    private Long gradeID;
    @Transient
    private String gradeName;
    @Transient
    private Long sectionID;
    @Transient
    private String sectionName;

    public TimetableEntry() {}

    @JsonIgnore
    public Timetable getTimetable() {
        return timetable;
    }

    public void setTimetable(Timetable timetable) {
        this.timetable = timetable;
    }

    public Long getTimetableEntryId() {
        return timetableEntryId;
    }

    public void setTimetableEntryId(Long timetableEntryId) {
        this.timetableEntryId = timetableEntryId;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public int getDurationMinutes() {
        return durationMinutes;
    }

    public void setDurationMinutes(int durationMinutes) {
        this.durationMinutes = durationMinutes;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public DayOfWeek getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(DayOfWeek dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Long getGradeID() {
        return gradeID;
    }

    public String getGradeName() {
        return gradeName;
    }

    public Long getSectionID() {
        return sectionID;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setGradeID(Long gradeID) {
        this.gradeID = gradeID;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public void setSectionID(Long sectionID) {
        this.sectionID = sectionID;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    @Override
    public boolean equals(Object obj) {
        if (null != obj && obj instanceof TimetableEntry) {
            TimetableEntry entry2 = (TimetableEntry) obj;
            if (this.getTimetableEntryId().equals(entry2.getTimetableEntryId())) {
                //TODO::Check other conditions (startTime, endTime, duration)
                return true;
            }
        }

        return false;
    }
}
