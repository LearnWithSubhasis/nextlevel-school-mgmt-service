package org.nextlevel.grade;

import org.nextlevel.attendance.AttendanceRepository;
import org.nextlevel.common.CommonController;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolRepository;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.Section;
import org.nextlevel.section.SectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class GradeController {
	@Autowired
	private GradeService service;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private OrganisationService orgService;
	@Autowired
	private SectionService sectionService;
	@Autowired
	private CommonController cc;
	@Autowired
	private AttendanceRepository attendanceRepo;
	@Autowired
	private GradeRepository gradeRepo;
	@Autowired
	private SchoolRepository schoolRepo;
	public static final Logger LOG = LoggerFactory.getLogger(GradeController.class);

	@GetMapping("/grade/list")
	public String viewHomePage(Model model, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId) {
		List<Grade> listGrades = service.listAll(orgId, schoolId);
		model.addAttribute("listGrades", listGrades);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);

		cc.setModelAttributes(model, orgId, schoolId, -1L, -1L, -1L);

		return "grades";
	}
	
	@GetMapping("/grade/new")
	public String showNewGradeForm(Model model, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId) {
		Grade grade = new Grade();
		grade.setSchool(schoolService.get(Long.parseLong(schoolId)));
		model.addAttribute("grade", grade);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);

		return "new_grade";
	}
	
	@PostMapping(value = "/grade/save")
	public String saveGrade(@ModelAttribute("grade") Grade grade, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId) {
		grade.setSchool(schoolService.get(Long.parseLong(schoolId)));
		service.save(grade);

		return "redirect:/grade/list?orgId=" + orgId + "&schoolId=" + schoolId;
	}
	
	@GetMapping("/grade/edit/{id}")
	public ModelAndView showEditGradeForm(@PathVariable(name = "id") Long id
			, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId) {
		ModelAndView mav = new ModelAndView("edit_grade");
		
		Grade grade = service.get(id);
		mav.addObject("grade", grade);
		mav.addObject("orgId", String.valueOf(orgId));
		mav.addObject("schoolId", String.valueOf(schoolId));

		return mav;
	}	
	
	@GetMapping("/grade/delete/{id}")
	public String deleteGrade(@PathVariable(name = "id") Long id, @RequestParam (name = "orgId") String orgId
			, @RequestParam (name = "schoolId") String schoolId) {
		service.delete(id);

		return "redirect:/grade/list?orgId=" + orgId + "&schoolId=" + schoolId;
	}

	@GetMapping("/grade/listSections/{gradeId}")
	public String listSections(Model model, @PathVariable(name = "gradeId") Long gradeId
			, @RequestParam (name = "orgId") Long orgId
			, @RequestParam (name = "schoolId") Long schoolId) {
		List<Section> listSections = sectionService.listAll(orgId, schoolId, gradeId);
		model.addAttribute("listSections", listSections);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);

		cc.setModelAttributes(model, orgId, schoolId, gradeId, -1L, -1L);

		return "sections";
	}

//	@RequestMapping(value = "/api/v1/grades", method = RequestMethod.PUT)
//	public ResponseEntity<Grade> save (@RequestBody Grade grade) {
//		return ResponseEntity.ok(gradeRepo.save(grade));
//	}

	@GetMapping("/api/v1/grades")
	public ResponseEntity<List<Grade>> get () {
		return ResponseEntity.ok(gradeRepo.findAll());
	}

	@GetMapping("/api/v1/grades/{id}")
	public ResponseEntity<Grade> read(@PathVariable("id") Long id) {
		Grade grade = service.get(id);
		if (grade == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(grade);
		}
	}

//	@RequestMapping(value = "/api/v1/grades", method = RequestMethod.POST)
//	//@Transactional
//	public ResponseEntity<Grade> save (@RequestBody Grade grade) {
//		if (grade.getSchool().getOrganisation() == null) {
//			return ResponseEntity.notFound().build();
//		}
//		School school1 = schoolRepo.findById(grade.getSchool().getSchoolId()).get();
//
//		grade.setSchool(school1);
//
//		return ResponseEntity.ok(gradeRepo.save(grade));
//	}

	@PostMapping(value = "/api/v1/grade")
	public ResponseEntity<Grade> save1 (@RequestBody Grade grade) {
		if (grade.getSchool() == null) {
			LOG.error("School cannot be empty.");
			return ResponseEntity.badRequest().build();
		}

		if(grade.getName() == null || grade.getName().trim().length() == 0) {
			LOG.error("Grade name cannot be empty.");
			return ResponseEntity.badRequest().build();
		}

		School schoolTemp = schoolRepo.findById(grade.getSchool().getSchoolId()).get();
		if (schoolTemp == null) {
			LOG.error("School not found.");
			return ResponseEntity.badRequest().build();
		}

		grade.setSchool(schoolTemp);

		boolean alreadyExists = service.existsAlready(grade, grade.getSchool());
		if(alreadyExists) {
			LOG.error("Grade with same name already exists.");
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(gradeRepo.save(grade));
	}

	//TODO:: value = URI, not name = URI
	@RequestMapping(value = "/api/v1/grades", method = RequestMethod.POST,
			consumes = "application/json", produces = "application/json")
	public ResponseEntity<Grade> save (@RequestBody Grade grade) {
		boolean anyInvalidData = false;
		if (grade.getSchool() == null) {
			LOG.error("School information is missing!");
			anyInvalidData = true;
		}
		if (grade.getName() == null || grade.getName().trim().length() == 0) {
			LOG.error("Grade name is missing!");
			anyInvalidData = true;
		}

		School school1 = null;
		try{
			school1 = schoolRepo.findById(grade.getSchool().getSchoolId()).get();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			anyInvalidData = true;
		}

		if(anyInvalidData) {
			return ResponseEntity.badRequest().build();
		}

		grade.setSchool(school1);

		boolean alreadyExists = service.existsAlready(grade, grade.getSchool());
		if(alreadyExists) {
			LOG.error("Grade with same name already exists.");
			return ResponseEntity.badRequest().build();
		}

		LocalDateTime timeNow = LocalDateTime.now();
		grade.setDateCreated(timeNow);
		grade.setLastUpdated(timeNow);

		return ResponseEntity.ok(gradeRepo.save(grade));
	}

	@PutMapping("/api/v1/grades/{id}")
	public ResponseEntity<Grade> update (@PathVariable Long id, @RequestBody Grade grade) {
		Grade gradeTemp = service.get(id);
		if (gradeTemp == null) {
			return ResponseEntity.notFound().build();
		}

		if(grade.getSchool() != null) {
			gradeTemp.setSchool(grade.getSchool());
		}

		if(grade.getName() != null) {
			gradeTemp.setName(grade.getName());
		}

		LocalDateTime timeNow = LocalDateTime.now();
		gradeTemp.setLastUpdated(timeNow);

		return ResponseEntity.ok(gradeRepo.save(gradeTemp));
	}

	@DeleteMapping("/api/v1/grades/{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {

		Grade grade = gradeRepo.getOne(id);
		if(grade != null) {
			School school = grade.getSchool();
			school.getGrades().remove(grade);
			schoolRepo.save(school);

			gradeRepo.deleteById(id);
		}

		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@GetMapping("/api/v1/grades/{id}/sections")
	public ResponseEntity<List<Section>> listGrades(@PathVariable("id") Long id) {
		Grade grade = service.get(id);
		if (grade == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(grade.getSections());
		}
	}
}
