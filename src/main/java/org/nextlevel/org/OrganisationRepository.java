package org.nextlevel.org;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrganisationRepository extends JpaRepository<Organisation, Long> {
    String FILTER_BY_NAME_QUERY = "select b from Organisation b where UPPER(b.name) like CONCAT('%',UPPER(?1),'%')";
    @Query(FILTER_BY_NAME_QUERY)
    Page<Organisation> findByNameLike(String nameFilter, Pageable pageable);

    String FILTER_BY_ORG_ADMIN_EMAIL_QUERY = "select * from organisations b where UPPER(b.org_admin_email) = UPPER(?1) order by b.org_id LIMIT 1";
    @Query(value = FILTER_BY_ORG_ADMIN_EMAIL_QUERY, nativeQuery = true)
    Organisation findByOrgAdminEmail(String orgAdminEmail);

//    @Query(value = "select count(s) from school s where s.org_id = :")

//    @Query(value = "SELECT o.id FROM Organisations o where o.name = :name")
//    List<Long> findIdByName(@Param("name") String name);

//    @Query(value = "SELECT o.* FROM organisations o", nativeQuery = true)
//    List<Organisation> findAll();

//    @Query(value = "SELECT o.* FROM Organisations o where o.id = :id", nativeQuery = true)
//    Optional<Organisation> findById(@Param("id") Long id);
}
