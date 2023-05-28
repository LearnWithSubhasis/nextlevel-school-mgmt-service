package org.nextlevel.event;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("from Event e where not(e.eventEndTime < :from or e.eventStartTime > :to)")
    public List<Event> findBetween(@Param("from") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date start, @Param("to") @DateTimeFormat(iso= DateTimeFormat.ISO.DATE_TIME) Date end);

}
