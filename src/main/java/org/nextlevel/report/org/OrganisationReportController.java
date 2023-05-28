package org.nextlevel.report.org;

import org.nextlevel.attendance.AttendanceService;
import org.nextlevel.event.EventService;
import org.nextlevel.grade.GradeService;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.SectionService;
import org.nextlevel.student.StudentService;
import org.nextlevel.teacher.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class OrganisationReportController {
    @Autowired
    private OrganisationService orgService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private EventService eventService;
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private OrganisationReportService orgReportService;

    @GetMapping("/api/v1/report/org/{id}/summary")
    public ResponseEntity<OrganisationReport> read(@PathVariable("id") Long id) {
        OrganisationReport orgSummaryReport = orgReportService.getOrganisationReport(id);
        return ResponseEntity.ok(orgSummaryReport);
    }
}
