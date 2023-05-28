package org.nextlevel.event;

import org.nextlevel.common.CommonController;
import org.nextlevel.grade.GradeService;
import org.nextlevel.org.OrganisationService;
import org.nextlevel.school.SchoolService;
import org.nextlevel.section.SectionService;
import org.nextlevel.student.StudentService;
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
public class EventController {
	@Autowired
	private EventService service;
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
	private EventRepository eventRepo;

	@GetMapping("/event/list")
	public String viewHomePage(Model model
			, @RequestParam (name = "orgId", required = false, defaultValue = "-1") Long orgId
			, @RequestParam (name = "schoolId", required = false, defaultValue = "-1") Long schoolId
			, @RequestParam (name = "gradeId", required = false, defaultValue = "-1") Long gradeId
			, @RequestParam (name = "sectionId", required = false, defaultValue = "-1") Long sectionId
			, @RequestParam (name = "studentId", required = false, defaultValue = "-1") Long studentId) {
		List<Event> listEvents = null;

		if (studentId != null && studentId > 0) {
			listEvents = service.listAllStudentEvents(orgId, schoolId, gradeId, sectionId, studentId);
		} else if (sectionId != null && sectionId > 0) {
			listEvents = service.listAllSectionEvents(orgId, schoolId, gradeId, sectionId);
		} else if (gradeId != null && gradeId > 0) {
			listEvents = service.listAllGradeEvents(orgId, schoolId, gradeId);
		} else if (schoolId != null && schoolId > 0) {
			listEvents = service.listAllSchoolEvents(orgId, schoolId);
		} else if (orgId != null && orgId > 0) {
			listEvents = service.listAllOrganisationEvents(orgId);
		} else if(orgId == null || orgId < 0) {
			listEvents = service.listAllGlobalEventsOnly();
		}

		model.addAttribute("listEvents", listEvents);
		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("sectionId", sectionId);
		model.addAttribute("studentId", studentId);

		cc.setModelAttributes(model, orgId, schoolId, gradeId, sectionId, studentId);

		return "events";
	}

	@GetMapping("/events/new")
	public String showNewEventForm(Model model
			, @RequestParam (name = "orgId", required = false, defaultValue = "-1") Long orgId
			, @RequestParam (name = "schoolId", required = false, defaultValue = "-1") Long schoolId
			, @RequestParam (name = "gradeId", required = false, defaultValue = "-1") Long gradeId
			, @RequestParam (name = "sectionId", required = false, defaultValue = "-1") Long sectionId) {

		Event event = new Event();
		model.addAttribute("event", event);
		if (null != orgId && orgId > 0) {
			event.setOrganisation(orgService.get(orgId));
		}

		model.addAttribute("orgId", orgId);
		model.addAttribute("schoolId", schoolId);
		model.addAttribute("gradeId", gradeId);
		model.addAttribute("sectionId", sectionId);

		return "new_event";
	}

	@RequestMapping(value = "/events/save", method = RequestMethod.POST)
	public String saveEvent(@ModelAttribute("event") Event event
			, @RequestParam (name = "orgId", required = false, defaultValue = "-1") Long orgId
			, @RequestParam (name = "schoolId", required = false, defaultValue = "-1") Long schoolId
			, @RequestParam (name = "gradeId", required = false, defaultValue = "-1") Long gradeId
			, @RequestParam (name = "sectionId", required = false, defaultValue = "-1") Long sectionId) {

		if (null != orgId && orgId > 0) {
			event.setOrganisation(orgService.get(orgId));
		}

		if (null != schoolId && schoolId > 0) {
			event.setSchool(schoolService.get(schoolId));
		}

		if (null != gradeId && gradeId > 0) {
			event.setGrade(gradeService.get(gradeId));
		}

		if (null != sectionId && sectionId > 0) {
			event.setSection(sectionService.get(sectionId));
		}

		service.save(event);

		return "redirect:/event/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId + "&sectionId=" + sectionId;
	}

	@GetMapping("/events/edit/{id}")
	public ModelAndView showEditEventForm(@PathVariable(name = "id") Long id
			, @RequestParam (name = "orgId", required = false, defaultValue = "-1") Long orgId
			, @RequestParam (name = "schoolId", required = false, defaultValue = "-1") Long schoolId
			, @RequestParam (name = "gradeId", required = false, defaultValue = "-1") Long gradeId
			, @RequestParam (name = "sectionId", required = false, defaultValue = "-1") Long sectionId) {
		ModelAndView mav = new ModelAndView("edit_event");

		Event event = service.get(id);
		mav.addObject("event", event);
		mav.addObject("orgId", String.valueOf(orgId));
		mav.addObject("schoolId", String.valueOf(schoolId));
		mav.addObject("gradeId", String.valueOf(gradeId));
		mav.addObject("sectionId", String.valueOf(sectionId));

		return mav;
	}

	@GetMapping("/events/delete/{id}")
	public String deleteEvent(@PathVariable(name = "id") Long id
			, @RequestParam (name = "orgId", required = false, defaultValue = "-1") Long orgId
			, @RequestParam (name = "schoolId", required = false, defaultValue = "-1") Long schoolId
			, @RequestParam (name = "gradeId", required = false, defaultValue = "-1") Long gradeId
			, @RequestParam (name = "sectionId", required = false, defaultValue = "-1") Long sectionId) {
		service.delete(id);

		return "redirect:/event/list?orgId=" + orgId + "&schoolId=" + schoolId + "&gradeId=" + gradeId + "&sectionId=" + sectionId;
	}

	@PostMapping("/api/v1/events")
	public ResponseEntity<Event> save (@RequestBody Event event) {
		LocalDateTime timeNow = LocalDateTime.now();
		event.setDateCreated(timeNow);
		event.setLastUpdated(timeNow);

		return ResponseEntity.ok(eventRepo.save(event));
	}

	@GetMapping("/api/v1/events")
	public ResponseEntity<List<Event>> get () {
		return ResponseEntity.ok(eventRepo.findAll());
	}

	@GetMapping("/api/v1/events/{id}")
	public ResponseEntity<Event> read(@PathVariable("id") Long id) {
		Event event = service.get(id);
		if (event == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(event);
		}
	}

	@PutMapping("/api/v1/events/{id}")
	public ResponseEntity<Event> update (@PathVariable Long id, @RequestBody Event event) {
		Event eventOld = service.get(id);
		if (eventOld == null) {
			return ResponseEntity.notFound().build();
		}

		if(event.getName() != null && event.getName().trim().length() > 0) {
			eventOld.setName(event.getName());
		}

		if(event.getEventDescription() != null && event.getEventDescription().trim().length() > 0) {
			eventOld.setEventDescription(event.getEventDescription());
		}

		if(event.getEventStartTime() != null) {
			eventOld.setEventStartTime(event.getEventStartTime());
		}

		if(event.getEventEndTime() != null) {
			eventOld.setEventEndTime(event.getEventEndTime());
		}

		LocalDateTime timeNow = LocalDateTime.now();
		event.setLastUpdated(timeNow);

		return ResponseEntity.ok(eventRepo.save(eventOld));
	}

	@DeleteMapping("/api/v1/events/{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		eventRepo.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}
}
