package org.nextlevel.report.section;

import org.nextlevel.attendance.Attendance;
import org.nextlevel.attendance.AttendanceStatus;
import org.nextlevel.section.Section;
import org.nextlevel.section.SectionService;
import org.nextlevel.student.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class SectionReportService {
    @Autowired
    private SectionService sectionService;

    private static final DecimalFormat df = new DecimalFormat("0.00");
    public SectionReport getSectionSummaryReport(Long sectionId) {
        SectionReport sectionSummaryReport = new SectionReport();
        Section section = sectionService.get(sectionId);
        if (section == null) {
            return sectionSummaryReport;
        }

        int currentMonth = LocalDate.now().getMonth().getValue();

        sectionSummaryReport.setReportType("Section Summary Report");
        sectionSummaryReport.setSectionId(sectionId);

        Long studentCount = 0L;
        Long attendanceCountTotal = 0L;
        Long attendanceCountPresent = 0L;
        Long attendanceCountAbsent = 0L;
        double attendanceCountPresentPercentage = 0F;
        double attendanceCountAbsentPercentage = 0F;
        double attendanceBoysPercentage = 0F;
        double attendanceGirlsPercentage = 0F;
        Long attendanceBoysCountTotal = 0L;
        Long attendanceGirlsCountTotal = 0L;
        Long boysStudentCount = 0L;
        Long girlsStudentCount = 0L;

        Long attendanceCountPresentTotalTerm1 = 0L;
        Long attendanceCountPresentTotalTerm2 = 0L;
        Long attendanceCountPresentTotalTerm3 = 0L;
        //Long attendanceCountPresentTotalTerm4 = 0L;
        Long attendanceCountAbsentTotalTerm1 = 0L;
        Long attendanceCountAbsentTotalTerm2 = 0L;
        Long attendanceCountAbsentTotalTerm3 = 0L;
        //Long attendanceCountAbsentTotalTerm4 = 0L;

//        List<Integer> weeklyPresent = new ArrayList<>();
//        List<Integer> weeklyAbsent = new ArrayList<>();
        List<Integer> weeklyPresentBoys = new ArrayList<>();
        List<Integer> weeklyPresentGirls = new ArrayList<>();
//        List<Integer> weeklyAbsentBoys = new ArrayList<>();
//        List<Integer> weeklyAbsentGirls = new ArrayList<>();

//        initialiseWeeklyReport(weeklyPresent);
//        initialiseWeeklyReport(weeklyAbsent);
        initialiseWeeklyReport(weeklyPresentBoys);
        initialiseWeeklyReport(weeklyPresentGirls);
//        initialiseWeeklyReport(weeklyAbsentBoys);
//        initialiseWeeklyReport(weeklyAbsentGirls);

//        List<Integer> weekDayPresent = new ArrayList<>();
//        List<Integer> weekDayAbsent = new ArrayList<>();
//        List<Integer> weekDayPresentBoys = new ArrayList<>();
//        List<Integer> weekDayPresentGirls = new ArrayList<>();
//        List<Integer> weekDayAbsentBoys = new ArrayList<>();
//        List<Integer> weekDayAbsentGirls = new ArrayList<>();

//        initialiseWeekDayReport(weekDayPresent);
//        initialiseWeekDayReport(weekDayAbsent);
//        initialiseWeekDayReport(weekDayPresentBoys);
//        initialiseWeekDayReport(weekDayPresentGirls);
//        initialiseWeekDayReport(weekDayAbsentBoys);
//        initialiseWeekDayReport(weekDayAbsentGirls);

        List<Student> greenZone = new ArrayList<>();
        List<Student> yellowZone = new ArrayList<>();
        List<Student> redZone = new ArrayList<>();

        List<Student> students = section.getStudents();
        studentCount += students.size();

//        sectionSummaryReport.setStudentStrength(students.size());
//        sectionSummaryReport.setAttendanceBoysCountTotal(attendanceBoysCountTotal);
//        sectionSummaryReport.setAttendanceGirlsCountTotal(attendanceGirlsCountTotal);
//        sectionSummaryReport.setAttendanceBoysPercentage(attendanceBoysPercentage);
//        sectionSummaryReport.setAttendanceGirlsPercentage(attendanceGirlsPercentage);
//        sectionSummaryReport.setAttendanceCountPresentPercentage(attendanceCountPresentPercentage);
//        sectionSummaryReport.setAttendanceCountAbsentPercentage(attendanceCountAbsentPercentage);
//        sectionSummaryReport.setGreenZone(greenZone);
//        sectionSummaryReport.setYellowZone(yellowZone);
//        sectionSummaryReport.setRedZone(redZone);

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
                    LocalDate finalDate = attendance.getAttendanceDate();
                    WeekFields weekFields = WeekFields.of(Locale.getDefault());
                    int weekNumber = finalDate.get(weekFields.weekOfWeekBasedYear()) - 1;
                    int weekDayNo = attendance.getWeekDayNo() - 1;

                    if(status == AttendanceStatus.P || status == AttendanceStatus.A || status == AttendanceStatus.NA ) {
                        attendanceCountTotalCurrent++;
                        if (status == AttendanceStatus.P) {
                            attendanceCountPresentCurrent++;

//                            int presentCount = weeklyPresent.get(weekNumber);
//                            presentCount += 1;
//                            weeklyPresent.set(weekNumber, presentCount);

                            int presentCount = 0;
//                            int presentCount = weekDayPresent.get(weekDayNo);
//                            weekDayPresent.set(weekDayNo, ++presentCount);

                            if (student.getSex().equalsIgnoreCase("Male")) {
                                attendanceBoysCountTotal++;

                                presentCount = weeklyPresentBoys.get(weekNumber);
                                presentCount += 1;
                                weeklyPresentBoys.set(weekNumber, presentCount);

//                                presentCount = weekDayPresentBoys.get(weekDayNo);
//                                weekDayPresentBoys.set(weekDayNo, ++presentCount);
                            } else if (student.getSex().equalsIgnoreCase("Female")) {
                                attendanceGirlsCountTotal++;

                                presentCount = weeklyPresentGirls.get(weekNumber);
                                presentCount += 1;
                                weeklyPresentGirls.set(weekNumber, presentCount);

//                                presentCount = weekDayPresentGirls.get(weekDayNo);
//                                weekDayPresentGirls.set(weekDayNo, ++presentCount);
                            }

                            if(attendance.getTerm().equalsIgnoreCase("Term 1")) {
                                attendanceCountPresentTotalTerm1++;
                            } else if (attendance.getTerm().equalsIgnoreCase("Term 2")) {
                                attendanceCountPresentTotalTerm2++;
                            } else if (attendance.getTerm().equalsIgnoreCase("Term 3")) {
                                attendanceCountPresentTotalTerm3++;
                            }
//                            } else if (attendance.getTerm().equalsIgnoreCase("Term 4")) {
//                                attendanceCountPresentTotalTerm4++;
//                            }
                        }
                        if (status == AttendanceStatus.A || status == AttendanceStatus.NA) {
                            attendanceCountAbsentCurrent++;

                            int absentCount = 0;
//                            int absentCount = weeklyAbsent.get(weekNumber);
//                            absentCount += 1;
//                            weeklyAbsent.set(weekNumber, absentCount);

//                            absentCount = weekDayAbsent.get(weekDayNo);
//                            weekDayAbsent.set(weekDayNo, ++absentCount);

//                            if (student.getSex().equalsIgnoreCase("Male")) {
//                                absentCount = weeklyAbsentBoys.get(weekNumber);
//                                absentCount += 1;
//                                weeklyAbsentBoys.set(weekNumber, absentCount);
//
//                                absentCount = weekDayAbsentBoys.get(weekDayNo);
//                                weekDayAbsentBoys.set(weekDayNo, ++absentCount);
//                            } else if (student.getSex().equalsIgnoreCase("Female")) {
//                                absentCount = weeklyAbsentGirls.get(weekNumber);
//                                absentCount += 1;
//                                weeklyAbsentGirls.set(weekNumber, absentCount);
//
//                                absentCount = weekDayAbsentGirls.get(weekDayNo);
//                                weekDayAbsentGirls.set(weekDayNo, ++absentCount);
//                            }

                            if(attendance.getTerm().equalsIgnoreCase("Term 1")) {
                                attendanceCountAbsentTotalTerm1++;
                            } else if (attendance.getTerm().equalsIgnoreCase("Term 2")) {
                                attendanceCountAbsentTotalTerm2++;
                            } else if (attendance.getTerm().equalsIgnoreCase("Term 3")) {
                                attendanceCountAbsentTotalTerm3++;
                            }
//                            } else if (attendance.getTerm().equalsIgnoreCase("Term 4")) {
//                                attendanceCountAbsentTotalTerm4++;
//                            }
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

        attendanceCountPresentPercentage = attendanceCountTotal==0? 0 : (double) Math.round(((attendanceCountPresent * 100) / attendanceCountTotal));
        attendanceCountAbsentPercentage = attendanceCountTotal==0? 0 : (double) Math.round(((attendanceCountAbsent * 100) / attendanceCountTotal));

        attendanceBoysPercentage = (attendanceBoysCountTotal+attendanceGirlsCountTotal)==0? 0 : (double) Math.round((attendanceBoysCountTotal * 100) / (attendanceBoysCountTotal+attendanceGirlsCountTotal));
        attendanceGirlsPercentage = (attendanceBoysCountTotal+attendanceGirlsCountTotal)==0? 0 : (double) Math.round((attendanceGirlsCountTotal * 100) / (attendanceBoysCountTotal+attendanceGirlsCountTotal));

        sectionSummaryReport.setStudentStrength(studentCount);
        sectionSummaryReport.setAttendanceBoysCountTotal(attendanceBoysCountTotal);
        sectionSummaryReport.setAttendanceGirlsCountTotal(attendanceGirlsCountTotal);
        sectionSummaryReport.setAttendanceBoysPercentage(attendanceBoysPercentage);
        sectionSummaryReport.setAttendanceGirlsPercentage(attendanceGirlsPercentage);
        sectionSummaryReport.setAttendanceCountPresentPercentage(attendanceCountPresentPercentage);
        sectionSummaryReport.setAttendanceCountAbsentPercentage(attendanceCountAbsentPercentage);
        sectionSummaryReport.setBoysStudentCount(boysStudentCount);
        sectionSummaryReport.setGirlsStudentCount(girlsStudentCount);
        sectionSummaryReport.setAttendanceCountTotal(attendanceCountTotal);
        sectionSummaryReport.setAttendanceCountPresent(attendanceCountPresent);
        sectionSummaryReport.setAttendanceCountAbsent(attendanceCountAbsent);
        sectionSummaryReport.setGreenZone(greenZone);
        sectionSummaryReport.setYellowZone(yellowZone);
        sectionSummaryReport.setRedZone(redZone);

        double attendanceCountPresentPercTerm1 = ((attendanceCountPresentTotalTerm1+attendanceCountAbsentTotalTerm1) == 0) ? 0.00 : (double) Math.round ((attendanceCountPresentTotalTerm1 * 100) /(attendanceCountPresentTotalTerm1+attendanceCountAbsentTotalTerm1));
        double attendanceCountPresentPercTerm2 = ((attendanceCountPresentTotalTerm2+attendanceCountAbsentTotalTerm2) == 0) ? 0.00 : (double) Math.round ((attendanceCountPresentTotalTerm2 * 100) /(attendanceCountPresentTotalTerm2+attendanceCountAbsentTotalTerm2));
        double attendanceCountPresentPercTerm3 = ((attendanceCountPresentTotalTerm3+attendanceCountAbsentTotalTerm3) == 0) ? 0.00 : (double) Math.round ((attendanceCountPresentTotalTerm3 * 100) /(attendanceCountPresentTotalTerm3+attendanceCountAbsentTotalTerm3));
        //float attendanceCountPresentPercTerm4 = ((attendanceCountPresentTotalTerm4+attendanceCountAbsentTotalTerm4) == 0) ? 0 : Float.valueOf((attendanceCountPresentTotalTerm4 * 100) /(attendanceCountPresentTotalTerm4+attendanceCountAbsentTotalTerm4));

        sectionSummaryReport.setTerm1Percentage(attendanceCountPresentPercTerm1);
        sectionSummaryReport.setTerm2Percentage(attendanceCountPresentPercTerm2);
        sectionSummaryReport.setTerm3Percentage(attendanceCountPresentPercTerm3);
        //sectionSummaryReport.setTerm4Percentage(attendanceCountPresentPercTerm4);

        double currentTermPercentageUp = 0F;
        if (currentMonth >= 5 && currentMonth <= 8) {
            currentTermPercentageUp = attendanceCountPresentPercTerm2 - attendanceCountPresentPercTerm1;
        } else if (currentMonth >= 9 /*&& currentMonth <= 12*/) {
            currentTermPercentageUp = attendanceCountPresentPercTerm3 - attendanceCountPresentPercTerm2;
        }
//        } else if (currentMonth >= 1 && currentMonth <= 3) {
//            currentTermPercentageUp = attendanceCountPresentPercTerm4 - attendanceCountPresentPercTerm3;
//        }

        sectionSummaryReport.setCurrentTermPercentageUp(currentTermPercentageUp);

//        sectionSummaryReport.setWeeklyPresent(weeklyPresent);
//        sectionSummaryReport.setWeeklyAbsent(weeklyAbsent);
//        sectionSummaryReport.setWeeklyAbsentBoys(weeklyAbsentBoys);
//        sectionSummaryReport.setWeeklyAbsentGirls(weeklyAbsentGirls);
        sectionSummaryReport.setWeeklyPresentBoys(weeklyPresentBoys);
        sectionSummaryReport.setWeeklyPresentGirls(weeklyPresentGirls);

//        sectionSummaryReport.setWeekDayPresent(weekDayPresent);
//        sectionSummaryReport.setWeekDayAbsent(weekDayAbsent);
//        sectionSummaryReport.setWeekDayPresentBoys(weekDayPresentBoys);
//        sectionSummaryReport.setWeekDayAbsentGirls(weekDayAbsentGirls);
//        sectionSummaryReport.setWeekDayPresentGirls(weekDayPresentGirls);
//        sectionSummaryReport.setWeekDayAbsentBoys(weekDayAbsentBoys);

        return sectionSummaryReport;
    }

    private void initialiseWeekDayReport(List<Integer> weeklyPresent) {
        for(int i=0; i<=6; i++) {
            weeklyPresent.add(i, 0);
        }
    }

    private void initialiseWeeklyReport(List<Integer> weeklyPresent) {
        for(int i=0; i<=52; i++) {
            weeklyPresent.add(i, 0);
        }
    }
}
