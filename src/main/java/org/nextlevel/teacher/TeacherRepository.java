package org.nextlevel.teacher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    @Query(value = "SELECT t.* FROM teachers t, users u where t.user_id = u.user_id and u.user_id = :userId ", nativeQuery = true)
    public Teacher findByUserId(Long userId);
}
