package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseOrganizationCmoDAO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CcaseOrganizationCmoRepository extends CrudRepository<CcaseOrganizationCmoDAO, Long> {

    @Query("""                
            from CcaseOrganizationCmoDAO ccaseOrganizationCmoDAO
                        where ccaseOrganizationCmoDAO.cmoId = :organisationId
            """)
    Long getOrganisationInfoById(Long organisationId);

}