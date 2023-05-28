package org.nextlevel.event;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Date;

@RestController
public class EventController2 {

    @Autowired
    EventRepository er;

    @RequestMapping("/api")
    @ResponseBody
    String home() {
        return "Welcome!";
    }

    @GetMapping("/api/events")
    @JsonSerialize(using = DateSerializer.class)
    Iterable<Event> events(@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date start, @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Date end) {
        return er.findBetween(start, end);
    }

    @PostMapping("/api/events/create")
    @JsonSerialize(using = DateSerializer.class)
    @Transactional
    Event createEvent(@RequestBody EventCreateParams params) {

        Event e = new Event();
        e.setEventStartTime(new Date());
        e.setEventEndTime(new Date());
        e.setName(params.text);

        er.save(e);

        return e;
    }

    @PostMapping("/api/events/move")
    @JsonSerialize(using = DateSerializer.class)
    @Transactional
    Event moveEvent(@RequestBody EventMoveParams params) {

        Event e = er.findById(params.id).get();

        e.setEventStartTime(new Date());
        e.setEventEndTime(new Date());

        er.save(e);

        return e;
    }

    @PostMapping("/api/events/setColor")
    @JsonSerialize(using = DateSerializer.class)
    @Transactional
    Event setColor(@RequestBody SetColorParams params) {

        Event e = er.findById(params.id).get();
        e.setColor(params.color);
        er.save(e);

        return e;
    }

    public static class EventCreateParams {
        public LocalDateTime start;
        public LocalDateTime end;
        public String text;
        public Long resource;
    }

    public static class EventMoveParams {
        public Long id;
        public LocalDateTime start;
        public LocalDateTime end;
        public Long resource;
    }

    public static class SetColorParams {
        public Long id;
        public String color;
    }


}
