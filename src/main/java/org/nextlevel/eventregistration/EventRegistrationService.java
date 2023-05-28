package org.nextlevel.eventregistration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventRegistrationService {
    @Autowired
    private EventRegistrationRepository eventRegistrationRepo;

    public List<EventRegistration> findAllForEvent(Long eventId) {
        List<EventRegistration> eventRegistrations = eventRegistrationRepo.findAll()
                .stream()
                .filter(eventRegistration -> eventRegistration.getEvent().getEventId().longValue() == eventId.longValue())
                .collect(Collectors.toList());
        return eventRegistrations;
    }
}
