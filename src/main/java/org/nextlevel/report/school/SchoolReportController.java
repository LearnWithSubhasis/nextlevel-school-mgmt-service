package org.nextlevel.report.school;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class SchoolReportController {
    @Autowired
    private SchoolReportService schoolReportService;

    @GetMapping("/api/v1/report/school/{id}/summary")
    public ResponseEntity<SchoolReport> read(@PathVariable("id") Long id) {
        SchoolReport schoolSummaryReport = schoolReportService.getSchoolSummaryReport(id);
        return ResponseEntity.ok(schoolSummaryReport);
    }
}
