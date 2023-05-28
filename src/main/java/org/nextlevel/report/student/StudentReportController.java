package org.nextlevel.report.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class StudentReportController {
    @Autowired
    private StudentReportService studentReportService;

    @GetMapping("/api/v1/report/student/{id}/summary")
    public ResponseEntity<StudentReport> read(@PathVariable("id") Long id) {
        StudentReport studentSummaryReport = studentReportService.getStudentSummaryReport(id);
        return ResponseEntity.ok(studentSummaryReport);
    }
}
