package org.nextlevel.student;

import org.nextlevel.attendance.AttendanceRequest;
import org.nextlevel.email.EmailDetails;
import org.nextlevel.email.EmailService;
import org.nextlevel.school.SchoolService;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {
	@Autowired
	private StudentRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private EmailService emailService;

	public static final Logger LOG = LoggerFactory.getLogger(SchoolService.class);

	public List<Student> listAll() {
		return repo.findAll();
	}

	public List<Student> listAll(Long orgId, Long schoolId, Long gradeId, Long sectionId) {
		return repo.findAll()
				.stream()
				.filter(student -> student.getSection().getSectionId().longValue() == sectionId.longValue())
//				.filter(student -> student.getSection().getGrade().getGradeId() == gradeId)
//				.filter(student -> student.getSection().getGrade().getSchool().getSchoolId() == schoolId)
//				.filter(student -> student.getSection().getGrade().getSchool().getOrganisation().getOrgId() == orgId)
				.collect(Collectors.toList());
	}

	public void save(Student student) {
		repo.save(student);
	}
	
	public Student get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }

	@Deprecated
	public Student getByName(String name, int rollNo) {
		Student studentSelected = null;
		List<Student> studentList = new ArrayList<>();
		Supplier<Stream<Student>> studentsStreamSupp = () -> repo.findAll()
				.stream()
				.filter(student -> student.getName().equalsIgnoreCase(name));

		Stream<Student> studentsStream = studentsStreamSupp.get();
		if (studentsStream.count() > 1) {
			studentList = studentsStreamSupp.get().filter(student -> student.getRoleNo() == rollNo)
					.collect(Collectors.toList());
		} else {
			studentList = studentsStreamSupp.get().collect(Collectors.toList());
		}

		if (null != studentList && studentList.size() == 1) {
			studentSelected = studentList.get(0);
		}

		return studentSelected;
	}

	public Student getByName(String name, int rollNo, AttendanceRequest ar) {
		Student studentSelected = null;
		List<Student> studentList = new ArrayList<>();
		Supplier<Stream<Student>> studentsStreamSupp = () -> repo.findAll()
				.stream()
				.filter(student -> student.getName().equalsIgnoreCase(name) &&
						student.getSection().getSectionId().longValue() == ar.getSectionId().longValue());

		Stream<Student> studentsStream = studentsStreamSupp.get();
		if (studentsStream.count() > 1) {
			studentList = studentsStreamSupp.get().filter(student -> student.getRoleNo() == rollNo)
					.collect(Collectors.toList());
		} else {
			studentList = studentsStreamSupp.get().collect(Collectors.toList());
		}

		if (null != studentList && studentList.size() == 1) {
			studentSelected = studentList.get(0);
		}

		return studentSelected;
	}

    public Page<Student> fetchStudentsDataAsPageWithFilteringAndSorting(String nameFilter, int page, int size,
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

	public void registerParent(Student student) {
		if (student.getParentEmail() == null || student.getParentEmail().trim().length()==0) {
			LOG.error("Parent's email is missing!");
			return;
		}

		Role adminRole = roleRepository.findByName(RoleType.PARENT_USER.getRoleName());
		HashSet<Role> adminRoles = new HashSet<>();
		adminRoles.add(adminRole);

		String password = "parent" + randomGenerator();

		User user = new User();
		user.setFirstName("Parent");
		user.setLastName("Parent");
		user.setEmail(student.getParentEmail());
		user.setUsername(student.getParentEmail());
		user.setPassword(passwordEncoder.encode(password));
		user.setRoles(adminRoles);
		user.setEnabled(true);
		user.setAuthType(AuthenticationType.DATABASE);

		User userExists = userRepository.findByEmail(user.getEmail());

		if(null == userExists) {
			user = userRepository.save(user);

			if(null != user) {
				EmailDetails emailDetails = new EmailDetails();
				emailDetails.setRecipient(student.getParentEmail());
				emailDetails.setSubject("Invitation to NextLevel School Management, to join as parent!");
				emailDetails.setMsgBody("Please open NextLevel School Management app, log in using following information. Email: " + student.getParentEmail() + ", Password: " + password);

				//emailService.sendSimpleMail(emailDetails);
			}
		} else {
			LOG.warn("User exists already: " + student.getParentEmail());
		}

	}

	private String randomGenerator() {
		Random rand = new Random(System.currentTimeMillis());
		return String.valueOf(rand.nextInt(10000));
	}
}
