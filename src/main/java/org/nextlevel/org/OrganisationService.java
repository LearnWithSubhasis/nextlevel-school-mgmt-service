package org.nextlevel.org;

import java.util.*;

import org.nextlevel.email.EmailDetails;
import org.nextlevel.email.EmailService;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolRepository;
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

@Service
public class OrganisationService {
	@Autowired
	private OrganisationRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private SchoolRepository schoolRepo;

	public static final Logger LOG = LoggerFactory.getLogger(OrganisationService.class);

	public List<Organisation> listAll() {
		return repo.findAll();
	}
	
	public void save(Organisation organisation) {
		repo.save(organisation);
	}
	
	public Organisation get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }

    public void registerOrgAdmin(Organisation org) {
		if (org.getOrgAdminEmail() == null || org.getOrgAdminEmail().trim().length()==0) {
			LOG.error("Org admin email is missing!");
			return;
		}

		Role adminRole = roleRepository.findByName(RoleType.ORG_ADMIN.getRoleName());
		HashSet<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);

		String password = "orgAdmin" + randomGenerator();
		LOG.info(password);

		User user = new User();
		user.setFirstName("Org");
		user.setLastName("Admin");
		user.setEmail(org.getOrgAdminEmail());
		user.setUsername(org.getOrgAdminEmail());
		user.setPassword(passwordEncoder.encode(password));
		user.setRoles(adminRoles);
		user.setEnabled(true);
		user.setAuthType(AuthenticationType.DATABASE);

		User userExists = userRepository.findByEmail(user.getEmail());

		if(null == userExists) {
			user = userRepository.save(user);

			if(null != user) {
//				EmailDetails emailDetails = new EmailDetails();
//				emailDetails.setRecipient(org.getOrgAdminEmail());
//				emailDetails.setSubject("Invitation to NextLevel School Mgmt, to join as organisation admin!");
//				emailDetails.setMsgBody("Please open NextLevel School Mgmt app, log in using following information. Email: " + org.getOrgAdminEmail() + ", Password: " + password);
//
//				emailService.sendSimpleMail(emailDetails);

				Map<String, Object> templateModel = new HashMap<>();
				templateModel.put("orgName", org.getName());
				templateModel.put("orgAdminEmail", org.getOrgAdminEmail());
				templateModel.put("orgAdminPassword", password);
				templateModel.put("inlineImage", "organisation");

				EmailDetails emailDetails = new EmailDetails();
				emailDetails.setRecipient(org.getOrgAdminEmail());
				emailDetails.setSubject("Invitation to NextLevel School Mgmt, to join as organisation admin!");
				//emailDetails.setMsgBody("Please open NextLevel School Mgmt app, log in using following information. Email: " + org.getOrgAdminEmail() + ", Password: " + password);
				emailService.sendMessageUsingThymeleafTemplate(emailDetails, "org_admin_invite_thyme.html", templateModel);
			}
		} else {
			LOG.warn("User exists already: " + org.getOrgAdminEmail());
		}
    }

	private String randomGenerator() {
		Random rand = new Random(System.currentTimeMillis());
		return String.valueOf(rand.nextInt(10000));
	}

    public Page<Organisation> fetchOrgDataAsPageWithFilteringAndSorting(String nameFilter, int page, int size,
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

	public Page<School> fetchSchoolsDataAsPageWithFilteringAndSorting(Long orgId, String nameFilter, int page, int size, List<String> sortList, String sortOrder) {
		// create Pageable object using the page, size and sort details
		Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
		// fetch the page object by additionally passing pageable with the filters
		return schoolRepo.findByNameLikeForOrgId(orgId, nameFilter, pageable);
	}

	public Organisation getByOrgAdminEmail(String orgAdminEmail) {
		Organisation org = repo.findByOrgAdminEmail(orgAdminEmail);
		return org;
	}

//	public List<School> listSchools(Long orgId) {
//		Optional<Organisation> org = repo.findById(orgId);
//		return org.get().getSchools();
//	}
}
