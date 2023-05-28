package org.nextlevel.user;

import org.nextlevel.org.Organisation;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping("/api/v1/users")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private OrganisationService orgService;

	@Autowired
	private SchoolService schoolService;

	public static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@RequestMapping(method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> save(@RequestBody User user) {
		return ResponseEntity.ok(userService.save(user));
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<User>> get() {
		List<User> users = userRepo.findAll();
		return ResponseEntity.ok(users);
	}

	@GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> read(@PathVariable("id") Long id) {
		User user = userService.get(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(user);
		}
	}

	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> update(@PathVariable Long id, @RequestBody User user) {
		User userOld = userService.get(id);
		if (userOld == null) {
			return ResponseEntity.notFound().build();
		}

		if (user.getUsername() != null) {
			userOld.setUsername(user.getUsername());
		}

		if (user.getPhoneNo() >= 0) {
			userOld.setPhoneNo(user.getPhoneNo());
		}

		if (user.getLanguages() != null) {
			userOld.setLanguages(user.getLanguages());
		}

		if (user.getEnabled() != null) {
			userOld.setEnabled(user.getEnabled());
		}

		if (user.getRoles() != null) {
			userOld.setRoles(user.getRoles());
		}

		if (user.getFirstName() != null && user.getFirstName().trim().length() > 0) {
			userOld.setFirstName(user.getFirstName().trim());
		}

		if (user.getLastName() != null && user.getLastName().trim().length() > 0) {
			userOld.setLastName(user.getLastName().trim());
		}

		if(user.getLicenseAgreementAccepted() != null) {
			userOld.setLicenseAgreementAccepted(user.getLicenseAgreementAccepted());
		}

		return ResponseEntity.ok(userRepo.save(userOld));
	}

	@PutMapping(value = "/{id}/changePass", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> updatePassword(@PathVariable Long id, @RequestBody User user) {
		Long loggedInUserId = userService.getLoggedInUserId();

		if(loggedInUserId.longValue() != id.longValue()) {
			if (!userService.isLoggedInUserSuperAdmin()) {
				return ResponseEntity.badRequest().build();
			}
		}

		User userOld = userService.get(id);
		if (userOld == null) {
			return ResponseEntity.notFound().build();
		}

		if (user.getPassword() != null) {
			userOld.setPassword(user.getPassword());
		}

		return ResponseEntity.ok(userService.save(userOld));
	}

	//	@DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
		userRepo.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@GetMapping(value = "/{id}/roles", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Collection<Role>> getRoles(@PathVariable Long id) {
		User user = userService.get(id);
		return ResponseEntity.ok(user.getRoles());
	}

	@GetMapping(value = "/{id}/org", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Organisation> getUserOrg(@PathVariable("id") Long userId) {
		Organisation org = userService.getUserOrg(userId);
		if (org == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(org);
	}

	@GetMapping(value = "/{id}/school", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<School> getUserSchool(@PathVariable("id") Long userId) {
		School school = userService.getUserSchool(userId);
		if (school == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(school);

//		User user = userService.get(userId);
//		if (user == null) {
//			LOG.error("Some error has occurred. Invalid user id: {}.", userId);
//			return ResponseEntity.notFound().build();
//		} else {
//			String emailId = user.getEmail();
//			Collection<Role> userRoles = user.getRoles();
//			for (Role role: userRoles) {
//				if (role.getName().equals(RoleType.SCHOOL_ADMIN.getRoleName()) ||
//						role.getName().equals(RoleType.ORG_ADMIN.getRoleName()) ||
//						role.getName().equals(RoleType.NEXTLEVEL_SUPER_ADMIN.getRoleName())) {
//
//					School school = schoolService.getBySchoolAdminEmail(emailId);
//					if (school != null) {
//						Organisation org = getUserOrg(userId).;
//
//
//						LOG.info("User {} is found as admin for school: {}.", emailId, school.getName());
//						return ResponseEntity.ok(school);
//					}
//				}
//			}
//			LOG.error("Some error has occurred. No school found where this user is school admin: {}.", emailId);
//			return ResponseEntity.notFound().build();
//		}
	}
}
