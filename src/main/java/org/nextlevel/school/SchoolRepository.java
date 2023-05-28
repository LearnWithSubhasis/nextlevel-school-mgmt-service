package org.nextlevel.school;

import org.nextlevel.grade.Grade;
import org.nextlevel.teacher.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SchoolRepository extends JpaRepository<School, Long> {
    @Query(value = "SELECT g.* FROM grades g, schools s where s.school_id = g.school_id and s.school_id = :id ", nativeQuery = true)
    List<Grade> listGrades(@Param("id") Long schoolId);

    @Query(value = "SELECT t.* FROM teachers t, schools s where s.school_id = t.school_id and s.school_id = :id ", nativeQuery = true)
    List<Teacher> listTeachers(@Param("id") Long schoolId);

    String FILTER_BY_NAME_QUERY = "select b from School b where UPPER(b.name) like CONCAT('%',UPPER(?1),'%')";

    @Query(FILTER_BY_NAME_QUERY)
    Page<School> findByNameLike(String nameFilter, Pageable pageable);

    String FILTER_BY_NAME_FOR_ORG_ID_QUERY = "select b from School b where org_id=?1 and UPPER(b.name) like CONCAT('%',UPPER(?2),'%')";

    @Query(FILTER_BY_NAME_FOR_ORG_ID_QUERY)
    Page<School> findByNameLikeForOrgId(Long orgId, String nameFilter, Pageable pageable);

    String FILTER_BY_SCHOOL_ADMIN_EMAIL_QUERY = "select * from schools b where UPPER(b.school_admin_email) = UPPER(?1) order by b.school_id LIMIT 1";
    @Query(value = FILTER_BY_SCHOOL_ADMIN_EMAIL_QUERY, nativeQuery = true)
    School findBySchoolAdminEmail(String schoolAdminEmail);

    //    @Query(value = "SELECT o.id FROM Organisations o where o.name = :name")
//    List<Long> findIdByName(@Param("name") String name);
}
