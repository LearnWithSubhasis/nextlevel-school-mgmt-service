package org.nextlevel.user;

import javax.transaction.Transactional;

import org.nextlevel.email.EmailDetails;
import org.nextlevel.email.EmailService;
import org.nextlevel.org.Organisation;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolService;
import org.nextlevel.security.oauth.CustomOAuth2User;
import org.nextlevel.teacher.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.Random;

@Service
@Transactional
public class UserService {
    @Autowired
    private UserRepository repo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private OrganisationService orgService;
    @Autowired
    private SchoolService schoolService;

    public static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public void updateAuthenticationType(String username, String oauth2ClientName) {
    	AuthenticationType authType = AuthenticationType.valueOf(oauth2ClientName.toUpperCase());
    	repo.updateAuthenticationType(username, authType);
    	System.out.println("Updated user's authentication type to " + authType);
    }

    public void updateUserDetails(User loggedInUser) throws Exception {
        User userObj = getUserByEmail(loggedInUser.getUsername());
        if (null == userObj) {
            if (loggedInUser.getPhoneNo() > 0) {
                userObj = getUserByPhoneNo(loggedInUser.getPhoneNo());
                if (userObj != null) {
                    throw new Exception("Another user with same phone number exists already!");
                }
            }

            repo.save(loggedInUser);
            System.out.println("Created new user " + loggedInUser.getUsername());

            //TODO::Registration Pending

        }

        AuthenticationType authType = AuthenticationType.valueOf(loggedInUser.getOpauth2ClientName().toUpperCase());
        repo.updateUserLogin(loggedInUser.getUsername(), authType, loggedInUser.getProfileImageURL());
        System.out.println("Updated user's authentication type to " + authType);
    }

    public User getUserByEmail(String email) {
        return repo.getUserByUsername(email);
    }

    public User getUserByPhoneNo(Long phoneNo) {
        return repo.getUserByPhoneNo(phoneNo);
    }

    public User get(Long id) {
        return repo.findById(id).get();
    }

    public User save(User user) {
        if(user.getPassword() != null && user.getPassword().trim().length()>0) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        return repo.save(user);
    }

    public Long getLoggedInUserId() {
        User user = this.getLoggedInUser();
        if(null != user) {
            return user.getUserId();
        }

        return -1L;
    }

    private User getLoggedInUser() {
        SecurityContext secureContext = SecurityContextHolder.getContext();
        Authentication auth = secureContext.getAuthentication();
        Object principal = auth.getPrincipal();

        String userName = null;
        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            userName = userDetails.getUsername();
        } else if (principal instanceof CustomOAuth2User) {
            CustomOAuth2User userDetails = (CustomOAuth2User) principal;
            userName = userDetails.getEmail();
        } else {
            userName = principal.toString();
        }

        return this.getUserByEmail(userName);
    }

    public boolean isLoggedInUserSuperAdmin() {
        User user = this.getLoggedInUser();
        Collection<Role> userRoles = user.getRoles();
        for (Role role: userRoles) {
            if (role.getName().equals(RoleType.NEXTLEVEL_SUPER_ADMIN.getRoleName())) {
                return true;
            }
        }

        return false;
    }

    public User getOrCreateUser(Teacher teacher) {
        User userExists = repo.findByEmail(teacher.getTeacherEmail());
        if (userExists == null) {
            String password = "teacher" + randomGenerator();
            Role adminRole = roleRepository.findByName(RoleType.SCHOOL_TEACHER_ADMIN.getRoleName());
            HashSet<Role> adminRoles = new HashSet<>();
            adminRoles.add(adminRole);

            User user = new User();
            user.setFirstName(teacher.getName() == null || teacher.getName().trim().length() == 0 ?
                    teacher.getName() : "Teacher");
            user.setLastName("Teacher");
            user.setEmail(teacher.getTeacherEmail());
            user.setUsername(teacher.getTeacherEmail());
            user.setPassword(passwordEncoder.encode(password));
            user.setRoles(adminRoles);
            user.setEnabled(true);
            user.setAuthType(AuthenticationType.DATABASE);

            userExists = repo.save(user);

            if(null != userExists) {
                EmailDetails emailDetails = new EmailDetails();
                emailDetails.setRecipient(teacher.getTeacherEmail());
                emailDetails.setSubject("Invitation to NextLevel School Mgmt, to join as teacher!");
                emailDetails.setMsgBody("Please open NextLevel School Mgmt app, log in using following information. Email: " + teacher.getTeacherEmail() + ", Password: " + password);

                emailService.sendSimpleMail(emailDetails);
            }
        } else {
            LOG.error("User exists alreday: {}", teacher.getTeacherEmail());
        }

        return userExists;
    }

    private String randomGenerator() {
        Random rand = new Random(System.currentTimeMillis());
        return String.valueOf(rand.nextInt(10000));
    }

    protected Organisation getUserOrg(Long userId) {
        User user = get(userId);
        if (user == null) {
            String message = String.format("Some error has occurred. Invalid user id: %o.", userId);
            LOG.error(message);
            return null;
        } else {
            String emailId = user.getEmail();
            Organisation org = orgService.getByOrgAdminEmail(emailId);
            if (org != null) {
                Collection<Role> userRoles = user.getRoles();
                for (Role role : userRoles) {
                    if (role.getName().equals(RoleType.ORG_ADMIN.getRoleName()) || role.getName().equals(RoleType.NEXTLEVEL_SUPER_ADMIN.getRoleName())) {
                        String message = String.format("User %s is found as admin for org: %s.", emailId, org.getName());
                        LOG.info(message);
                        return org;
                    }
                }
            }

            String message = String.format("Some error has occurred. No org found where this user is org admin: %s.", emailId);
            LOG.error(message);
            return null;
        }
    }

    public School getUserSchool(Long userId) {
        User user = get(userId);
        if (user == null) {
            String message = String.format("Some error has occurred. Invalid user id: %o.", userId);
            LOG.error(message);
            return null;
        } else {
            String emailId = user.getEmail();
            School school = schoolService.getBySchoolAdminEmail(emailId);
            if (school != null) {
                Collection<Role> userRoles = user.getRoles();
                for (Role role: userRoles) {
				    if (role.getName().equals(RoleType.SCHOOL_ADMIN.getRoleName())) {
                        String message = String.format("User %s is found as admin for school: %s.", emailId, school.getName());
                        LOG.info(message);
                        return school;
					} else if ( role.getName().equals(RoleType.ORG_ADMIN.getRoleName())) {
                        Organisation org = getUserOrg(userId);
                        if (school.getOrganisation().getOrgId().longValue() == org.getOrgId().longValue()) {
                            LOG.info("User %s is org admin, indirectly found as admin for school: %s.", emailId, school.getName());
                            return school;
                        }
                    } else if (role.getName().equals(RoleType.NEXTLEVEL_SUPER_ADMIN.getRoleName())) {
                        String message = String.format("User %s is NextLevel admin, indirectly found as admin for school: %s.", emailId, school.getName());
                        LOG.info(message);
                        return school;
                    }
				}
            }
            String message = String.format("Some error has occurred. No school found where this user is school admin: %s.", emailId);
            LOG.error(message);
            return null;
        }
    }
}
