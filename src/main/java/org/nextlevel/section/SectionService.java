package org.nextlevel.section;

import org.nextlevel.grade.Grade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SectionService {
	@Autowired
	private SectionRepository repo;
	
	public List<Section> listAll() {
		return repo.findAll();
	}

	public List<Section> listAll(Long orgId, Long schoolId, Long gradeId) {
		return repo.findAll()
				.stream()
					.filter(section -> section.getGrade().getGradeId().longValue() == gradeId.longValue())
//					.filter(section -> section.getGrade().getSchool().getSchoolId() == schoolId)
//					.filter(section -> section.getGrade().getSchool().getOrganisation().getOrgId() == orgId)
				.collect(Collectors.toList());
	}

	public void save(Section grade) {
		repo.save(grade);
	}
	
	public Section get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }

    public boolean existsAlready(Section section, Grade grade) {
		List<Section> sections = grade.getSections();
		if(null != sections && sections.size()>0) {
			for (Section sectionTemp:
					sections) {
				if(sectionTemp.getName().equalsIgnoreCase(section.getName().trim())) {
					return true;
				}
			}
		}

		return false;
    }
}
