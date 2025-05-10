package com.ssi.ms.common.database.repository;

import com.ssi.ms.common.database.dao.NhuisLogNhlDao;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Praveenraja Paramsivam
 *Repository interface for managing NhuisLogNhlDao entities.
 */
@Repository
public interface NhuisLogNhlRepository extends CrudRepository<NhuisLogNhlDao, Long> {
}
