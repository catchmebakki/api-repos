package com.ssi.ms.collecticase.database.repository;

import com.ssi.ms.collecticase.database.dao.GTTForOrgLookupDAO;
import com.ssi.ms.collecticase.database.dao.GttForCaselookupDAO;
import com.ssi.ms.collecticase.dto.OrgLookupDTO;
import com.ssi.ms.collecticase.dto.VwCcaseCaseloadDTO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomLookupRepository {

    @Transactional
    List<GTTForOrgLookupDAO> processOrgLookupEmpQuery(OrgLookupDTO orgLookupDTO);

    @Transactional
    List<GTTForOrgLookupDAO> processOrgLookupOrgEmpQuery(OrgLookupDTO orgLookupDTO);

    @Transactional
    List<GTTForOrgLookupDAO> searchOrgLookup(String organisationName, String uiAcctNbr, String fein);

    List<VwCcaseCaseloadDTO> caseLoadByMetrics(Long staffId, Boolean newCase, Boolean highMediumPriority,
                                               Boolean overdue, Boolean bankruptcy);

    List<GttForCaselookupDAO> processCaseLookupQuery(String searchString);
}
