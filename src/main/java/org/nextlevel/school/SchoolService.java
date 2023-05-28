package org.nextlevel.school;

import org.nextlevel.email.EmailDetails;
import org.nextlevel.email.EmailService;
import org.nextlevel.grade.Grade;
import org.nextlevel.org.Organisation;
import org.nextlevel.teacher.Teacher;
import org.nextlevel.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SchoolService {
	@Autowired
	private SchoolRepository repo;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	public static final Logger LOG = LoggerFactory.getLogger(SchoolService.class);

	public List<School> listAll() {
		return repo.findAll();
	}

	public List<School> listAll(Long orgId) {
		return repo.findAll()
				.stream().filter(school -> school.getOrganisation()!=null && school.getOrganisation().getOrgId() == orgId.longValue()).collect(Collectors.toList());
	}

	public void save(School school) {
		repo.save(school);
	}
	
	public School get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }

	public List<Grade> listGrades(School school) {
		List<Grade> grades = school.getGrades();
		if(null == grades || grades.size() == 0) {
			grades = repo.listGrades(school.getSchoolId());
		}

		return grades;
	}

	public List<Teacher> listTeachers(School school) {
		List<Teacher> teachers = school.getTeachers();
		if(null == teachers || teachers.size() == 0) {
			teachers = repo.listTeachers(school.getSchoolId());
		}

		return teachers;
	}

	public void registerSchoolAdmin(School school) {
		if (school.getSchoolAdminEmail() == null || school.getSchoolAdminEmail().trim().length()==0) {
			LOG.error("School admin email is missing!");
			return;
		}

		Role adminRole = roleRepository.findByName(RoleType.SCHOOL_ADMIN.getRoleName());
		HashSet<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);

		String password = "schoolAdmin" + randomGenerator();

		User user = new User();
		user.setFirstName("School");
		user.setLastName("Admin");
		user.setEmail(school.getSchoolAdminEmail());
		user.setUsername(school.getSchoolAdminEmail());
		user.setPassword(passwordEncoder.encode(password));
		user.setRoles(adminRoles);
		user.setEnabled(true);
		user.setAuthType(AuthenticationType.DATABASE);

		User userExists = userRepository.findByEmail(user.getEmail());

		if(null == userExists) {
			user = userRepository.save(user);

			if(null != user) {
//				EmailDetails emailDetails = new EmailDetails();
//				emailDetails.setRecipient(school.getSchoolAdminEmail());
//				emailDetails.setSubject("Invitation to NextLevel School Mgmt, to join as school admin!");
//				emailDetails.setMsgBody("Please open NextLevel School Mgmt app, log in using following information. Email: " + school.getSchoolAdminEmail() + ", Password: " + password);
//
//				emailService.sendSimpleMail(emailDetails);

				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("orgName", school.getOrganisation().getName());
				templateModel.put("schoolName", school.getName());
				templateModel.put("schoolAdminEmail", school.getSchoolAdminEmail());
				templateModel.put("schoolAdminPassword", password);
				templateModel.put("inlineImage", "school");

				EmailDetails emailDetails = new EmailDetails();
				emailDetails.setRecipient(school.getSchoolAdminEmail());
				emailDetails.setSubject("Invitation to NextLevel School Mgmt, to join as school admin!");
				//emailDetails.setMsgBody("Please open NextLevel School Mgmt app, log in using following information. Email: " + school.getSchoolAdminEmail() + ", Password: " + password);
				emailService.sendMessageUsingThymeleafTemplate(emailDetails, "school_admin_invite_thyme.html", templateModel);
			}
		} else {
			LOG.warn("User exists already: " + school.getSchoolAdminEmail());
		}

	}

	private String randomGenerator() {
		Random rand = new Random(System.currentTimeMillis());
		return String.valueOf(rand.nextInt(10000));
	}

	public boolean existsAlready(School school, Organisation org) {
		List<School> schools = org.getSchools();
		if(null != schools && schools.size()>0) {
			for (School schoolTemp:
					schools) {
				if(schoolTemp.getName().equalsIgnoreCase(school.getName().trim())) {
					return true;
				}
			}
		}

		return false;
	}

	public Page<School> fetchSchoolDataAsPageWithFilteringAndSorting(String nameFilter, int page, int size,
																		List<String> sortList, String sortOrder) {
		// create Pageable object using the page, size and sort details
		Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
		// fetch the page object by additionally passing pageable with the filters
		return repo.findByNameLike(nameFilter, pageable);
	}

	private List<Sort.Order> createSortOrder(List<String> sortList, String sortDirection) {
		List<Sort.Order> sorts = new ArrayList<>();
		Sort.Direction direction;
		for (String sort : sortList) {
			if (sortDirection != null) {
				direction = Sort.Direction.fromString(sortDirection);
			} else {
				direction = Sort.Direction.DESC;
			}
			sorts.add(new Sort.Order(direction, sort));
		}
		return sorts;
	}

    public School getBySchoolAdminEmail(String schoolAdminEmail) {
		School school = repo.findBySchoolAdminEmail(schoolAdminEmail);
		return school;
    }
}
