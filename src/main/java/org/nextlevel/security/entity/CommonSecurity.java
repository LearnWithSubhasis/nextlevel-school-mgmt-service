package org.nextlevel.security.entity;

import org.nextlevel.user.Role;
import org.nextlevel.user.RoleType;
import org.nextlevel.user.User;
import org.nextlevel.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collection;

@Component
public class CommonSecurity {

    @Autowired
    protected UserService userService;

    protected boolean isSuperAdmin(String userEmail) {
        User user = userService.getUserByEmail(userEmail);
        Collection<Role> userRoles = user.getRoles();
        for (Role role: userRoles) {
            if (role.getName().equals(RoleType.NEXTLEVEL_SUPER_ADMIN.getRoleName())) {
                return true;
            }
        }

        return false;
    }
}
