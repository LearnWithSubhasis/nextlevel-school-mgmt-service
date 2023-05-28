package org.nextlevel.attendance;

import org.nextlevel.bulkload.ParseAttendanceSheets;
import org.nextlevel.common.CommonController;
import org.nextlevel.grade.GradeService;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.SectionService;
import org.nextlevel.student.StudentService;
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

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Controller
public class AttendanceController {
	@Autowired
	private AttendanceService service;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private OrganisationService orgService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private CommonController cc;
	@Autowired
	private AttendanceRepository attendanceRepo;
	@Autowired
	private ParseAttendanceSheets pas;
	@Autowired
	private AttendanceModelAssembler attendanceModelAssembler;
	@Autowired
	private PagedResourcesAssembler<Attendance> pagedAttendanceAssembler;

	@GetMapping("/attendance/list")
	public String viewHomePage(Model model, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId
			, @RequestParam (name = "gradeId") Long gradeId
			, @RequestParam (name = "sectionId") Long sectionId
			, @RequestParam (name = "studentId") Long studentId) {
		List<Attendance> listAttendance = service.listAll(orgId, schoolId, gradeId, sectionId, studentId);
		model.addAttribute("listAttendance", listAttendance);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("sectionId", sectionId);
		model.addAttribute("studentId", studentId);

		cc.setModelAttributes(model, orgId, schoolId, gradeId, sectionId, studentId);

		return "attendance";
	}

	@GetMapping("/attendance/new")
	public String showNewAttendanceForm(Model model, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId
			, @RequestParam (name = "sectionId") String sectionId
			, @RequestParam (name = "studentId") String studentId) {
		Attendance attendance = new Attendance();
		attendance.setStudent(studentService.get(Long.parseLong(studentId)));
		model.addAttribute("attendance", attendance);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("sectionId", sectionId);
		model.addAttribute("studentId", studentId);

		return "new_attendance";
	}

	@RequestMapping(value = "/attendance/save", method = RequestMethod.POST)
	public String saveAttendance(@ModelAttribute("attendance") Attendance attendance, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId
			, @RequestParam (name = "sectionId") String sectionId
			, @RequestParam (name = "studentId") String studentId) {
		attendance.setStudent(studentService.get(Long.parseLong(studentId)));
		service.save(attendance);

		return "redirect:/attendance/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId + "&sectionId=" + sectionId + "&studentId=" + studentId;
	}

	@GetMapping("/attendance/edit/{id}")
	public ModelAndView showEditAttendanceForm(@PathVariable(name = "id") Long id
			, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId
			, @RequestParam (name = "sectionId") String sectionId
			, @RequestParam (name = "studentId") String studentId) {
		ModelAndView mav = new ModelAndView("edit_attendance");

		Attendance attendance = service.get(id);
		mav.addObject("attendance", attendance);
		mav.addObject("orgId", String.valueOf(orgId));
		mav.addObject("schoolId", String.valueOf(schoolId));
		mav.addObject("gradeId", String.valueOf(gradeId));
		mav.addObject("sectionId", String.valueOf(sectionId));
		mav.addObject("studentId", String.valueOf(studentId));

		return mav;
	}

	@GetMapping("/attendance/delete/{id}")
	public String deleteAttendance(@PathVariable(name = "id") Long id, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId
			, @RequestParam (name = "sectionId") String sectionId
			, @RequestParam (name = "studentId") String studentId) {
		service.delete(id);

		return "redirect:/attendance/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId + "&sectionId=" + sectionId + "&studentId=" + studentId;
	}

	@PostMapping("/api/v1/attendance")
	public ResponseEntity<Attendance> save (@RequestBody Attendance attendance) {
		return ResponseEntity.ok(attendanceRepo.save(attendance));
	}

	@GetMapping("/api/v1/attendance")
	public ResponseEntity<List<Attendance>> get () {
		return ResponseEntity.ok(attendanceRepo.findAll());
	}

	@GetMapping("/api/v2/attendance")
	public ResponseEntity<PagedModel<AttendanceModel>> getAttendanceWithPagination(
			@RequestParam(defaultValue = "") String termFilter,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") Sort.Direction sortOrder
			) {
		Page<Attendance> attendancePage = service.fetchAttendanceDataAsPageWithFilteringAndSorting(termFilter, page, size, sortList, sortOrder.toString());
		// Use the pagedResourcesAssembler and customerModelAssembler to convert data to PagedModel format
		return ResponseEntity.ok(pagedAttendanceAssembler.toModel(attendancePage, attendanceModelAssembler));
	}

	@GetMapping("/api/v1/attendance/{id}")
	public ResponseEntity<Attendance> read(@PathVariable("id") Long id) {
		Attendance attendance = service.get(id);
		if (attendance == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(attendance);
		}
	}

	@PutMapping("/api/v1/attendance/{id}")
	public ResponseEntity<Attendance> update (@PathVariable Long id, @RequestBody Attendance attendance) {
		attendance.setAttendanceId(id);
		return ResponseEntity.ok(attendanceRepo.save(attendance));
	}

	@DeleteMapping("/api/v1/attendance/{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		attendanceRepo.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/api/v1/attendance/markPastAttendance")
	public ResponseEntity<HttpStatus> markPastAttendance (
			@RequestBody AttendanceRequest ar
	) {
		try {
			service.markPastAttendance(ar);
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		} catch (IOException e) {
			e.printStackTrace();
			//throw new RuntimeException(e);
			return new ResponseEntity<HttpStatus>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}

	@PostMapping("/api/v1/attendance/markAttendance")
	public ResponseEntity<HttpStatus> markAttendance(@RequestBody AttendanceRequest ar) {
		service.markAttendance(ar);
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}
}
