package org.nextlevel.org;

import java.time.LocalDateTime;
import java.util.List;

import org.nextlevel.common.CommonController;
import org.nextlevel.common.DeleteResponse;
import org.nextlevel.common.DeleteStatus;
import org.nextlevel.event.Event;
import org.nextlevel.school.School;
import org.nextlevel.school.SchoolModel;
import org.nextlevel.school.SchoolModelAssembler;
import org.nextlevel.school.SchoolService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class OrgController {
	@Autowired
	private OrganisationService orgService;
	@Autowired
	private SchoolService schoolService;
	@Autowired
	private CommonController cc;
	@Autowired
	private OrganisationRepository orgRepo;
	@Autowired
	private OrgModelAssembler orgModelAssembler;
	@Autowired
	private SchoolModelAssembler schoolModelAssembler;
	@Autowired
	private PagedResourcesAssembler<School> pagedSchoolAssembler;
	public static final Logger LOG = LoggerFactory.getLogger(OrgController.class);

	@Autowired
	private OrganisationService service;

	@Autowired
	private PagedResourcesAssembler<Organisation> pagedOrgAssembler;

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public String viewHomePage(Model model) {
		List<Organisation> listOrganisations = orgService.listAll();
		model.addAttribute("listOrganisations", listOrganisations);
		
		return "organisations";
	}
	
	@RequestMapping(value = "/new", method = RequestMethod.GET)
	public String showNewOrganisationForm(Model model) {
		Organisation organisation = new Organisation();
		model.addAttribute("organisation", organisation);
		
		return "new_organisation";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveOrganisation(@ModelAttribute("organisation") Organisation organisation) {
		orgService.save(organisation);
		
		return "redirect:/list";
	}
	
	@RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
	public ModelAndView showEditOrganisationForm(@PathVariable(name = "id") Long id) {
		ModelAndView mav = new ModelAndView("edit_organisation");
		
		Organisation organisation = orgService.get(id);
		mav.addObject("organisation", organisation);
		
		return mav;
	}	
	
	@RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
	public String deleteOrganisation(@PathVariable(name = "id") Long id) {
		orgService.delete(id);
		
		return "redirect:/list";
	}

	@RequestMapping(value = "/listSchools/{orgId}", method = RequestMethod.GET)
	public String listSchools(Model model, @PathVariable(name = "orgId") Long orgId) {
		List<School> listSchools = schoolService.listAll(orgId);
		model.addAttribute("listSchools", listSchools);
		model.addAttribute("orgId", orgId);

		model.addAttribute("organisation", orgService.get(orgId).getName());
		cc.setModelAttributes(model, orgId, -1L, -1L, -1L, -1L);

		return "schools";
	}


	@RequestMapping(value = "/api/v1/orgs", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Organisation> save (@RequestBody Organisation organisation) {
		Organisation org = null;

		boolean anyInvalidData = false;
		if (organisation.getOrgAdminEmail() == null || organisation.getOrgAdminEmail().trim().length() == 0) {
			LOG.error("Org admin email is missing!");
			anyInvalidData = true;
		} else if (organisation.getName() == null || organisation.getName().trim().length() == 0) {
			LOG.error("Org name is missing!");
			anyInvalidData = true;
		}

		if (anyInvalidData) {
			return ResponseEntity.badRequest().build();
		}

		try {
			LocalDateTime timeNow = LocalDateTime.now();
			organisation.setDateCreated(timeNow);
			organisation.setLastUpdated(timeNow);
			org = orgRepo.save(organisation);
		} catch (DataIntegrityViolationException ex) {
			LOG.error("Org name - unique constraint error. " + ex.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			return ResponseEntity.badRequest().build();
		}
		orgService.registerOrgAdmin(org);

		return ResponseEntity.ok(org);
	}

	@GetMapping("/api/v1/orgs")
	public ResponseEntity<List<Organisation>> get () {
		return ResponseEntity.ok(orgRepo.findAll());
	}

	@GetMapping("/api/v1/orgs/{id}")
	public ResponseEntity<Organisation> read(@PathVariable("id") Long id) {
		Organisation organisation = orgService.get(id);
		if (organisation == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(organisation);
		}
	}

	@PutMapping("/api/v1/orgs/{id}")
	public ResponseEntity<Organisation> update (@PathVariable Long id, @RequestBody Organisation organisation) {
		Organisation orgTemp = orgService.get(id);
		if (orgTemp == null) {
			return ResponseEntity.notFound().build();
		}

		organisation.setOrgId(orgTemp.getOrgId());
		if(organisation.getName()==null) {
			organisation.setName(orgTemp.getName());
		}

		if(organisation.getCounty()==null) {
			organisation.setCounty(orgTemp.getCounty());
		}

		if(organisation.getOrgAdminEmail()==null) {
			organisation.setOrgAdminEmail(orgTemp.getOrgAdminEmail());
		}

		if(organisation.getContactNumber() <= 0) {
			organisation.setContactNumber(orgTemp.getContactNumber());
		}

		if(organisation.getAddress()==null) {
			organisation.setAddress(orgTemp.getAddress());
		}

		if(organisation.getOrgSignupCompleted() == null) {
			organisation.setOrgSignupCompleted(orgTemp.getOrgSignupCompleted());
		}

		organisation.setLastUpdated(LocalDateTime.now());

		try {
			organisation = orgRepo.save(organisation);
		} catch (DataIntegrityViolationException ex) {
			LOG.error("Org name - unique constraint error. " + ex.getMessage());
			return ResponseEntity.badRequest().build();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			return ResponseEntity.badRequest().build();
		}

		return ResponseEntity.ok(organisation);
	}

	@DeleteMapping("/api/v1/orgs/{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		orgRepo.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/api/v2/orgs/{id}")
	public ResponseEntity<DeleteResponse> deleteV2 (@PathVariable Long id) {
		DeleteResponse dr = new DeleteResponse();
		dr.setEntityType("Organisation");
		dr.setId(id);
		try {
			orgRepo.deleteById(id);
			dr.setStatus(DeleteStatus.DeleteSuccess);
			return ResponseEntity.ok(dr);
		} catch (Exception ex) {
			dr.setStatus(DeleteStatus.DeleteFailed);
			dr.setDeleteMessage(ex.getMessage());
			return ResponseEntity.ok(dr);
		}
	}

	@GetMapping("/api/v1/orgs/{id}/schools")
	public ResponseEntity<List<School>> getSchools(@PathVariable Long id) {
		Organisation org = orgService.get(id);
		return ResponseEntity.ok(org.getSchools());
	}

	@GetMapping("/api/v1/orgs/{id}/events")
	public ResponseEntity<List<Event>> getEvents(@PathVariable Long id) {
		Organisation org = orgService.get(id);
		return ResponseEntity.ok(org.getEvents());
	}

	@GetMapping("/api/v2/orgs")
	public ResponseEntity<PagedModel<OrgModel>> getOrgsPaginated (
			@RequestParam(defaultValue = "") String nameFilter,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") Sort.Direction sortOrder
	) {
		Page<Organisation> orgPage = service.fetchOrgDataAsPageWithFilteringAndSorting(nameFilter, page, size, sortList, sortOrder.toString());
		// Use the pagedResourcesAssembler and customerModelAssembler to convert data to PagedModel format
		return ResponseEntity.ok(pagedOrgAssembler.toModel(orgPage, orgModelAssembler));
	}

	@GetMapping("/api/v2/orgs/{id}/schools")
	public ResponseEntity<PagedModel<SchoolModel>> getSchoolsPaginated(
			@PathVariable Long id,
			@RequestParam(defaultValue = "") String nameFilter,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "") List<String> sortList,
			@RequestParam(defaultValue = "DESC") Sort.Direction sortOrder
	) {
//		Organisation org = orgService.get(id);
//
//		return ResponseEntity.ok(org.getSchools());

		Page<School> schoolPage = service.fetchSchoolsDataAsPageWithFilteringAndSorting(id, nameFilter, page, size, sortList, sortOrder.toString());
		// Use the pagedResourcesAssembler and customerModelAssembler to convert data to PagedModel format
		return ResponseEntity.ok(pagedSchoolAssembler.toModel(schoolPage, schoolModelAssembler));
	}
}
