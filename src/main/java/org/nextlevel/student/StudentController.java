package org.nextlevel.student;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.nextlevel.attendance.AttendanceModelAssembler;
import org.nextlevel.common.CommonController;
import org.nextlevel.grade.GradeService;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.report.student.StudentReport;
import org.nextlevel.report.student.StudentReportService;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.Section;
import org.nextlevel.section.SectionService;
import org.nextlevel.attendance.Attendance;
import org.nextlevel.attendance.AttendanceModel;
import org.nextlevel.attendance.AttendanceService;
import org.nextlevel.tools.kafka.KafkaConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Controller
public class StudentController {
	@Autowired
	private StudentService service;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private OrganisationService orgService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private AttendanceService attendanceService;
	@Autowired
	private CommonController cc;
	@Autowired
	private StudentRepository studentRepo;
	@Autowired
	private StudentModelAssembler studentModelAssembler;
	@Autowired
	private PagedResourcesAssembler<Student> pagedStudentAssembler;
	@Autowired
	private AttendanceModelAssembler attendanceModelAssembler;
	@Autowired
	private PagedResourcesAssembler<Attendance> pagedAttendanceAssembler;
	@Autowired
	private StudentReportService studentReportService;

	private KafkaConnectionProvider kafkaProducer = KafkaConnectionProvider.getInstance();

	public static final Logger LOG = LoggerFactory.getLogger(StudentController.class);

	@GetMapping("/student/list")
	public String viewHomePage(Model model, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId
			, @RequestParam (name = "gradeId") Long gradeId
			, @RequestParam (name = "sectionId") Long sectionId) {
		List<Student> listStudents = service.listAll(orgId, schoolId, gradeId, sectionId);
		model.addAttribute("listStudents", listStudents);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("sectionId", sectionId);

		cc.setModelAttributes(model, orgId, schoolId, gradeId, sectionId, -1L);

		return "students";
	}

	@GetMapping("/student/new")
	public String showNewSectionForm(Model model, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId
			, @RequestParam (name = "sectionId") String sectionId) {
		Student student = new Student();
		student.setSection(sectionService.get(Long.parseLong(sectionId)));
		model.addAttribute("student", student);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("sectionId", sectionId);

		return "new_student";
	}

	@RequestMapping(value = "/student/save", method = RequestMethod.POST)
	public String saveStudent(@ModelAttribute("student") Student student, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId
			, @RequestParam (name = "sectionId") String sectionId) {
		student.setSection(sectionService.get(Long.parseLong(sectionId)));
		service.save(student);

		return "redirect:/student/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId + "&sectionId=" + sectionId;
	}

	@GetMapping("/student/edit/{id}")
	public ModelAndView showEditStudentForm(@PathVariable(name = "id") Long id
			, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId
			, @RequestParam (name = "sectionId") String sectionId) {
		ModelAndView mav = new ModelAndView("edit_student");

		Student student = service.get(id);
		mav.addObject("student", student);
		mav.addObject("orgId", String.valueOf(orgId));
		mav.addObject("schoolId", String.valueOf(schoolId));
		mav.addObject("gradeId", String.valueOf(gradeId));
		mav.addObject("sectionId", String.valueOf(sectionId));

		return mav;
	}

	@GetMapping("/student/delete/{id}")
	public String deleteStudent(@PathVariable(name = "id") Long id, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId
			, @RequestParam (name = "sectionId") String sectionId) {
		service.delete(id);

		return "redirect:/student/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId + "&sectionId=" + sectionId;
	}

	@GetMapping("/student/listAttendance/{studentId}")
	public String listStudents(Model model, @PathVariable(name = "studentId") Long studentId
			, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId
			, @RequestParam (name = "gradeId") Long gradeId
			, @RequestParam (name = "sectionId") Long sectionId) {
		List<Attendance> listAttendance = attendanceService.listAll(orgId, schoolId, gradeId, sectionId, studentId);
		model.addAttribute("listAttendance", listAttendance);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("sectionId", sectionId);
		model.addAttribute("studentId", studentId);

		cc.setModelAttributes(model, orgId, schoolId, gradeId, sectionId, studentId);

		return "attendance";
	}

	@PostMapping("/api/v1/students")
	public ResponseEntity<Student> save (@RequestBody Student student) throws Exception {
		if(student.getSection() != null && sectionService.get(student.getSection().getSectionId().longValue()) != null) {
			Section section = sectionService.get(student.getSection().getSectionId().longValue());
			student.setSection(section);

			LocalDateTime timeNow = LocalDateTime.now();
			student.setDateCreated(timeNow);
			student.setLastUpdated(timeNow);

			student = studentRepo.save(student);
			service.registerParent(student);

			HashMap<String, String> mapNewItem = new HashMap<>();
			mapNewItem.put("tenant_id", "tenant1");
			mapNewItem.put("object_code", "student");
			mapNewItem.put("operation_type", "CREATE");
			mapNewItem.put("object_id", String.valueOf(student.getStudentId()));

			kafkaProducer.sendRequestsToMQ(mapNewItem);

			return ResponseEntity.ok(student);
		} else {
			LOG.error("Section doesn't exist.");
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/api/v1/students")
	public ResponseEntity<List<Student>> get () {
		return ResponseEntity.ok(studentRepo.findAll());
	}

	@GetMapping("/api/v2/students")
	public ResponseEntity<PagedModel<StudentModel>> getStudentsWithPagination(
			@RequestParam(defaultValue = "") String nameFilter,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") Sort.Direction sortOrder
	) {
		Page<Student> studentPage = service.fetchStudentsDataAsPageWithFilteringAndSorting(nameFilter, page, size, sortList, sortOrder.toString());
		// Use the pagedResourcesAssembler and customerModelAssembler to convert data to PagedModel format
		return ResponseEntity.ok(pagedStudentAssembler.toModel(studentPage, studentModelAssembler));
	}

	@GetMapping("/api/v1/students/{id}")
	public ResponseEntity<Student> read(@PathVariable("id") Long id) {
		Student student = service.get(id);
		if (student == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(student);
		}
	}

	@PutMapping("/api/v1/students/{id}")
	public ResponseEntity<Student> update (@PathVariable Long id, @RequestBody Student student) {
		Student studentTemp = service.get(id);
		if (studentTemp == null) {
			return ResponseEntity.notFound().build();
		}

		boolean emailChanged = false;
		if(student.getName()!=null) {
			studentTemp.setName(student.getName());
		}

		if(student.getAge()!=null) {
			studentTemp.setAge(student.getAge());
		}

		if(student.getSex()!=null) {
			studentTemp.setSex(student.getSex());
		}

		if(student.getSection()!=null) {
			studentTemp.setSection(student.getSection());
		}

		if(student.getParentEmail() != null) {
			if(student.getParentEmail() != studentTemp.getParentEmail()) {
				emailChanged = true;
			}

			studentTemp.setParentEmail(student.getParentEmail());
		}

		LocalDateTime timeNow = LocalDateTime.now();
		student.setLastUpdated(timeNow);
		student = studentRepo.save(studentTemp);

		if(emailChanged) {
			service.registerParent(student);
		}

		return ResponseEntity.ok(student);
	}

	@DeleteMapping("/api/v1/students/{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		studentRepo.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/api/v1/students/{id}/attendance")
	public ResponseEntity<List<Attendance>> getAttendance (@PathVariable Long id) {
		Student student = service.get(id);
		if (null == student) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(student.getAttendance());
	}

	@GetMapping("/api/v2/students/{id}/attendance")
	public ResponseEntity<PagedModel<AttendanceModel>> getAttendancePaginated (
			@PathVariable Long id,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") Sort.Direction sortOrder
	) {
		Student student = service.get(id);
		if (null == student) {
			return ResponseEntity.notFound().build();
		}
		Page<Attendance> attendancePage = attendanceService.fetchAttendanceDataAsPageWithFilteringAndSorting(id, page, size, sortList, sortOrder.toString());
		// Use the pagedResourcesAssembler and customerModelAssembler to convert data to PagedModel format
		return ResponseEntity.ok(pagedAttendanceAssembler.toModel(attendancePage, attendanceModelAssembler));
	}

	@GetMapping("/api/v1/students/{id}/attendanceSummary")
	public ResponseEntity<StudentReport> getAttendanceSummary (@PathVariable Long id) {
		Student student = service.get(id);
		if (null == student) {
			return ResponseEntity.notFound().build();
		}

		StudentReport summary = studentReportService.getStudentSummaryReport(id);
		return ResponseEntity.ok(summary);
	}
}
