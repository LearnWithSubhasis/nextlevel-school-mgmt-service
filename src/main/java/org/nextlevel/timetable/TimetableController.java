package org.nextlevel.timetable;

import org.nextlevel.common.DeleteResponse;
import org.nextlevel.common.DeleteStatus;
import org.nextlevel.section.Section;
import org.nextlevel.section.SectionRepository;
import org.nextlevel.subject.SubjectService;
import org.nextlevel.teacher.TeacherService;
import org.nextlevel.timetableentry.TimetableEntry;
import org.nextlevel.timetableentry.TimetableEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@RequestMapping
public class TimetableController {
	@Autowired
	private TimetableService service;
	@Autowired
	private TimetableRepository timetableRepo;
	@Autowired
	private TimetableEntryRepository entryRepo;
	@Autowired
	private SectionRepository sectionRepo;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private SubjectService subjectService;
	public static final Logger LOG = LoggerFactory.getLogger(TimetableController.class);

	@PostMapping("/api/v1/timetables")
	@Transactional
	public ResponseEntity<Timetable> save (@RequestBody Timetable timetable) {
		Section section = sectionRepo.findById(timetable.getSection().getSectionId()).get();
		timetable.setSection(section);
		Timetable tt = timetableRepo.save(timetable);

		List<TimetableEntry> entries = timetable.getEntries();
		if (null != entries && entries.size() > 0) {
			for (TimetableEntry entry: entries) {
				if(teacherService.get(entry.getTeacher().getTeacherId()) == null) {
					LOG.error("Teacher with id missing: " + entry.getTeacher().getTeacherId());
					return ResponseEntity.notFound().build();
				}

				if(subjectService.get(entry.getSubject().getSubjectId()) == null) {
					LOG.error("Subject with id missing: " + entry.getSubject().getSubjectId());
					return ResponseEntity.notFound().build();
				}

				entry.setTimetable(tt);
				entryRepo.save(entry);
			}
		}

		return ResponseEntity.ok(tt);
	}

	@GetMapping("/api/v1/timetables")
	public ResponseEntity<List<Timetable>> get () {
		return ResponseEntity.ok(timetableRepo.findAll());
	}

	@GetMapping("/api/v1/timetables/{id}")
	public ResponseEntity<Timetable> read(@PathVariable("id") Long id) {
		Timetable timetable = service.get(id);
		if (timetable == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(timetable);
		}
	}

	@PutMapping("/api/v1/timetables/{id}")
	public ResponseEntity<Timetable> update (@PathVariable Long id, @RequestBody Timetable timetable) {
		Timetable timetableOld = service.get(id);
		if (timetableOld == null) {
			return ResponseEntity.notFound().build();
		}

		if (timetable.getSection() != null) {
			timetableOld.setSection(timetable.getSection());
		}

		if (timetable.getStartDate() != null) {
			timetableOld.setStartDate(timetable.getStartDate());
		}

		if (timetable.getEndDate() != null) {
			timetableOld.setEndDate(timetable.getEndDate());
		}

		Timetable tt = timetableRepo.save(timetableOld);
		List<TimetableEntry> entries = timetable.getEntries();
		if (null != entries && entries.size() > 0) {
			for (TimetableEntry entry: entries) {
				if(teacherService.get(entry.getTeacher().getTeacherId()) == null) {
					LOG.error("Teacher with id missing: " + entry.getTeacher().getTeacherId());
					return ResponseEntity.notFound().build();
				}

				if(subjectService.get(entry.getSubject().getSubjectId()) == null) {
					LOG.error("Subject with id missing: " + entry.getSubject().getSubjectId());
					return ResponseEntity.notFound().build();
				}

				entry.setTimetable(tt);
				entryRepo.save(entry);
			}
		}

		return ResponseEntity.ok(tt);
	}

	@DeleteMapping("/api/v1/timetables/{id}")
	@Transactional
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		Timetable timetable = timetableRepo.findById(id).get();
		List<TimetableEntry> entries = timetable.getEntries();
		for (TimetableEntry entry: entries) {
			entryRepo.delete(entry);
		}

		timetableRepo.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
	}

	@RequestMapping(value = "/api/v2/timetables/{id}", method = RequestMethod.DELETE)
	@Transactional
	public ResponseEntity<DeleteResponse> deleteV2 (@PathVariable Long id) {
		DeleteResponse dr = new DeleteResponse();
		dr.setEntityType("TimeTable");
		dr.setId(id);
		try {
			Timetable timetable = timetableRepo.findById(id).get();
			List<TimetableEntry> entries = timetable.getEntries();
			for (TimetableEntry entry: entries) {
				entryRepo.delete(entry);
			}

			timetableRepo.deleteById(id);

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
