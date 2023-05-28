package org.nextlevel.student;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface StudentRepository extends JpaRepository<Student, Long> {
    String FILTER_BY_NAME_QUERY = "select b from Student b where UPPER(b.name) like CONCAT('%',UPPER(?1),'%')";

    @Query(FILTER_BY_NAME_QUERY)
    Page<Student> findByNameLike(String nameFilter, Pageable pageable);
}
