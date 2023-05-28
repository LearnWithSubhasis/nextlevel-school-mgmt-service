package org.nextlevel.timetableentry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/v1/entries")
public class TimetableEntryController {
	@Autowired
	private TimetableEntryService service;
	@Autowired
	private TimetableEntryRepository entryRepo;

	public static final Logger LOG = LoggerFactory.getLogger(TimetableEntryController.class);

	@PostMapping()
	public ResponseEntity<TimetableEntry> save (@RequestBody TimetableEntry entry) {
		if (entry.getTimetable() == null) {
			LOG.error("Couldn't find timetable.");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return ResponseEntity.ok(entryRepo.save(entry));
	}

	@GetMapping()
	public ResponseEntity<List<TimetableEntry>> get () {
		return ResponseEntity.ok(entryRepo.findAll());
	}

	@GetMapping("{id}")
	public ResponseEntity<TimetableEntry> read(@PathVariable("id") Long id) {
		TimetableEntry entry = service.get(id);
		if (entry == null) {
			return ResponseEntity.notFound().build();
		} else {
			return ResponseEntity.ok(entry);
		}
	}

	@PutMapping("{id}")
	public ResponseEntity<TimetableEntry> update (@PathVariable Long id, @RequestBody TimetableEntry entry) {
		TimetableEntry entryOld = service.get(id);
		if (entryOld == null) {
			return ResponseEntity.notFound().build();
		}

		if (entry.getTeacher() != null) {
			entryOld.setTeacher(entry.getTeacher());
		}

		if (entry.getSubject() != null) {
			entryOld.setSubject(entry.getSubject());
		}

		return ResponseEntity.ok(entryRepo.save(entryOld));
	}

	@DeleteMapping("{id}")
	public ResponseEntity<HttpStatus> delete (@PathVariable Long id) {
		entryRepo.deleteById(id);
		return new ResponseEntity<HttpStatus>(HttpStatus.NO_CONTENT);
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
