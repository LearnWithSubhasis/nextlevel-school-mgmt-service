package org.nextlevel.timetableentry;

import org.nextlevel.teacher.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimetableEntryService {
    @Autowired
    private TimetableEntryRepository repo;

    public List<TimetableEntry> listAll() {
        return repo.findAll();
    }

    public List<TimetableEntry> listAll(Long orgId, Long schoolId, Long gradeId, Long sectionId, Long timetableId) {
        return repo.findAll()
                .stream()
				.filter(entry -> entry.getTimetableEntryId().longValue() == timetableId.longValue())
                .collect(Collectors.toList());
    }

    public void save(TimetableEntry entry) {
        repo.save(entry);
    }

    public TimetableEntry get(Long id) {
        return repo.findById(id).get();
    }

    public void delete(Long id) { repo.deleteById(id); }

    public List<TimetableEntry> filterTimeTableWeekly(Teacher teacher) {
        return repo.findAll()
                .stream()
                .filter(entry -> entry.getTeacher().equals(teacher))
                //.filter(entry -> entry.getDayOfWeek().equals(currentTime.getDayOfWeek()))
                //.filter(entry -> currentDate.after(entry.getTimetable().getStartDate())
                //        && currentDate.before(entry.getTimetable().getEndDate()))
                ////.filter(entry -> currentTime.getHour() > entry.getStartTime().getHour())
                .sorted(new TimetableEntryComparator())
                .collect(Collectors.toList());
    }

    public List<TimetableEntry> filterTimeTableForToday(Teacher teacher, LocalDateTime currentTime) {
        Date currentDate = Date.from(currentTime.toInstant(ZoneId.systemDefault().getRules().getOffset(currentTime)));
        List<TimetableEntry> entries = repo.findAll()
                .stream()
                .filter(entry -> entry.getTeacher().equals(teacher))
//                .filter(entry -> currentDate.after(entry.getTimetable().getStartDate())
//                        && currentDate.before(entry.getTimetable().getEndDate()))
                .filter(entry -> entry.getDayOfWeek() == currentTime.getDayOfWeek())
                .sorted(new TimetableEntryComparator())
                .collect(Collectors.toList());

        return entries;
    }
}

class TimetableEntryComparator implements Comparator<TimetableEntry> {

    @Override
    public int compare(TimetableEntry o1, TimetableEntry o2) {
        //return (o1.getStartTime().isBefore(o2.getStartTime())) ? 1 : 0;
        return (o1.getStartTime().compareTo(o2.getStartTime()));
    }
}
