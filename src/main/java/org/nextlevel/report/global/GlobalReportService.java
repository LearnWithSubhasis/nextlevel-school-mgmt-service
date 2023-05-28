package org.nextlevel.report.global;

import org.nextlevel.org.Organisation;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.report.org.OrganisationReport;
import org.nextlevel.report.org.OrganisationReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GlobalReportService {
    @Autowired
    private OrganisationReportService ors;
    @Autowired
    private OrganisationService organisationService;

    public GlobalReport getGlobalReport() {
        GlobalReport globalReport = new GlobalReport();
        globalReport.setReportType("Global Summary Report");

        Long facultyStrength = 0L;
        Long studentStrength = 0L;
        Integer schoolCount = 0;

        List<Organisation> greenZone = new ArrayList<>();
        List<Organisation> yellowZone = new ArrayList<>();
        List<Organisation> redZone = new ArrayList<>();

        List<Organisation> orgs = organisationService.listAll();
        for (Organisation org: orgs) {
            OrganisationReport or = ors.getOrganisationReport(org.getOrgId());
            if (null != or) {
                facultyStrength += or.getFacultyStrength();
                studentStrength += or.getStudentStrength();
                schoolCount += or.getNumberOfSchool();
            }

            if (or.getRedZone().size() > 0) {
                redZone.add(org);
            } else if (or.getYellowZone().size() > 0) {
                yellowZone.add(org);
            } else {
                greenZone.add(org);
            }
        }

        globalReport.setNumberOfOrgs(orgs.size());
        globalReport.setFacultyStrength(facultyStrength);
        globalReport.setStudentStrength(studentStrength);
        globalReport.setNumberOfSchool(schoolCount);

        globalReport.setGreenZone(greenZone);
        globalReport.setYellowZone(yellowZone);
        globalReport.setRedZone(redZone);

        return globalReport;
    }
}
