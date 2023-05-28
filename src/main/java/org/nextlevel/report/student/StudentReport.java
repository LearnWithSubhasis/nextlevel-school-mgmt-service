package org.nextlevel.report.student;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class StudentReport {
    private Long studentId;
    private String reportType;
    private Long attendanceCountTotal;
    private Long attendanceCountPresent;
    private Long attendanceCountAbsent;
    private Float attendanceCountPresentPercentage;
    private Float attendanceCountAbsentPercentage;
    private Long attendanceCountUnrecognisedTotal = 0L;
    private String zoneAttendance;
    private Integer absentInCurrentMonth;
    private Float term1Percentage;
    private Float term2Percentage;
    private Float term3Percentage;
//    private Float term4Percentage;
    private Float currentTermPercentageUp;
}
