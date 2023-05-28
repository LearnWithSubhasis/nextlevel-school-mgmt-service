package org.nextlevel.subject;

import org.nextlevel.common.DeleteResponse;
import org.nextlevel.common.DeleteStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping
public class SubjectController {
	@Autowired
	private SubjectService service;
//	@Autowired
//	private SectionService sectionService;
//	@Autowired
//	private SchoolService schoolService;
//	@Autowired
//	private OrganisationService orgService;
//	@Autowired
//	private GradeService gradeService;
//	@Autowired
//	private CommonController cc;
	@Autowired
	private SubjectRepository subjectRepo;

	@PostMapping("/api/v1/subjects")
	public ResponseEntity<Subject> save (@RequestBody Subject subject) {
		LocalDateTime timeNow = LocalDateTime.now();
		subject.setDateCreated(timeNow);
		subject.setLastUpdated(timeNow);

		return ResponseEntity.ok(subjectRepo.save(subject));
	}

	@GetMapping("/api/v1/subjects")
	public ResponseEntity<List<Subject>> get () {
		return ResponseEntity.ok(subjectRepo.findAll());
	}

	@GetMapping("/api/v1/subjects/{id}")
	public ResponseEntity<Subject> read(@PathVariable("id") Long id) {
		Subject subject = service.get(id);
		if (subject == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(subject);
		}
	}

	@PutMapping("/api/v1/subjects/{id}")
	public ResponseEntity<Subject> update (@PathVariable Long id, @RequestBody Subject subject) {
		Subject subjectOld = service.get(id);
		if (subjectOld == null) {
			return ResponseEntity.notFound().build();
		}

		if(subject.getName() != null && subject.getName().trim().length() > 0) {
			subjectOld.setName(subject.getName().trim());
		}

		subjectOld.setLastUpdated(LocalDateTime.now());
		return ResponseEntity.ok(subjectRepo.save(subjectOld));
	}

	@DeleteMapping("/api/v1/subjects/{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		subjectRepo.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@DeleteMapping("/api/v2/subjects/{id}")
	public ResponseEntity<DeleteResponse> deleteV2 (@PathVariable Long id) {
		DeleteResponse dr = new DeleteResponse();
		dr.setEntityType("Subject");
		dr.setId(id);
		try {
			subjectRepo.deleteById(id);
			dr.setStatus(DeleteStatus.DeleteSuccess);
			return ResponseEntity.ok(dr);
		} catch (Exception ex) {
			dr.setStatus(DeleteStatus.DeleteFailed);
			dr.setDeleteMessage(ex.getMessage());
			return ResponseEntity.ok(dr);
		}
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
