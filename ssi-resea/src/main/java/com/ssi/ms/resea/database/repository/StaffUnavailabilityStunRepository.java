package com.ssi.ms.resea.database.repository;

import com.ssi.ms.resea.database.dao.StaffUnavaiabilityStunDAO;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;

/**
 * {@code StaffUnavailabilityStunRepository} is a Spring Data repository that provides data access functionality
 * for the  {@code StaffUnavaiabilityStunDAO} entity.
 *
 * <p>
 * This repository is responsible for performing CRUD (Create, Read, Update, Delete) operations on
 * the STAFF_UNAVAILABILITY_STUN table in the database. It interacts with the underlying data source using
 * Spring Data JPA, leveraging predefined methods or custom query methods.
 * </p>
 *
 * @author Anand
 */

@Repository
public interface StaffUnavailabilityStunRepository extends CrudRepository<StaffUnavaiabilityStunDAO, Long> {
    @Transactional
    @Procedure(name = "applyUnavailibility")
    Map<String, Object> applyStaffUnavailibility(@Param("pin_boundary_date") Date boundaryDate,
                                                 @Param("pin_stun_id") Long stunId,
                                                 @Param("pin_calling_pgm") String callingProg);
}
