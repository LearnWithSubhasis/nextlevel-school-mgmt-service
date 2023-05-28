package org.nextlevel.attendance;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class AttendanceRequest {
    private Long orgId;
    private Long schoolId;
    private Long gradeId;
    private Long sectionId;
    private Long studentId;
    private String term;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate attendanceCollectionDate;
    private List<Attendance> entries;

    public Long getOrgId() {
        return orgId;
    }

    public void setOrgId(Long orgId) {
        this.orgId = orgId;
    }

    public Long getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(Long schoolId) {
        this.schoolId = schoolId;
    }

    public Long getGradeId() {
        return gradeId;
    }

    public void setGradeId(Long gradeId) {
        this.gradeId = gradeId;
    }

    public Long getSectionId() {
        return sectionId;
    }

    public void setSectionId(Long sectionId) {
        this.sectionId = sectionId;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }


    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalDate getAttendanceCollectionDate() {
        return attendanceCollectionDate;
    }

    public void setAttendanceCollectionDate(LocalDate attendanceCollectionDate) {
        this.attendanceCollectionDate = attendanceCollectionDate;
    }
    public List<Attendance> getEntries() {
        return entries;
    }

    public void setEntries(List<Attendance> entries) {
        this.entries = entries;
    }
}
