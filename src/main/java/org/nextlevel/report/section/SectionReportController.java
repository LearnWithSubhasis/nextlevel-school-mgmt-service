package org.nextlevel.report.section;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class SectionReportController {
    @Autowired
    private SectionReportService sectionReportService;

    @GetMapping("/api/v1/report/section/{id}/summary")
    public ResponseEntity<SectionReport> read(@PathVariable("id") Long id) {
        SectionReport sectionSummaryReport = sectionReportService.getSectionSummaryReport(id);
        return ResponseEntity.ok(sectionSummaryReport);
    }
}
