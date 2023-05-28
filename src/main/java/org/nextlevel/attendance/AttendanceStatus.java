package org.nextlevel.attendance;

public enum AttendanceStatus {
    NA ("NA"),
    A ("Absent"),
    P ("Present"),
    H ("Holiday");

    private String value;
    AttendanceStatus(String value) {
        this.value = value;
    }
}
