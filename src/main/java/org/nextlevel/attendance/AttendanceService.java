package org.nextlevel.attendance;

import org.nextlevel.bulkload.ParseAttendanceSheets;
import org.nextlevel.security.oauth.CustomOAuth2User;
import org.nextlevel.student.Student;
import org.nextlevel.student.StudentRepository;
import org.nextlevel.teacher.Teacher;
import org.nextlevel.teacher.TeacherRepository;
import org.nextlevel.user.User;
import org.nextlevel.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AttendanceService {
	@Autowired
	private AttendanceRepository repo;
	@Autowired
	private ParseAttendanceSheets pas;
	@Autowired
	private UserRepository userRepo;
	@Autowired
	private TeacherRepository teacherRepo;
	@Autowired
	private StudentRepository studentRepo;

	private static HashMap<String, String> mapValidTerms = new HashMap<>();

	AttendanceService() {
		mapValidTerms.put("term 1", "term 1");
		mapValidTerms.put("term 2", "term 2");
		mapValidTerms.put("term 3", "term 3");
		mapValidTerms.put("term 4", "term 4");
	}

	public List<Attendance> listAll() {
		return repo.findAll();
	}

	public List<Attendance> listAll(Long orgId, Long schoolId, Long gradeId, Long sectionId, Long studentId) {
		return repo.findAll()
				.stream()
//				.filter(student -> student.getSection().getSectionId().longValue() == sectionId.longValue())
//				.filter(student -> student.getSection().getGrade().getGradeId() == gradeId)
//				.filter(student -> student.getSection().getGrade().getSchool().getSchoolId() == schoolId)
//				.filter(student -> student.getSection().getGrade().getSchool().getOrganisation().getOrgId() == orgId)
				.collect(Collectors.toList());
	}

	public void save(Attendance attendance) {
		repo.save(attendance);
	}
	
	public Attendance get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }

	public void markPastAttendance(AttendanceRequest ar) throws GeneralSecurityException, IOException {
		pas.parseAndMarkHistoricalAttendance(ar);
	}

	public List<Attendance> markAttendance(AttendanceRequest ar) {
		List<Attendance> entries = ar.getEntries();
		List<Attendance> allAttendance = new ArrayList<>();
		List<Attendance> failedEntries = new ArrayList<>();
		Teacher loggedInTeacher = getLoggedInUser();

		LocalDate attendanceDate = ar.getAttendanceDate();
		if (null == attendanceDate) { attendanceDate = LocalDate.now(); }
		LocalDate attendanceCollectionDate = ar.getAttendanceCollectionDate();
		if (null == attendanceCollectionDate) { attendanceCollectionDate = LocalDate.now(); }
		String term = ar.getTerm();

		for (Attendance entry: entries) {
			Student student = null;
			try {
				student = studentRepo.findById(entry.getStudent().getStudentId()).get();

				Attendance attendance = new Attendance();
				attendance.setStudent(student);
				AttendanceStatus attendanceStatus = entry.getAttendanceStatus();
				attendance.setAttendanceStatus(attendanceStatus);
				attendance.setAttendanceDate(entry.getAttendanceDate() != null? entry.getAttendanceDate() : attendanceDate);
				attendance.setAttendanceCollectionDate(entry.getAttendanceCollectionDate() != null? entry.getAttendanceCollectionDate() : attendanceCollectionDate);
				attendance.setTerm( validateTerm(term, entry) );
				attendance.setAttendanceCollector(loggedInTeacher);
				attendance.setWeekDayNo( fetchDayOfWeek(attendanceDate, entry.getAttendanceDate()) );

				allAttendance.add(attendance);
			} catch (Exception ex) {
				failedEntries.add(entry);
			}
		}

		repo.saveAll(allAttendance);
		return failedEntries;
	}

	private int fetchDayOfWeek(LocalDate attendanceDate, LocalDate attendanceDateEntry) {
		LocalDate finalDate = attendanceDateEntry != null ? attendanceDateEntry : attendanceDate;
		return finalDate.getDayOfWeek().getValue();
	}

	private String validateTerm(String term, Attendance entry) throws Exception {
		if (term == null || term.trim().length() == 0) {
			if (entry.getTerm() == null || entry.getTerm().trim().length() == 0) {
				throw new Exception("Empty term value.");
			} else if (!mapValidTerms.containsKey(entry.getTerm().toLowerCase())) {
				throw new Exception("Empty term value.");
			} else {
				return entry.getTerm();
			}
		} else if (!mapValidTerms.containsKey(term.toLowerCase())) {
			throw new Exception("Empty term value.");
		}

		return term;
	}

	private Teacher getLoggedInUser() {
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

		Teacher loggedInTeacher = resolveLoggedInUserId(userName);

		return loggedInTeacher;
	}

	private Teacher resolveLoggedInUserId(String userEmail) {
		User user = userRepo.findByEmail(userEmail);
		Teacher teacher = null;
		if (null != user.getTeacher()) {
			teacher = teacherRepo.findById(user.getTeacher().getTeacherId()).get();
		}

		return teacher;
	}

	//Paginated
	public Page<Attendance> fetchAttendanceDataAsPageWithFilteringAndSorting(String termFilter, int page, int size, List<String> sortList, String sortOrder) {
		// create Pageable object using the page, size and sort details
		Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
		// fetch the page object by additionally passing pageable with the filters
		return repo.findByTermLike(termFilter, pageable);
	}

	public Page<Attendance> fetchAttendanceDataAsPageWithFilteringAndSorting(Long studentID, int page, int size, List<String> sortList, String sortOrder) {
		// create Pageable object using the page, size and sort details
		Pageable pageable = PageRequest.of(page, size, Sort.by(createSortOrder(sortList, sortOrder)));
		// fetch the page object by additionally passing pageable with the filters
		return repo.findAttendanceByStudentID(studentID, pageable);
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
}
