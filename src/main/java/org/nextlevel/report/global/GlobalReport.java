package org.nextlevel.report.global;

import org.nextlevel.org.Organisation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Component
public class GlobalReport {
    private Integer numberOfOrgs = 0;
    private Integer numberOfSchool = 0;
    private Long facultyStrength = 0L;
    private Long studentStrength = 0L;
    private String reportType;
    private List<Organisation> greenZone;
    private List<Organisation> yellowZone;
    private List<Organisation> redZone;
}
