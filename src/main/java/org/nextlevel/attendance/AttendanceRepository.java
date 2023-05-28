package org.nextlevel.attendance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    String FILTER_ATTENDANCE_ON_TERM_QUERY = "select b from Attendance b where UPPER(b.term) like CONCAT('%',UPPER(?1),'%')";
    String FILTER_ATTENDANCE_BY_STUDENT_ID_QUERY = "select b from Attendance b where b.student.studentId = ?1";

    @Query(FILTER_ATTENDANCE_ON_TERM_QUERY)
    Page<Attendance> findByTermLike(String termFilter, Pageable pageable);

    @Query(FILTER_ATTENDANCE_BY_STUDENT_ID_QUERY)
    Page<Attendance> findAttendanceByStudentID(Long studentID, Pageable pageable);
}
