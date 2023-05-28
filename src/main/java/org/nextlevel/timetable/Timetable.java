package org.nextlevel.timetable;

//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.nextlevel.section.Section;
import org.nextlevel.timetableentry.TimetableEntry;
import org.springframework.format.annotation.DateTimeFormat;
//import org.codehaus.jackson.annotate.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table (name = "timetable")
public class Timetable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timetable_id", nullable = false)
    private Long timetableId;

    @OneToMany(mappedBy = "timetable", fetch = FetchType.EAGER)
    //@JsonIgnore
    private List<TimetableEntry> entries = new ArrayList<>();

    @OneToOne(optional = false, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "section_id")
    private Section section;

    @Column(name = "start_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "end_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    public Timetable(){}

    public Timetable(Long timetableId, Section section, List<TimetableEntry> entries) {
        this.timetableId = timetableId;
        this.section = section;
        this.entries = entries;
    }

    //@JsonIgnore
    public List<TimetableEntry> getEntries() {
        return entries;
    }

    //@JsonProperty
    public void setEntries(List<TimetableEntry> entries) {
        this.entries = entries;
    }

    public Long getTimetableId() {
        return timetableId;
    }

    public void setTimetableId(Long timetableId) {
        this.timetableId = timetableId;
    }

    public void addEntry(TimetableEntry entry) {
        //TODO::Check this entry is not duplicate / already exists, or there is no overlap with existing entries
        this.entries.add(entry);
    }

    public Section getSection() {
        return this.section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
