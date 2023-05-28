package org.nextlevel.report.section;

import org.nextlevel.student.Student;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Component
public class SectionReport {
    private Long sectionId;
    //private Integer facultyStrength;
    private Long studentStrength;
    private String reportType;
    private Long attendanceCountTotal;
    private Long attendanceCountPresent;
    private Long attendanceCountAbsent;
    private Double attendanceCountPresentPercentage;
    private Double attendanceCountAbsentPercentage;
    private Double attendanceBoysPercentage;
    private Double attendanceGirlsPercentage;
    private Long attendanceBoysCountTotal;
    private Long attendanceGirlsCountTotal;
    private Long boysStudentCount;
    private Long girlsStudentCount;
    private Long attendanceCountUnrecognisedTotal = 0L;

    private Double term1Percentage;
    private Double term2Percentage;
    private Double term3Percentage;
    private Double currentTermPercentageUp;

    private List<Student> greenZone;
    private List<Student> yellowZone;
    private List<Student> redZone;

    private List<Integer> weeklyPresent;
    private List<Integer> weeklyAbsent;
    private List<Integer> weeklyPresentBoys;
    private List<Integer> weeklyPresentGirls;
    private List<Integer> weeklyAbsentBoys;
    private List<Integer> weeklyAbsentGirls;

    private List<Integer> weekDayPresent;
    private List<Integer> weekDayAbsent;
    private List<Integer> weekDayPresentBoys;
    private List<Integer> weekDayPresentGirls;
    private List<Integer> weekDayAbsentBoys;
    private List<Integer> weekDayAbsentGirls;
}
