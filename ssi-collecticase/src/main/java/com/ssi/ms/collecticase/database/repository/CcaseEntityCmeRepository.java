package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseActivitiesCmaDAO;
import com.ssi.ms.collecticase.database.dao.CcaseEntityCmeDAO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CcaseEntityCmeRepository extends CrudRepository<CcaseEntityCmeDAO, Long> {

}
