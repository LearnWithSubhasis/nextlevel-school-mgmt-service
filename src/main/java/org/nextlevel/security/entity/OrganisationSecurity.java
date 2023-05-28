package org.nextlevel.security.entity;

import org.nextlevel.org.Organisation;
import org.nextlevel.org.OrganisationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component("orgSecurity")
public class OrganisationSecurity extends CommonSecurity {
    @Autowired
    OrganisationService orgService;
    public static final Logger LOG = LoggerFactory.getLogger(OrganisationSecurity.class);

    public boolean hasUserId(Authentication authentication, Long orgId) {
        String userEmail = authentication.getName();
        if(isSuperAdmin(userEmail)) {
            return true;
        }

        List<Organisation> orgs = null;
        try {
            orgs = orgService.listAll().stream()
                    .filter(org -> org.getOrgId() != null && org.getOrgId().longValue() == orgId.longValue())
                    .filter(org -> org.getOrgAdminEmail() != null && org.getOrgAdminEmail().equalsIgnoreCase(userEmail))
                    .collect(Collectors.toList());
        } catch (Exception ex) {
            LOG.error(ex.getMessage());
        }

        if(orgs.size() > 0) { //TODO:: Ideally it should be just 1
            return true;
        }

        return false;
    }

    public boolean hasUserIdSpecificOrg(Authentication authentication, Long orgId) {
        String userEmail = authentication.getName();
        if(isSuperAdmin(userEmail)) {
            return true;
        }

        Organisation org = orgService.get(orgId.longValue());
        if (org != null && org.getOrgAdminEmail() != null && org.getOrgAdminEmail().equalsIgnoreCase(userEmail)) {
            return true;
        }

        return false;
    }
}
