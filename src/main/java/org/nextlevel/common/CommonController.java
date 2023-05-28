package org.nextlevel.common;

import org.nextlevel.attendance.AttendanceService;
import org.nextlevel.grade.GradeService;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.SectionService;
import org.nextlevel.student.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

@Component
public class CommonController {
    @Autowired
    private AttendanceService attendanceService;
    @Autowired
    private SectionService sectionService;
    @Autowired
    private SchoolService schoolService;
    @Autowired
    private OrganisationService orgService;
    @Autowired
    private GradeService gradeService;
    @Autowired
    private StudentService studentService;

    public Model setModelAttributes(Model model, Long orgId, Long schoolId, Long gradeId, Long sectionId
            , Long studentId) {
        if (orgId > 0) {
            model.addAttribute("organisation", orgService.get(orgId).getName());
        }
        if (schoolId > 0) {
            model.addAttribute("school", schoolService.get(schoolId).getName());
        }
        if (gradeId > 0) {
            model.addAttribute("grade", gradeService.get(gradeId).getName());
        }
        if (sectionId > 0) {
            model.addAttribute("section", sectionService.get(sectionId).getName());
        }
        if (studentId > 0) {
            model.addAttribute("student", studentService.get(studentId).getName());
        }

        return model;
    }
}
