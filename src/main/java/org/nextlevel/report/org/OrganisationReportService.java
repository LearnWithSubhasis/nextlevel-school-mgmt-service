package org.nextlevel.report.org;

import org.nextlevel.grade.Grade;
import org.nextlevel.grade.GradeService;
import org.nextlevel.org.Organisation;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.report.grade.GradeReportService;
import org.nextlevel.report.section.SectionReport;
import org.nextlevel.report.section.SectionReportService;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.Section;
import org.nextlevel.section.SectionService;
import org.nextlevel.student.Student;
import org.nextlevel.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrganisationReportService {
    @Autowired
    private OrganisationService orgService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private GradeReportService gradeReportService;
    @Autowired
    private SectionReportService sectionReportService;

    public OrganisationReport getOrganisationReport(Long orgId) {
        OrganisationReport orgSummaryReport = new OrganisationReport();
        Organisation organisation = orgService.get(orgId);
        if (organisation == null) {
            return orgSummaryReport;
        }

        orgSummaryReport.setReportType("Organisation Summary Report");
        orgSummaryReport.setOrgId(orgId);

        List<School> schools = organisation.getSchools();

        List<School> greenZone = new ArrayList<>();
        List<School> yellowZone = new ArrayList<>();
        List<School> redZone = new ArrayList<>();

        int schoolCount = schools.size();
        orgSummaryReport.setNumberOfSchool(schoolCount);

        Long studentCount = 0L;
        Long teacherCount = 0L;
        if(null != schools && schools.size() > 0) {
            for (School school : schools) {
                boolean redZoneTouched = false;
                boolean yellowZoneTouched = false;
                school = schoolService.get(school.getSchoolId());
                List<Grade> grades = school.getGrades();
                if(null == grades || grades.size() == 0) {
                    grades = schoolService.listGrades(school);
                }

                if(null != grades && grades.size() > 0) {
                    for (Grade grade : grades) {
                        grade = gradeService.get(grade.getGradeId());
                        List<Section> sections = grade.getSections();
                        if(null != sections && sections.size() > 0) {
                            for (Section section : sections) {
                                section = sectionService.get(section.getSectionId());
                                List<Student> students = section.getStudents();
                                if(null != students) {
                                    studentCount += students.size();
                                } else {
                                    studentCount = 0l;
                                }

                                SectionReport sectionReport = sectionReportService.getSectionSummaryReport(section.getSectionId());
                                if (sectionReport.getAttendanceCountPresentPercentage() < 50) {
                                    redZoneTouched = true;
                                }

                                if (!redZoneTouched && sectionReport.getAttendanceCountPresentPercentage() < 80) {
                                    yellowZoneTouched = true;
                                }
                            }
                        }
                    }
                }

                List<Teacher> teachers = school.getTeachers();
                if(null == teachers || teachers.size() == 0) {
                    teachers = schoolService.listTeachers(school);
                }

                if(null != teachers && teachers.size() > 0) {
                    teacherCount += teachers.size();
                }

                if (redZoneTouched) {
                    redZone.add(school);
                } else if (yellowZoneTouched) {
                    yellowZone.add(school);
                } else {
                    greenZone.add(school);
                }
            }
        }

        orgSummaryReport.setStudentStrength(studentCount);
        orgSummaryReport.setFacultyStrength(teacherCount);

        orgSummaryReport.setGreenZone(greenZone);
        orgSummaryReport.setYellowZone(yellowZone);
        orgSummaryReport.setRedZone(redZone);

        return orgSummaryReport;
    }
}
