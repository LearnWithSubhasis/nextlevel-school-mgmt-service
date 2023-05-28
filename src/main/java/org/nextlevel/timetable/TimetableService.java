package org.nextlevel.timetable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimetableService {
    @Autowired
    private TimetableRepository repo;

    public List<Timetable> listAll() {
        return repo.findAll();
    }

    public List<Timetable> listAll(Long orgId, Long schoolId, Long gradeId, Long sectionId) {
        return repo.findAll()
                .stream()
				.filter(timetable -> timetable.getSection().getSectionId().longValue() == sectionId.longValue())
                .collect(Collectors.toList());
    }

    public void save(Timetable timetable) {
        repo.save(timetable);
    }

    public Timetable get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) { repo.deleteById(id); }

    public Timetable getForSection(Long sectionId) {
        List<Timetable> listTable = repo.findAll()
                .stream()
                .filter(timetable -> timetable.getSection().getSectionId().longValue() == sectionId.longValue())
                .collect(Collectors.toList());

        if (listTable != null && listTable.size() > 0) {
            return listTable.get(0);
        }

        return null;
    }
}
