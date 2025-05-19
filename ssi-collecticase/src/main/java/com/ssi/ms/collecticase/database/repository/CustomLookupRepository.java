package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.GTTForOrgLookupDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseCaseloadDAO;
import com.ssi.ms.collecticase.dto.OrgLookupDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomLookupRepository {

    List<VwCcaseCaseloadDAO> processCaseLookupQuery(String searchString);

    @Transactional
    List<GTTForOrgLookupDAO> processOrgLookupEmpQuery(OrgLookupDTO orgLookupDTO);

    @Transactional
    List<GTTForOrgLookupDAO> processOrgLookupOrgEmpQuery(OrgLookupDTO orgLookupDTO);

    @Transactional
    List<GTTForOrgLookupDAO> searchOrgLookup(String organisationName, String uiAcctNbr, String fein);
}
