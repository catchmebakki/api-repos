package com.ssi.ms.fraudreview.database.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.ssi.ms.fraudreview.database.dao.StaffStfDAO;


public interface StaffRepository extends CrudRepository<StaffStfDAO, Long> {

	@Query(value = "SELECT distinct stf.* FROM Staff_STF stf "
			+ "     JOIN USERS_USR usr ON stf.FK_USR_ID = usr.USR_ID "
			+ "     JOIN USR_ROL_URL url ON url.FK_USR_ID = stf.FK_USR_ID "
			+ "     WHERE url.FK_ROL_ID IN ( :roleIds ) "
			+ "     AND usr.USR_STATUS_CD = :userStatusCd ",
			  nativeQuery = true)
	List<StaffStfDAO> getBPCStaffByRoleId(List<Long> roleIds, Long userStatusCd);


	@Query(value = "SELECT stf.* "
			+ "     FROM Staff_STF stf "
			+ "     JOIN USERS_USR usr ON stf.FK_USR_ID = usr.USR_ID "
			+ "     JOIN USR_ROL_URL url ON url.FK_USR_ID = stf.FK_USR_ID "
			+ "     WHERE stf.FK_USR_ID = :userId "
			+ "     AND url.FK_ROL_ID =  :roleId "
			+ "     AND usr.USR_STATUS_CD = :userStatusCd ",
			nativeQuery = true)
	List<StaffStfDAO> getStaffByUserAndRoleId(Long userId, Long roleId, Long userStatusCd);

	StaffStfDAO findByFkUserId(Long fkUserId);
}