package org.nextlevel.grade;

import org.nextlevel.school.School;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeService {
	@Autowired
	private GradeRepository repo;
	
	public List<Grade> listAll() {
		return repo.findAll();
	}

	public List<Grade> listAll(Long orgId, Long schoolId) {
		return repo.findAll()
				.stream()
					.filter(grade -> grade.getSchool().getSchoolId().longValue() == schoolId.longValue())
					.filter(grade -> grade.getSchool().getOrganisation().getOrgId().longValue() == orgId.longValue())
				.collect(Collectors.toList());
	}

	public void save(Grade grade) {
		repo.save(grade);
	}
	
	public Grade get(Long id) {
		return repo.findById(id).get();
	}
	
	public void delete(Long id) { repo.deleteById(id); }

	public boolean existsAlready(Grade grade, School school) {
		List<Grade> grades = school.getGrades();
		if(null != grades && grades.size()>0) {
			for (Grade gradeTemp:
					grades) {
				if(gradeTemp.getName().equalsIgnoreCase(grade.getName().trim())) {
					return true;
				}
			}
		}

		return false;
	}
}
