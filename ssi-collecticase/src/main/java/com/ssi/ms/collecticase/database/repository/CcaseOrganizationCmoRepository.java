package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.CcaseActivitiesCmaDAO;
import com.ssi.ms.collecticase.database.dao.CcaseOrganizationCmoDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CcaseOrganizationCmoRepository extends CrudRepository<CcaseOrganizationCmoDAO, Long> {


}
