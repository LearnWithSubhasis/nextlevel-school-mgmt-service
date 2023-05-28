package org.nextlevel.security.entity;

import org.nextlevel.org.Organisation;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component("schoolSecurity")
public class SchoolSecurity extends CommonSecurity {

    @Autowired
    OrganisationService orgService;
    @Autowired
    SchoolService schoolService;

    public static final Logger LOG = LoggerFactory.getLogger(SchoolSecurity.class);

    public boolean hasUserId(Authentication authentication, Long schoolId) {
//        String userEmail = authentication.getName();
//        School schoolExisting = schoolService.get(schoolId);
//        List<School> schools = schoolService.listAll().stream()
//                .filter(school -> school.getSchoolId().longValue() == schoolId.longValue())
//                .collect(Collectors.toList());
//        if(schools.size() > 0) {
//            return true;
//        }
//
//        return false;
        return true;
    }

    public boolean hasUserIdSpecificSchool(Authentication authentication, Long schoolId) {
        String userEmail = authentication.getName();
        School school = schoolService.get(schoolId);
        if (school != null && school.getSchoolAdminEmail() != null && school.getSchoolAdminEmail().equalsIgnoreCase(userEmail)) {
            LOG.info("Current user {} is school admin, allowed access to school: {}.", userEmail, school.getName());
            return true;
        }

        if(isSuperAdmin(userEmail)) {
            LOG.info("Current user {} is super admin, allowed access to school: {}.", userEmail, school.getName());
            return true;
        }

        Organisation org = school.getOrganisation();
        if (org != null && org.getOrgAdminEmail() != null && org.getOrgAdminEmail().equalsIgnoreCase(userEmail)) {
            LOG.info("Current user {} is org admin, allowed access to school: {}.", userEmail, school.getName());
            return true;
        }

        return false;
    }
}
