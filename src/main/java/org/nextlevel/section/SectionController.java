package org.nextlevel.section;

import org.nextlevel.bulkload.ParseAttendanceSheets;
import org.nextlevel.common.CommonController;
import org.nextlevel.grade.Grade;
import org.nextlevel.grade.GradeRepository;
import org.nextlevel.grade.GradeService;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.SchoolService;
import org.nextlevel.student.Student;
import org.nextlevel.student.StudentService;
import org.nextlevel.timetable.Timetable;
import org.nextlevel.timetable.TimetableService;
import org.nextlevel.timetableentry.TimetableEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SectionController {
	@Autowired
	private SectionService service;
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
	private ParseAttendanceSheets pas;
	@Autowired
	private SectionRepository sectionRepo;
	@Autowired
	private GradeRepository gradeRepo;
	@Autowired
	private TimetableService timetableService;

	public static final Logger LOG = LoggerFactory.getLogger(SectionController.class);

	@GetMapping("/section/list")
	public String viewHomePage(Model model, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId
			, @RequestParam (name = "gradeId") Long gradeId) {
		List<Section> listSections = service.listAll(orgId, schoolId, gradeId);
		model.addAttribute("listSections", listSections);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);

		if (orgId > 0) {
			model.addAttribute("organisation", orgService.get(orgId).getName());
		}
		if (schoolId > 0) {
			model.addAttribute("school", schoolService.get(schoolId).getName());
		}
		if (gradeId > 0) {
			model.addAttribute("grade", gradeService.get(gradeId).getName());
		}

		cc.setModelAttributes(model, orgId, schoolId, gradeId, -1L, -1L);

		return "sections";
	}
	
	@GetMapping("/section/new")
	public String showNewSectionForm(Model model, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId) {
		Section section = new Section();
		section.setGrade(gradeService.get(Long.parseLong(gradeId)));
		model.addAttribute("section", section);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);

		return "new_section";
	}
	
	@RequestMapping(value = "/section/save", method = RequestMethod.POST)
	public String saveSection(@ModelAttribute("section") Section section, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId) {
		section.setGrade(gradeService.get(Long.parseLong(gradeId)));
		service.save(section);

		return "redirect:/section/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId;
	}
	
	@GetMapping("/section/edit/{id}")
	public ModelAndView showEditSectionForm(@PathVariable(name = "id") Long id
			, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId) {
		ModelAndView mav = new ModelAndView("edit_section");
		
		Section section = service.get(id);
		mav.addObject("section", section);
		mav.addObject("orgId", String.valueOf(orgId));
		mav.addObject("schoolId", String.valueOf(schoolId));
		mav.addObject("gradeId", String.valueOf(gradeId));

		return mav;
	}	
	
	@GetMapping("/section/delete/{id}")
	public String deleteSection(@PathVariable(name = "id") Long id, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId
			, @RequestParam (name = "gradeId") String gradeId) {
		service.delete(id);

		return "redirect:/section/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId;
	}

	@GetMapping("/section/listStudents/{sectionId}")
	public String listStudents(Model model, @PathVariable(name = "sectionId") Long sectionId
			, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId
			, @RequestParam (name = "gradeId") Long gradeId) {
		List<Student> listStudents = studentService.listAll(orgId, schoolId, gradeId, sectionId);
		model.addAttribute("listStudents", listStudents);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("sectionId", sectionId);

		cc.setModelAttributes(model, orgId, schoolId, gradeId, sectionId, -1L);

		return "students";
	}

	@GetMapping("/section/markAttendance/{sectionId}")
	public String markAttendance(Model model, @PathVariable(name = "sectionId") Long sectionId
			, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId
			, @RequestParam (name = "gradeId") Long gradeId) {

//		try {
//			List<List<Object>> attendanceRows = pas.parseSheet();
//			pas.createAttendanceRecords(orgId, schoolId, gradeId, sectionId, attendanceRows);
//		} catch (GeneralSecurityException e) {
//			throw new RuntimeException(e);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}

		//return "redirect:/attendance/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId + "&sectionId=" + sectionId + "&studentId=-1";
		return "redirect:/section/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId;
	}

	@PostMapping(value = "/api/v1/sections", consumes = "application/json", produces = "application/json")
	public ResponseEntity<Section> save (@RequestBody Section section) {
		if (section.getGrade() == null) {
			LOG.error("Grade cannot be empty.");
			return ResponseEntity.badRequest().build();
		}

		if(section.getName() == null || section.getName().trim().length() == 0) {
			LOG.error("Section name cannot be empty.");
			return ResponseEntity.badRequest().build();
		}

		Grade grade = gradeRepo.findById(section.getGrade().getGradeId()).get();
		if (grade == null) {
			LOG.error("Grade not found.");
			return ResponseEntity.badRequest().build();
		}

		boolean alreadyExists = service.existsAlready(section, grade);
		if(alreadyExists) {
			LOG.error("Section with same name already exists.");
			return ResponseEntity.badRequest().build();
		}

		section.setGrade(grade);

		return ResponseEntity.ok(sectionRepo.save(section));
	}

	@GetMapping("/api/v1/sections")
	public ResponseEntity<List<Section>> get () {
		return ResponseEntity.ok(sectionRepo.findAll());
	}

	@GetMapping("/api/v1/sections/{id}")
	public ResponseEntity<Section> read(@PathVariable("id") Long id) {
		Section section = service.get(id);
		if (section == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(section);
		}
	}

	@PutMapping("/api/v1/sections/{id}")
	public ResponseEntity<Section> update (@PathVariable Long id, @RequestBody Section section) {
		Section sectionTemp = service.get(id);
		if (sectionTemp == null) {
			return ResponseEntity.notFound().build();
		}

		if(section.getGrade() != null) {
			sectionTemp.setGrade(section.getGrade());
		}
		boolean alreadyExists = service.existsAlready(section, sectionTemp.getGrade());

		if(section.getName()!=null) {
			sectionTemp.setName(section.getName());
		}

		if(alreadyExists) {
			LOG.error("Section with same name already exists.");
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(sectionRepo.save(sectionTemp));
	}

	@DeleteMapping("/api/v1/sections/{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		//sectionRepo.deleteById(id);

		Section section = sectionRepo.getOne(id);
		if(section != null) {
			Grade grade = section.getGrade();
			grade.getSections().remove(section);
			gradeRepo.save(grade);

			sectionRepo.deleteById(id);
		}

		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@PostMapping("/api/v1/sections/{id}/markPastAttendance")
	public ResponseEntity<Section> markPastAttendance (
			  @RequestBody Section section
			, @PathVariable(name = "sectionId") Long sectionId
			, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId
			, @RequestParam (name = "gradeId") Long gradeId
			, String term
			) {
//		try {
//			List<List<Object>> attendanceRows = pas.parseSheet();
//			pas.createAttendanceRecords(orgId, schoolId, gradeId, sectionId, attendanceRows);
//		} catch (GeneralSecurityException e) {
//			throw new RuntimeException(e);
//		} catch (IOException e) {
//			throw new RuntimeException(e);
//		}

		return ResponseEntity.ok(sectionRepo.save(section));
	}

	@GetMapping("/api/v1/sections/{id}/students")
	public ResponseEntity<List<Student>> listGrades(@PathVariable("id") Long id) {
		Section section = service.get(id);
		if (section == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(section.getStudents());
		}
	}

	@GetMapping("/api/v1/sections/{id}/timetable")
	public ResponseEntity<List<TimetableEntry>> getTimetable(@PathVariable("id") Long id) {
		Section section = service.get(id);
		Timetable tt = null;
		List<TimetableEntry> entries = new ArrayList<>();
		if (section == null) {
			return ResponseEntity.notFound().build();
		} else {
			tt = timetableService.getForSection(section.getSectionId());
		}

		if (null != tt) {
			return ResponseEntity.ok(tt.getEntries());
		}

		return ResponseEntity.ok(entries);
	}
}
