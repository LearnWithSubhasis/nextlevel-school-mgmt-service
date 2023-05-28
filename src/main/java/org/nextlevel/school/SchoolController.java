package org.nextlevel.school;

import org.nextlevel.common.CommonController;
import org.nextlevel.common.DeleteResponse;
import org.nextlevel.common.DeleteStatus;
import org.nextlevel.grade.Grade;
import org.nextlevel.grade.GradeService;
import org.nextlevel.org.Organisation;
import org.nextlevel.org.OrganisationRepository;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.teacher.Teacher;
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
import java.util.List;

@Controller
public class SchoolController {
	@Autowired
	private SchoolService service;
	@Autowired
	private OrganisationService orgService;
	@Autowired
	private GradeService gradeService;
	@Autowired
	private CommonController cc;
	@Autowired
	private SchoolRepository schoolRepo;
	@Autowired
	private OrganisationRepository orgRepo;
	@Autowired
	private SchoolModelAssembler schoolModelAssembler;
	@Autowired
	private PagedResourcesAssembler<School> pagedSchoolAssembler;
	public static final Logger LOG = LoggerFactory.getLogger(SchoolController.class);

	@GetMapping("/school/list")
	public String viewHomePage(Model model, @RequestParam (name = "orgId") Long orgId) {
		List<School> listSchools = service.listAll(Long.valueOf(orgId));
		model.addAttribute("listSchools", listSchools);
		model.addAttribute("orgId", orgId);

		cc.setModelAttributes(model, orgId, -1L, -1L, -1L, -1L);

		return "schools";
	}
	
	@GetMapping("/school/new")
	public String showNewSchoolForm(Model model, @RequestParam (name = "orgId") String orgId) {
		School school = new School();
		school.setOrganisation(orgService.get(Long.parseLong(orgId)));
		model.addAttribute("school", school);
		model.addAttribute("orgId", orgId);

		return "new_school";
	}
	
	@RequestMapping(value = "/school/save", method = RequestMethod.POST)
	public String saveSchool(@ModelAttribute("school") School school, @RequestParam (name = "orgId") String orgId) {
		school.setOrganisation(orgService.get(Long.parseLong(orgId)));
		service.save(school);

		return "redirect:/school/list?orgId=" + orgId;
	}
	
	@GetMapping("/school/edit/{id}")
	public ModelAndView showEditSchoolForm(@PathVariable(name = "id") Long id
			, @RequestParam (name = "orgId") String orgId) {
		ModelAndView mav = new ModelAndView("edit_school");
		
		School school = service.get(id);
		mav.addObject("school", school);
		mav.addObject("orgId", String.valueOf(orgId));
		
		return mav;
	}	
	
	@GetMapping("/school/delete/{id}")
	public String deleteSchool(@PathVariable(name = "id") Long id, @RequestParam (name = "orgId") String orgId) {
		service.delete(id);
		
		return "redirect:/school/list?orgId=" + orgId;
	}

	@GetMapping("/school/listGrades/{schoolId}")
	public String listSchools(Model model, @PathVariable(name = "schoolId") Long schoolId
			, @RequestParam (name = "orgId") Long orgId) {
		List<Grade> listGrades = gradeService.listAll(orgId, schoolId);
		model.addAttribute("listGrades", listGrades);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);

		cc.setModelAttributes(model, orgId, schoolId, -1L, -1L, -1L);

		return "grades";
	}

	@RequestMapping(value = "/api/v1/schools", method = RequestMethod.POST,
			consumes = "application/json", produces = "application/json")
	//@Transactional
	public ResponseEntity<School> save1 (@RequestBody School school) {
		boolean anyInvalidData = false;
		if (school.getOrganisation() == null) {
			LOG.error("Organisation information is missing!");
			anyInvalidData = true;
		}
		if (school.getName() == null || school.getName().trim().length() == 0) {
			LOG.error("School name is missing!");
			anyInvalidData = true;
		}
		if (school.getSchoolAdminEmail() == null || school.getSchoolAdminEmail().trim().length() == 0) {
			LOG.error("School admin email is missing!");
			anyInvalidData = true;
		}

		Organisation org = null;
		try{
			org = orgRepo.findById(school.getOrganisation().getOrgId()).get();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			anyInvalidData = true;
		}

		if(anyInvalidData) {
			return ResponseEntity.badRequest().build();
		}

		school.setOrganisation(org);

		boolean alreadyExists = service.existsAlready(school, school.getOrganisation());
		if(alreadyExists) {
			LOG.error("School with same name already exists.");
			return ResponseEntity.badRequest().build();
		}

		LocalDateTime timeNow = LocalDateTime.now();
		school.setDateCreated(timeNow);
		school.setLastUpdated(timeNow);

		service.registerSchoolAdmin(school);

		return ResponseEntity.ok(schoolRepo.save(school));
	}

//	@RequestMapping(value = "/api/v1/{orgId}/schools", method = RequestMethod.POST)
//	@Transactional
//	public ResponseEntity<School> save (@PathVariable Long orgId, @RequestBody School school) {
//		boolean anyInvalidData = false;
//		if (school.getName() == null || school.getName().trim().length() == 0) {
//			LOG.error("School name is missing!");
//			anyInvalidData = true;
//		}
//
//		Organisation org = null;
//		try{
//			org = orgRepo.findById(orgRepo.findById(orgId).get().getOrgId()).get();
//		} catch (Exception ex) {
//			LOG.error(ex.getMessage());
//			anyInvalidData = true;
//		}
//
//		if(anyInvalidData) {
//			return ResponseEntity.badRequest().build();
//		}
//
//		school.setOrganisation(org);
//		LocalDateTime timeNow = LocalDateTime.now();
//		school.setDateCreated(timeNow);
//		school.setLastUpdated(timeNow);
//
//		return ResponseEntity.ok(schoolRepo.save(school));
//	}

	@GetMapping("/api/v1/schools")
	public ResponseEntity<List<School>> get () {
		return ResponseEntity.ok(schoolRepo.findAll());
	}

	@GetMapping("/api/v1/schools/{id}")
	public ResponseEntity<School> read(@PathVariable("id") Long id) {
		School school = service.get(id);
		if (school == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(school);
		}
	}

	@RequestMapping(value = "/api/v1/schools/{id}", method = RequestMethod.PUT)
	public ResponseEntity<School> update (@PathVariable Long id, @RequestBody School school) {
		School schoolTemp = service.get(id);
		if (schoolTemp == null) {
			return ResponseEntity.notFound().build();
		}

		school.setSchoolId(schoolTemp.getSchoolId());
		if(school.getName() != null) {
			boolean alreadyExists = service.existsAlready(school, schoolTemp.getOrganisation());
			if (alreadyExists) {
				LOG.error("School with same name already exists.");
				return ResponseEntity.badRequest().build();
			}

			if (school.getName() != schoolTemp.getName()) {
				schoolTemp.setName(school.getName());
			}
		}

		if(school.getContactNumber() > 0 ) {
			schoolTemp.setContactNumber(school.getContactNumber());
		}

		if(school.getOrganisation() != null) {
			schoolTemp.setOrganisation(school.getOrganisation());
		}

		if(school.getSchoolSignupCompleted() != null) {
			schoolTemp.setSchoolSignupCompleted(school.getSchoolSignupCompleted());
		}

		if(school.getSchoolAdminEmail() != null) {
			schoolTemp.setSchoolAdminEmail(school.getSchoolAdminEmail());
		}

		LocalDateTime timeNow = LocalDateTime.now();
		schoolTemp.setLastUpdated(timeNow);

		return ResponseEntity.ok(schoolRepo.save(schoolTemp));
	}

	@DeleteMapping("/api/v1/schools/{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		School school = schoolRepo.getOne(id);
		if(school != null) {
			Organisation org = school.getOrganisation();
			org.getSchools().remove(school);
			orgRepo.save(org);

			schoolRepo.deleteById(id);
		}

		//DeleteResponse delResp = new DeleteResponse(id, "School", HttpStatus.OK);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/api/v2/schools/{id}")
	public ResponseEntity<DeleteResponse> deleteV2 (@PathVariable Long id) {
		DeleteResponse dr = new DeleteResponse();
		dr.setEntityType("School");
		dr.setId(id);
		try {
			School school = schoolRepo.getOne(id);
			if(school != null) {
				Organisation org = school.getOrganisation();
				org.getSchools().remove(school);
				orgRepo.save(org);

				schoolRepo.deleteById(id);
				dr.setStatus(DeleteStatus.DeleteSuccess);
				return ResponseEntity.ok(dr);
			}
			dr.setStatus(DeleteStatus.DeleteFailed);
			return ResponseEntity.ok(dr);
		} catch (Exception ex) {
			dr.setStatus(DeleteStatus.DeleteFailed);
			dr.setDeleteMessage(ex.getMessage());
			return ResponseEntity.ok(dr);
		}
	}

	@GetMapping("/api/v1/schools/{id}/grades")
	public ResponseEntity<List<Grade>> listGrades(@PathVariable("id") Long id) {
		School school = service.get(id);
		if (school == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(school.getGrades());
		}
	}

	@GetMapping("/api/v1/schools/{id}/teachers")
	public ResponseEntity<List<Teacher>> listTeachers(@PathVariable("id") Long id) {
		School school = service.get(id);
		if (school == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(school.getTeachers());
		}
	}

	@GetMapping("/api/v2/schools")
	public ResponseEntity<PagedModel<SchoolModel>> getSchoolsPaginated (
			@RequestParam(defaultValue = "") String nameFilter,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") Sort.Direction sortOrder
	) {
		Page<School> schoolPage = service.fetchSchoolDataAsPageWithFilteringAndSorting(nameFilter, page, size, sortList, sortOrder.toString());
		// Use the pagedResourcesAssembler and customerModelAssembler to convert data to PagedModel format
		return ResponseEntity.ok(pagedSchoolAssembler.toModel(schoolPage, schoolModelAssembler));
	}
}
