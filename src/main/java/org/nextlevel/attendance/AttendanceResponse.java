package org.nextlevel.attendance;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AttendanceResponse {
    private List<Attendance> failedEntries;

    public List<Attendance> getFailedEntries() {
        return failedEntries;
    }

    public void setFailedEntries(List<Attendance> failedEntries) {
        this.failedEntries = failedEntries;
    }
}
