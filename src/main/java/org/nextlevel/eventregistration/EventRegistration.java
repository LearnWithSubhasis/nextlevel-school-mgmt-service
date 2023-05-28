package org.nextlevel.eventregistration;

import org.nextlevel.event.Event;
import org.nextlevel.student.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "events_registrations")
public class EventRegistration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "event_reg_id", nullable = false)
    private Long eventRegId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false, cascade = CascadeType.DETACH)
    //@JsonIgnore
    @JoinColumn(name = "event_id", nullable = false)
    private Event event;

    @Column(name="date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name="last_updated")
    @UpdateTimestamp
    private LocalDateTime lastUpdated;

//    @OneToOne(optional = true, fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
//    @JoinColumn(name = "student_id")
//    @JsonIgnore

    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name = "student_id")
    private Student student;

//    //@JsonIgnore
//    public Student getStudent() {
//        return student;
//    }



//    @ManyToOne(fetch = FetchType.EAGER, optional = true, cascade = CascadeType.ALL)
//    @JoinColumn(name = "student_id", nullable = false)
//    @JsonIgnore
//    private Student student;

//    @ManyToOne(optional = false,fetch = FetchType.LAZY)
//    @JsonIgnore
//    private User user;
//
//    @ManyToOne(optional = false, fetch = FetchType.LAZY)
//    @JsonIgnore
//    private Teacher teacher;

}
