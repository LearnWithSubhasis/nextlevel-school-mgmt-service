package org.nextlevel.teacher;

import org.nextlevel.school.School;

import javax.persistence.*;

@Entity
@Table(name = "principals")
public class Principal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="principal_id")
    private Long principalId;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "teacher_id")
    private Teacher teacher;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "school_id")
    private School school;

    public Principal() {
    }

    public Principal(Teacher teacher, School school) {
        this.teacher = teacher;
        this.school = school;
    }

    public Long getPrincipalId() {
        return principalId;
    }

    public void setPrincipalId(Long principalId) {
        this.principalId = principalId;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public School getSchool() {
        return school;
    }

    public void setSchool(School school) {
        this.school = school;
    }
}
