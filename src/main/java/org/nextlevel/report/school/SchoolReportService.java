package org.nextlevel.report.school;

import org.nextlevel.attendance.Attendance;
import org.nextlevel.attendance.AttendanceStatus;
import org.nextlevel.grade.Grade;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.Section;
import org.nextlevel.student.Student;
import org.nextlevel.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolReportService {
    @Autowired
    private SchoolService schoolService;

    public SchoolReport getSchoolSummaryReport(Long schoolId) {
        SchoolReport schoolSummaryReport = new SchoolReport();
        School school = schoolService.get(schoolId);
        if (school == null) {
            return schoolSummaryReport;
        }

        schoolSummaryReport.setReportType("School Summary Report");
        schoolSummaryReport.setSchoolId(schoolId);

        Long studentCount = 0L;
        Long teacherCount = 0L;
        List<Teacher> teachers = school.getTeachers();
        if(null == teachers || teachers.size() == 0) {
            teachers = schoolService.listTeachers(school);
        }

        if(null != teachers && teachers.size() > 0) {
            teacherCount += teachers.size();
        }

        Long boysStudentCount = 0L;
        Long girlsStudentCount = 0L;

        Long attendanceCountTotal = 0L;
        Long attendanceCountPresent = 0L;
        Long attendanceCountAbsent = 0L;
        Float attendanceCountPresentPercentage = 0F;
        Float attendanceCountAbsentPercentage = 0F;
        Float attendanceBoysPercentage = 0F;
        Float attendanceGirlsPercentage = 0F;
        Long attendanceBoysCountTotal = 0L;
        Long attendanceGirlsCountTotal = 0L;
        List<Student> greenZone = new ArrayList<>();
        List<Student> yellowZone = new ArrayList<>();
        List<Student> redZone = new ArrayList<>();

        List<Grade> grades = school.getGrades();
        for (Grade grade: grades) {
            List<Section> sections = grade.getSections();
            for (Section section: sections) {
                List<Student> students = section.getStudents();

                studentCount += students.size();

                for (Student student: students) {
                    List<Attendance> attendanceList = student.getAttendance();

                    if (student.getSex().equalsIgnoreCase("Male")) {
                        boysStudentCount++;
                    } else if (student.getSex().equalsIgnoreCase("Female")) {
                        girlsStudentCount++;
                    }

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
                                    if (student.getSex().equalsIgnoreCase("Male")) {
                                        attendanceBoysCountTotal++;
                                    } else if (student.getSex().equalsIgnoreCase("Female")) {
                                        attendanceGirlsCountTotal++;
                                    }
                                }
                                if (status == AttendanceStatus.A || status == AttendanceStatus.NA) {
                                    attendanceCountAbsentCurrent++;
                                }
                            }
                        }
                        attendanceCountTotal += attendanceCountTotalCurrent;
                        attendanceCountPresent += attendanceCountPresentCurrent;
                        attendanceCountAbsent += attendanceCountAbsentCurrent;

                        if (attendanceCountPresent >= (attendanceCountTotal*0.8)) {
                            greenZone.add(student);
                        } else if (attendanceCountPresent >= (attendanceCountTotal*0.5)) {
                            yellowZone.add(student);
                        } else {
                            redZone.add(student);
                        }
                    } else {
                        redZone.add(student);
                    }
                }
            }
        }

        attendanceCountPresentPercentage = attendanceCountTotal==0? 0 : Float.valueOf(((attendanceCountPresent * 100) / attendanceCountTotal));
        attendanceCountAbsentPercentage = attendanceCountTotal==0? 0 : Float.valueOf(((attendanceCountAbsent * 100) / attendanceCountTotal));

        attendanceBoysPercentage = (attendanceBoysCountTotal+attendanceGirlsCountTotal)==0? 0 : Float.valueOf(((attendanceBoysCountTotal * 100) / (attendanceBoysCountTotal+attendanceGirlsCountTotal)));
        attendanceGirlsPercentage = (attendanceBoysCountTotal+attendanceGirlsCountTotal)==0? 0 : Float.valueOf(((attendanceGirlsCountTotal * 100) / (attendanceBoysCountTotal+attendanceGirlsCountTotal)));

        schoolSummaryReport.setStudentStrength(studentCount);
        schoolSummaryReport.setFacultyStrength(teacherCount);
        schoolSummaryReport.setAttendanceBoysCountTotal(attendanceBoysCountTotal);
        schoolSummaryReport.setAttendanceGirlsCountTotal(attendanceGirlsCountTotal);
        schoolSummaryReport.setAttendanceBoysPercentage(attendanceBoysPercentage);
        schoolSummaryReport.setAttendanceGirlsPercentage(attendanceGirlsPercentage);
        schoolSummaryReport.setAttendanceCountPresentPercentage(attendanceCountPresentPercentage);
        schoolSummaryReport.setAttendanceCountAbsentPercentage(attendanceCountAbsentPercentage);
        schoolSummaryReport.setAttendanceCountTotal(attendanceCountTotal);
        schoolSummaryReport.setAttendanceCountPresent(attendanceCountPresent);
        schoolSummaryReport.setAttendanceCountAbsent(attendanceCountAbsent);
        schoolSummaryReport.setBoysStudentCount(boysStudentCount);
        schoolSummaryReport.setGirlsStudentCount(girlsStudentCount);
        schoolSummaryReport.setGreenZone(greenZone);
        schoolSummaryReport.setYellowZone(yellowZone);
        schoolSummaryReport.setRedZone(redZone);

        return schoolSummaryReport;
    }
}
