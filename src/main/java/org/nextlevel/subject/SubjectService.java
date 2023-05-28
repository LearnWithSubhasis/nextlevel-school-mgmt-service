package org.nextlevel.subject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class SubjectService {
	@Autowired
	private SubjectRepository repo;
	
	public List<Subject> listAll() {
		return repo.findAll();
	}

	public List<Subject> listAll(Long orgId, Long schoolId) {
		return repo.findAll()
				.stream()
				.filter(subject -> subject.getSchool().getSchoolId().longValue() == schoolId.longValue())
				.collect(Collectors.toList());
	}

	public void save(Subject subject) {
		repo.save(subject);
	}
	
	public Subject get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }

	public Subject getByName(String name) {
		Subject subjectSelected = null;
		List<Subject> subjectList = new ArrayList<>();
		Supplier<Stream<Subject>> subjectsStreamSupp = () -> repo.findAll()
				.stream()
				.filter(subject -> subject.getName().equalsIgnoreCase(name));

		subjectList = subjectsStreamSupp.get().collect(Collectors.toList());

		if (null != subjectList && subjectList.size() == 1) {
			subjectSelected = subjectList.get(0);
		}

		return subjectSelected;
	}
}
