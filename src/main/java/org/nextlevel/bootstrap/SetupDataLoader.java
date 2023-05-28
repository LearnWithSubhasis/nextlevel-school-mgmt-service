package org.nextlevel.bootstrap;

import org.nextlevel.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;
        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> userPrivileges = Arrays.asList(
                readPrivilege);
        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        //createRoleIfNotFound("TEST_ROLE_ADMIN", adminPrivileges);
        //createRoleIfNotFound("TEST_ROLE_USER", Arrays.asList(readPrivilege));

        //createRoleIfNotFound(RoleType.NEXTLEVEL_USER, userPrivileges);
        //createRoleIfNotFound(RoleType.NEXTLEVEL_SUPER_ADMIN, adminPrivileges);

//        Arrays.asList(RoleType.values())
//                .forEach(role -> System.out.println(role.name()));

        RoleType.stream()
                .filter(roleType -> roleType.name().endsWith("_USER"))
                .forEach(roleType ->
                        createRoleIfNotFound(roleType, userPrivileges)
                );

        RoleType.stream()
                .filter(role -> role.name().endsWith("_ADMIN"))
                .forEach(role ->
                        createRoleIfNotFound(role, adminPrivileges)
                );

        Role adminRole = roleRepository.findByName(RoleType.NEXTLEVEL_SUPER_ADMIN.getRoleName());
        HashSet<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);

        User user = new User();
        user.setFirstName("NextLevel");
        user.setLastName("Admin");
        user.setEmail("nextlevel@admin.com");
        user.setUsername("nextlevel@admin.com");
        user.setPassword(passwordEncoder.encode("skz"));
        user.setRoles(adminRoles);
        user.setEnabled(true);

        User userExists = userRepository.findByEmail(user.getEmail());

        if(null == userExists)
            userRepository.save(user);

        //Another User
        user = new User();
        user.setFirstName("Test");
        user.setLastName("Admin");
        user.setEmail("test2@test.com");
        user.setUsername("test2@test.com");
        user.setPassword(passwordEncoder.encode("test"));
        user.setRoles(adminRoles);
        user.setEnabled(true);

        userExists = userRepository.findByEmail(user.getEmail());

        if(null == userExists)
            userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            RoleType roleType, Collection<Privilege> privileges) {

        Role role = roleRepository.findByName(roleType.getRoleName());
        if (role == null) {
            role = new Role();
            role.setName(roleType.name());
            role.setTitle(roleType.getRoleTitle());
            role.setActive(true);
            role.setPrivileges(privileges);
            roleRepository.save(role);
        }
        return role;
    }
}
