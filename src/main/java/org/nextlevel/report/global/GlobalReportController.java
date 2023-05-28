package org.nextlevel.report.global;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class GlobalReportController {
    @Autowired
    GlobalReportService grs;

    @GetMapping("/api/v1/report/global/summary")
    public ResponseEntity<GlobalReport> read() {
        GlobalReport globalSummaryReport = grs.getGlobalReport();
        return ResponseEntity.ok(globalSummaryReport);
    }
}
