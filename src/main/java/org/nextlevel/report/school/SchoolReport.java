package org.nextlevel.report.school;

import org.nextlevel.student.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class SchoolReport {
    private Long schoolId;
    private Long facultyStrength;
    private Long studentStrength;
    private String reportType;
    private Long attendanceCountTotal;
    private Long attendanceCountPresent;
    private Long attendanceCountAbsent;
    private Float attendanceCountPresentPercentage;
    private Float attendanceCountAbsentPercentage;
    private Float attendanceBoysPercentage;
    private Float attendanceGirlsPercentage;
    private Long attendanceBoysCountTotal;
    private Long attendanceGirlsCountTotal;
    private Long boysStudentCount;
    private Long girlsStudentCount;
    private List<Student> greenZone;
    private List<Student> yellowZone;
    private List<Student> redZone;
}
