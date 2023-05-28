package org.nextlevel.eventregistration;

import org.nextlevel.event.EventService;
import org.nextlevel.student.Student;
import org.nextlevel.student.StudentService;
import org.nextlevel.teacher.TeacherService;
import org.nextlevel.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class EventRegistrationController {
    @Autowired
    private EventService eventService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private UserService userService;
    @Autowired
    private TeacherService teacherService;
    @Autowired
    private EventRegistrationRepository eventRegistrationRepo;
    @Autowired
    private EventRegistrationService eventRegistrationService;

    public static final Logger LOG = LoggerFactory.getLogger(EventRegistrationController.class);

    @PostMapping("/api/v1/events/{id}/registrations")
    public ResponseEntity<EventRegistration> save (@RequestBody EventRegistration eventRegistration) {
        if(eventRegistration.getStudent() != null) {
            Student student = studentService.get(eventRegistration.getStudent().getStudentId());
            if(null == student) {
                LOG.error("Student doesn't exist.");
                return ResponseEntity.notFound().build();
            }
            //TODO::Otherwise you will get - persist error to a detached entity
            eventRegistration.setStudent(student);
        }

        LocalDateTime timeNow = LocalDateTime.now();
        eventRegistration.setDateCreated(timeNow);
        eventRegistration.setLastUpdated(timeNow);

        return ResponseEntity.ok(eventRegistrationRepo.save(eventRegistration));
    }

    @GetMapping("/api/v1/events/{eventId}/registrations")
    public ResponseEntity<List<EventRegistration>> getRegistrations (@PathVariable Long eventId) {
        return ResponseEntity.ok(eventRegistrationService.findAllForEvent(eventId));
    }
}


