package org.nextlevel.teacher;

import org.nextlevel.grade.Grade;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.Section;
import org.nextlevel.timetableentry.TimetableEntry;
import org.nextlevel.timetableentry.TimetableEntryService;
import org.nextlevel.user.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/teachers")
public class TeacherController {
	@Autowired
	private TeacherService service;
	@Autowired
	private TeacherRepository teacherRepo;
	@Autowired
	private TimetableEntryService ttService;
	@Autowired
	private UserService userService;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private SchoolService schoolService;

	@Autowired
	private UserRepository userRepo;

	public static final Logger LOG = LoggerFactory.getLogger(TeacherController.class);

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@Transactional
	public ResponseEntity<Teacher> save (@RequestBody Teacher teacher) {
		User user = userRepo.findById(teacher.getUser().getUserId()).get();
		teacher.setUser(user);

		School school = schoolService.get(teacher.getSchool().getSchoolId());
		teacher.setSchool(school);

		LocalDateTime timeNow = LocalDateTime.now();
		teacher.setDateCreated(timeNow);
		teacher.setLastUpdated(timeNow);

		try {
			teacher = teacherRepo.save(teacher);

			Role schoolTeacherRole = roleRepository.findByName(RoleType.SCHOOL_TEACHER_ADMIN.getRoleName());
			HashSet<Role> adminRoles = new HashSet<>();
			adminRoles.add(schoolTeacherRole);
			user.setRoles(adminRoles);
			userService.save(user);

			return ResponseEntity.ok(teacher);
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<Teacher>> get () {
		return ResponseEntity.ok(teacherRepo.findAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<Teacher> read(@PathVariable("id") Long id) {
		Teacher teacher = service.get(id);
		if (teacher == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(teacher);
		}
	}

	@GetMapping("/byUserId/{userId}")
	public ResponseEntity<Teacher> getByUserId(@PathVariable("userId") Long userId) {
		Teacher teacher = service.getByUserId(userId);
		if (teacher == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(teacher);
		}
	}

//	@RequestMapping(name = "{id}", method = RequestMethod.PUT,
//			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping("/{id}")
	public ResponseEntity<Teacher> update (@PathVariable Long id, @RequestBody Teacher teacher) {
		Teacher teacherOld = service.get(id);
		if (teacherOld != null) {
			if(teacher.getName() != null && teacher.getName().trim().length()>0) {
				teacherOld.setName(teacher.getName());
			}

			if(teacher.getSchool() != null) {
				School school = schoolService.get(teacher.getSchool().getSchoolId());
				teacherOld.setSchool(school);
			}

			if(teacher.getSubjects() != null) {
				teacherOld.setSubjects(teacher.getSubjects());
			}

			if(teacher.getDateOfBirth() != null) {
				teacherOld.setDateOfBirth(teacher.getDateOfBirth());
			}

			if(teacher.getGender() != null) {
				teacherOld.setGender(teacher.getGender());
			}

			if (teacher.getQualification() != null) {
				teacherOld.setQualification(teacher.getQualification());
			}

			if (teacher.getYearsOfExperience() != null) {
				teacherOld.setYearsOfExperience(teacher.getYearsOfExperience());
			}

			if(teacher.getSubjects() != null) {
				teacherOld.setSubjects(teacher.getSubjects());
			}

			LocalDateTime timeNow = LocalDateTime.now();
			teacherOld.setLastUpdated(timeNow);

			return ResponseEntity.ok(teacherRepo.save(teacherOld));
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@DeleteMapping("{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		Teacher teacher = teacherRepo.getOne(id);
		if(teacher != null) {
			List<TimetableEntry> entries = new ArrayList<>();
			LocalDateTime currentTime = LocalDateTime.now();

			entries = ttService.filterTimeTableWeekly(teacher);
			for (TimetableEntry entry: entries) {
				ttService.delete(entry.getTimetableEntryId());
			}

			teacherRepo.deleteById(id);
		}

		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("{id}/timetable")
	public ResponseEntity<List<TimetableEntry>> getTimeTableWeekly (@PathVariable Long id) {
		Teacher teacher = service.get(id);
		if (null == teacher) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<TimetableEntry> entries = new ArrayList<>();
		LocalDateTime currentTime = LocalDateTime.now();

		//Long loggedInUserId = userService.getLoggedInUserId();
		entries = ttService.filterTimeTableWeekly(teacher);
		for (TimetableEntry entry: entries) {
			Section section = entry.getTimetable().getSection();
			entry.setSectionID(section.getSectionId());
			entry.setSectionName(section.getName());
			Grade grade = section.getGrade();
			entry.setGradeID(grade.getGradeId());
			entry.setGradeName(grade.getName());
		}

		return ResponseEntity.ok(entries);
	}

	@GetMapping("{id}/timetableUpcoming")
	public ResponseEntity<List<TimetableEntry>> getTimeTableToday (@PathVariable Long id) {
		Teacher teacher = service.get(id);
		if (null == teacher) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		List<TimetableEntry> entries = new ArrayList<>();
		LocalDateTime currentTime = LocalDateTime.now();

		//Long loggedInUserId = userService.getLoggedInUserId();
		entries = ttService.filterTimeTableForToday(teacher, currentTime);
		for (TimetableEntry entry: entries) {
			Section section = entry.getTimetable().getSection();
			entry.setSectionID(section.getSectionId());
			entry.setSectionName(section.getName());
			Grade grade = section.getGrade();
			entry.setGradeID(grade.getGradeId());
			entry.setGradeName(grade.getName());
		}

		return ResponseEntity.ok(entries);
	}

//	@GetMapping("{id}/students")
//	public ResponseEntity<List<Section>> getSections (@PathVariable Long id) {
//		Subject subject = service.get(id);
//		if (null == subject) {
//			return ResponseEntity.notFound().build();
//		}
//		return ResponseEntity.ok(subject.getS);
//	}
}
