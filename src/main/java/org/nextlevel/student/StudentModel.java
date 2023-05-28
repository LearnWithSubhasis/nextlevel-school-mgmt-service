package org.nextlevel.student;

import org.nextlevel.section.Section;
import org.springframework.hateoas.RepresentationModel;

public class StudentModel extends RepresentationModel<StudentModel> {
    private Long studentId;
    private String name;
    private Integer age;
    private String sex;
    private String religion;
    private String nationality;
    private String orphanStatus;
    private String socialNeeds;
    private String repeaters;
    private String parentName;
    private Integer roleNo;
    private Section section;
    //private List<Attendance> attendance;


    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getOrphanStatus() {
        return orphanStatus;
    }

    public void setOrphanStatus(String orphanStatus) {
        this.orphanStatus = orphanStatus;
    }

    public String getSocialNeeds() {
        return socialNeeds;
    }

    public void setSocialNeeds(String socialNeeds) {
        this.socialNeeds = socialNeeds;
    }

    public String getRepeaters() {
        return repeaters;
    }

    public void setRepeaters(String repeaters) {
        this.repeaters = repeaters;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getRoleNo() {
        return roleNo;
    }

    public void setRoleNo(Integer roleNo) {
        this.roleNo = roleNo;
    }

    public Section getSection() {
        return section;
    }

    public void setSection(Section section) {
        this.section = section;
    }

//    public List<Attendance> getAttendance() {
//        return attendance;
//    }
//
//    public void setAttendance(List<Attendance> attendance) {
//        this.attendance = attendance;
//    }
}
