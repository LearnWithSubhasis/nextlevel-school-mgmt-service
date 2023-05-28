package org.nextlevel.report.grade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class GradeReportController {
    @Autowired
    private GradeReportService gradeReportService;

    @GetMapping("/api/v1/report/grade/{id}/summary")
    public ResponseEntity<GradeReport> read(@PathVariable("id") Long id) {
        GradeReport gradeSummaryReport = gradeReportService.getGradeSummaryReport(id);
        return ResponseEntity.ok(gradeSummaryReport);
    }
}
