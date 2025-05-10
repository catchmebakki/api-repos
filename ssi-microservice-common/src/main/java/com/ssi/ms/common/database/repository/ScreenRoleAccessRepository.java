package com.ssi.ms.common.database.repository;

import com.ssi.ms.common.database.dao.ScreenRolesDao;
import com.ssi.ms.common.database.dao.UserDAO;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author munirathnam.surepall
 * This repository will provide roles based on screen
 */
@Repository
public interface ScreenRoleAccessRepository extends CrudRepository<ScreenRolesDao, Long> {

	/**
	 * @param screenId {@link Long} The ID of the user to search for
	 * @return {@link UserDAO} roll and access info
	 */
	List<ScreenRolesDao> findAllByScreenId(Long screenId);
}
