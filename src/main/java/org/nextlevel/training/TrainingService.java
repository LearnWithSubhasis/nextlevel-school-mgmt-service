//package org.nextlevel.training;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//import java.util.stream.Collectors;
//
//@Service
//public class TrainingService {
//	@Autowired
//	private TrainingRepository repo;
//
//	public List<Event> listAll() {
//		return repo.findAll();
//	}
//
//	public List<Event> listAll(Long orgId, Long schoolId, Long gradeId, Long sectionId) {
//		List<Event> events = repo.findAll()
//				.stream()
////				.filter(event -> event.getSection().getSectionId().longValue() == sectionId.longValue())
////				.filter(student -> student.getSection().getGrade().getGradeId() == gradeId)
////				.filter(student -> student.getSection().getGrade().getSchool().getSchoolId() == schoolId)
////				.filter(student -> student.getSection().getGrade().getSchool().getOrganisation().getOrgId() == orgId)
//				.collect(Collectors.toList());
//
//		if (null != orgId && orgId > 0) {
//			List<Event> eventsGlobal = events.stream()
//					.filter(event -> event.getOrganisation() == null || event.getOrganisation().getOrgId() == -1)
//					.collect(Collectors.toList());
//			List<Event> eventsOrg = events.stream()
//					.filter(event -> event.getOrganisation() != null && event.getOrganisation().getOrgId() == orgId)
//					.collect(Collectors.toList());
//
//			events = this.union(eventsOrg, eventsGlobal);
//		}
//
//		if (null != schoolId && schoolId > 0) {
//			List<Event> eventsSchool = events.stream()
//					.filter(event -> event.getSchool() != null && event.getSchool().getSchoolId() == schoolId)
//					.collect(Collectors.toList());
//
//			events = this.union(events, eventsSchool);
//		}
//
//		return events;
//	}
//
//	public List<Event> listAllGlobalEventsOnly() {
//		List<Event> events = repo.findAll()
//				.stream()
//				.filter(event -> event.getOrganisation() == null || event.getOrganisation().getOrgId() == -1)
//				.filter(event -> event.getEventType().equalsIgnoreCase("Global"))
//				.collect(Collectors.toList());
//
//		return events;
//	}
//
//	public List<Event> listAllOrganisationEventsOnly(Long orgId) {
//		List<Event> events = listAll();
//
//		if (null != orgId && orgId > 0) {
//			events = events.stream()
//					.filter(event -> event.getOrganisation() != null && event.getOrganisation().getOrgId() == orgId)
//					.filter(event -> event.getEventType().equalsIgnoreCase("Organisation"))
//					.collect(Collectors.toList());
//		}
//
//		return events;
//	}
//
//	public List<Event> listAllSchoolEventsOnly(Long orgId, Long schoolId) {
//		List<Event> events = listAll();
//
//		if (null != schoolId && schoolId > 0) {
//			events = events.stream()
//					.filter(event -> event.getSchool() != null && event.getSchool().getSchoolId() == schoolId)
//					.filter(event -> event.getEventType().equalsIgnoreCase("School"))
//					.collect(Collectors.toList());
//		}
//
//		return events;
//	}
//
//	public List<Event> listAllOrganisationEvents(Long orgId) {
//		List<Event> events = listAllGlobalEventsOnly();
//
//		if (null != orgId && orgId > 0) {
//			List<Event> eventsOrg = listAllOrganisationEventsOnly(orgId);
//
//			events = this.union(events, eventsOrg);
//		}
//
//		return events;
//	}
//
//	public List<Event> listAllSchoolEvents(Long orgId, Long schoolId) {
//		List<Event> events = listAllOrganisationEvents(orgId);
//
//		if (null != schoolId && schoolId > 0) {
//			List<Event> eventsSchool = listAllSchoolEventsOnly(orgId, schoolId);
//
//			events = this.union(events, eventsSchool);
//		}
//
//		return events;
//	}
//
//	public void save(Event event) {
//		repo.save(event);
//	}
//
//	public Event get(Long id) {
//		return repo.findById(id).get();
//	}
//
//	public void delete(Long id) { repo.deleteById(id); }
//
//	public <T> List<T> union(List<T> list1, List<T> list2) {
//		Set<T> set = new HashSet<T>();
//
//		set.addAll(list1);
//		set.addAll(list2);
//
//		return new ArrayList<T>(set);
//	}
//
//	public List<Event> listAllGradeEvents(Long orgId, Long schoolId, Long gradeId) {
//		List<Event> events = listAllSchoolEvents(orgId, schoolId);
//
//		if (null != gradeId && gradeId > 0) {
//			List<Event> eventsGrade = listAllGradeEventsOnly(orgId, schoolId, gradeId);
//
//			events = this.union(events, eventsGrade);
//		}
//
//		return events;
//	}
//
//	private List<Event> listAllGradeEventsOnly(Long orgId, Long schoolId, Long gradeId) {
//		List<Event> events = listAll();
//		for (Event event:events
//		) {
//			System.out.println(event.getEventId() + ":" + event.getName() + ":" + event.getEventType());
//
//			if(event.getGrade() != null && event.getGrade().getGradeId().longValue() == gradeId.longValue()) {
//				System.out.println(event.getGrade().getGradeId());
//			}
//		}
//
//		if (null != gradeId && gradeId > 0) {
//			System.out.println(gradeId);
//
//			events = events.stream()
//					.filter(event -> event.getGrade() != null && event.getGrade().getGradeId().longValue() == gradeId.longValue())
//					.filter(event -> event.getEventType().equalsIgnoreCase("Grade"))
//					.collect(Collectors.toList());
//
//			for (Event event:events
//				 ) {
//				System.out.println(event.getName());
//			}
//		}
//
//		return events;
//	}
//
//	public List<Event> listAllSectionEvents(Long orgId, Long schoolId, Long gradeId, Long sectionId) {
//		List<Event> events = listAllGradeEvents(orgId, schoolId, gradeId);
//
//		if (null != sectionId && sectionId > 0) {
//			List<Event> eventsSection = listAllSectionEventsOnly(orgId, schoolId, gradeId, sectionId);
//
//			events = this.union(events, eventsSection);
//		}
//
//		return events;
//	}
//
//	private List<Event> listAllSectionEventsOnly(Long orgId, Long schoolId, Long gradeId, Long sectionId) {
//		List<Event> events = listAll();
//
//		if (null != sectionId && sectionId > 0) {
//			events = events.stream()
//					.filter(event -> event.getSection() != null && event.getSection().getSectionId() == sectionId)
//					.filter(event -> event.getEventType().equalsIgnoreCase("Section"))
//					.collect(Collectors.toList());
//		}
//
//		return events;
//	}
//
//	public List<Event> listAllStudentEvents(Long orgId, Long schoolId, Long gradeId, Long sectionId, Long studentId) {
//		List<Event> events = listAllSectionEvents(orgId, schoolId, gradeId, sectionId);
//
//		if (null != studentId && studentId > 0) {
//			List<Event> eventsStudent = listAllStudentEventsOnly(events, orgId, schoolId, gradeId, sectionId, studentId);
//
//			events = this.union(events, eventsStudent);
//		}
//
//		return events;
//	}
//
//	private List<Event> listAllStudentEventsOnly(List<Event> events, Long orgId, Long schoolId, Long gradeId, Long sectionId, Long studentId) {
//		return new ArrayList<Event>();
//	}
//}
