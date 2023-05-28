package org.nextlevel.report.student;

import org.nextlevel.attendance.Attendance;
import org.nextlevel.attendance.AttendanceStatus;
import org.nextlevel.student.Student;
import org.nextlevel.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class StudentReportService {
    @Autowired
    private StudentService studentService;

    public StudentReport getStudentSummaryReport(Long studentId) {
        StudentReport studentSummaryReport = new StudentReport();
        Student student = studentService.get(studentId);
        if (student == null) {
            return studentSummaryReport;
        }

        studentSummaryReport.setReportType("Student Summary Report");
        studentSummaryReport.setStudentId(studentId);

        int currentMonth = LocalDate.now().getMonth().getValue();

        Long attendanceCountTotal = 0L;
        Long attendanceCountPresent = 0L;
        Long attendanceCountAbsent = 0L;
        Float attendanceCountPresentPercentage = 0F;
        Float attendanceCountAbsentPercentage = 0F;
        String zoneAttendance = "Green";
        Integer absentInCurrentMonth = 0;

        Long attendanceCountPresentTotalTerm1 = 0L;
        Long attendanceCountPresentTotalTerm2 = 0L;
        Long attendanceCountPresentTotalTerm3 = 0L;
        Long attendanceCountPresentTotalTerm4 = 0L;
        Long attendanceCountAbsentTotalTerm1 = 0L;
        Long attendanceCountAbsentTotalTerm2 = 0L;
        Long attendanceCountAbsentTotalTerm3 = 0L;
        Long attendanceCountAbsentTotalTerm4 = 0L;

        List<Attendance> attendanceList = student.getAttendance();

        if (null != attendanceList && attendanceList.size() > 0) {
            long attendanceCountTotalCurrent = 0;
            long attendanceCountPresentCurrent = 0;
            long attendanceCountAbsentCurrent = 0;

            for (Attendance attendance: attendanceList) {
                AttendanceStatus status = attendance.getAttendanceStatus();
                if(status == AttendanceStatus.P || status == AttendanceStatus.A || status == AttendanceStatus.NA ) {
                    attendanceCountTotalCurrent++;
                    if (status == AttendanceStatus.P) {
                        attendanceCountPresentCurrent++;

                        if(attendance.getTerm().equalsIgnoreCase("Term 1")) {
                            attendanceCountPresentTotalTerm1++;
                        } else if (attendance.getTerm().equalsIgnoreCase("Term 2")) {
                            attendanceCountPresentTotalTerm2++;
                        } else if (attendance.getTerm().equalsIgnoreCase("Term 3")) {
                            attendanceCountPresentTotalTerm3++;
                        } else if (attendance.getTerm().equalsIgnoreCase("Term 4")) {
                            attendanceCountPresentTotalTerm4++;
                        }
                    }
                    if (status == AttendanceStatus.A || status == AttendanceStatus.NA) {
                        attendanceCountAbsentCurrent++;

                        if(attendance.getAttendanceDate().getMonth().getValue() == currentMonth) {
                            absentInCurrentMonth++;
                        }

                        if(attendance.getTerm().equalsIgnoreCase("Term 1")) {
                            attendanceCountAbsentTotalTerm1++;
                        } else if (attendance.getTerm().equalsIgnoreCase("Term 2")) {
                            attendanceCountAbsentTotalTerm2++;
                        } else if (attendance.getTerm().equalsIgnoreCase("Term 3")) {
                            attendanceCountAbsentTotalTerm3++;
                        } else if (attendance.getTerm().equalsIgnoreCase("Term 4")) {
                            attendanceCountAbsentTotalTerm4++;
                        }
                    }
                }
            }
            attendanceCountTotal += attendanceCountTotalCurrent;
            attendanceCountPresent += attendanceCountPresentCurrent;
            attendanceCountAbsent += attendanceCountAbsentCurrent;

            if (attendanceCountPresent >= (attendanceCountTotal*0.8)) {
                zoneAttendance = "Green";
            } else if (attendanceCountPresent >= (attendanceCountTotal*0.5)) {
                zoneAttendance = "Yellow";
            } else {
                zoneAttendance = "Red";
            }
        } else {
            zoneAttendance = "Red";
        }

        attendanceCountPresentPercentage = attendanceCountTotal==0? 0 : Float.valueOf(((attendanceCountPresent * 100) / attendanceCountTotal));
        attendanceCountAbsentPercentage = attendanceCountTotal==0? 0 : Float.valueOf(((attendanceCountAbsent * 100) / attendanceCountTotal));

        float attendanceCountPresentPercTerm1 = ((attendanceCountPresentTotalTerm1+attendanceCountAbsentTotalTerm1) == 0) ? 0 : Float.valueOf((attendanceCountPresentTotalTerm1 * 100) /(attendanceCountPresentTotalTerm1+attendanceCountAbsentTotalTerm1));
        float attendanceCountPresentPercTerm2 = ((attendanceCountPresentTotalTerm2+attendanceCountAbsentTotalTerm2) == 0) ? 0 : Float.valueOf((attendanceCountPresentTotalTerm2 * 100) /(attendanceCountPresentTotalTerm2+attendanceCountAbsentTotalTerm2));
        float attendanceCountPresentPercTerm3 = ((attendanceCountPresentTotalTerm3+attendanceCountAbsentTotalTerm3) == 0) ? 0 : Float.valueOf((attendanceCountPresentTotalTerm3 * 100) /(attendanceCountPresentTotalTerm3+attendanceCountAbsentTotalTerm3));
        float attendanceCountPresentPercTerm4 = ((attendanceCountPresentTotalTerm4+attendanceCountAbsentTotalTerm4) == 0) ? 0 : Float.valueOf((attendanceCountPresentTotalTerm4 * 100) /(attendanceCountPresentTotalTerm4+attendanceCountAbsentTotalTerm4));

        studentSummaryReport.setAttendanceCountTotal(attendanceCountTotal);
        studentSummaryReport.setAttendanceCountPresent(attendanceCountPresent);
        studentSummaryReport.setAttendanceCountAbsent(attendanceCountAbsent);
        studentSummaryReport.setAttendanceCountPresentPercentage(attendanceCountPresentPercentage);
        studentSummaryReport.setAttendanceCountAbsentPercentage(attendanceCountAbsentPercentage);
        studentSummaryReport.setZoneAttendance(zoneAttendance);
        studentSummaryReport.setAbsentInCurrentMonth(absentInCurrentMonth);

        studentSummaryReport.setTerm1Percentage(attendanceCountPresentPercTerm1);
        studentSummaryReport.setTerm2Percentage(attendanceCountPresentPercTerm2);
        studentSummaryReport.setTerm3Percentage(attendanceCountPresentPercTerm3);
        //studentSummaryReport.setTerm4Percentage(attendanceCountPresentPercTerm4);

//        float currentTermPercentageUp = 0F;
//        if (currentMonth >= 7 && currentMonth <= 9) {
//            currentTermPercentageUp = attendanceCountPresentPercTerm2 - attendanceCountPresentPercTerm1;
//        } else if (currentMonth >= 10 && currentMonth <= 12) {
//            currentTermPercentageUp = attendanceCountPresentPercTerm3 - attendanceCountPresentPercTerm2;
//        } else if (currentMonth >= 1 && currentMonth <= 3) {
//            currentTermPercentageUp = attendanceCountPresentPercTerm4 - attendanceCountPresentPercTerm3;
//        }

        float currentTermPercentageUp = 0F;
        if (currentMonth >= 5 && currentMonth <= 8) {
            currentTermPercentageUp = attendanceCountPresentPercTerm2 - attendanceCountPresentPercTerm1;
        } else if (currentMonth >= 9 /*&& currentMonth <= 12*/) {
            currentTermPercentageUp = attendanceCountPresentPercTerm3 - attendanceCountPresentPercTerm2;
        }

        studentSummaryReport.setCurrentTermPercentageUp(currentTermPercentageUp);

        return studentSummaryReport;
    }
}
