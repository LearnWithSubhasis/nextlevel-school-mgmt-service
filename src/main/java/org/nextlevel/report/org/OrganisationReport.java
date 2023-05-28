package org.nextlevel.report.org;

import org.nextlevel.school.School;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrganisationReport {
    private Long orgId;
    private Integer numberOfSchool;
    private Long facultyStrength;
    private Long studentStrength;
    private String reportType;
    private List<School> greenZone;
    private List<School> yellowZone;
    private List<School> redZone;
}
