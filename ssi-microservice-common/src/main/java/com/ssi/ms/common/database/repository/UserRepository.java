package com.ssi.ms.common.database.repository;


import com.ssi.ms.common.database.dao.UserDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Praveenraja Paramsivam
 *Repository interface for managing UserDAO entities.
 */
@Repository
public interface UserRepository extends CrudRepository<UserDAO, Long> {
	/**
	 * Retrieve a UserDAO instance based on the provided user ID.
	 *
	 * @param userId {@link Long} The ID of the user to search for.
	 * @return {@link UserDAO} The retrieved UserDAO instance matching the provided user ID.
	 */
    UserDAO findByUserId(Long userId);
}
