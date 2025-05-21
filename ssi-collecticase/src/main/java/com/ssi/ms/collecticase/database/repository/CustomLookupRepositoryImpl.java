package com.ssi.ms.collecticase.database.repository;


import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.GTTForOrgLookupDAO;
import com.ssi.ms.collecticase.database.dao.GttForCaselookupDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseCaseloadDAO;
import com.ssi.ms.collecticase.dto.OrgLookupDTO;
import com.ssi.ms.collecticase.dto.VwCcaseCaseloadDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.EMP_ADDRESS_CORPORATE;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.EMP_ADDRESS_MAILING;

@Repository
public class CustomLookupRepositoryImpl implements CustomLookupRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    VwCcaseCaseloadRepository vwCcaseCaseloadRepository;

    @Autowired
    GTTForOrgLookupRepository gttForOrgLookupRepository;

    @Transactional
    @Override
    public List<VwCcaseCaseloadDAO> processCaseLookupQuery(String searchString) {
        List<VwCcaseCaseloadDAO> vwCcaseCaseloadDAOList;
        List<GttForCaselookupDAO> gttForCaselookupDAOList;
        Query query = entityManager.createNativeQuery(searchString.substring(0, searchString.length() - 1).trim());
        query.executeUpdate();
        vwCcaseCaseloadDAOList = vwCcaseCaseloadRepository.getCaseLookupData();
        return vwCcaseCaseloadDAOList;
    }

    @Transactional
    @Override
    public List<GTTForOrgLookupDAO> processOrgLookupEmpQuery(OrgLookupDTO orgLookupDTO) {
        gttForOrgLookupRepository.processGTTLookupForEmployer(orgLookupDTO.getCaseId(), List.of(EMP_ADDRESS_CORPORATE,
                EMP_ADDRESS_MAILING), CollecticaseConstants.INDICATOR.Y.name());
        return searchOrgLookup(orgLookupDTO.getOrgName(), orgLookupDTO.getUiAcctNbr(), orgLookupDTO.getFein());
    }

    @Transactional
    @Override
    public List<GTTForOrgLookupDAO> processOrgLookupOrgEmpQuery(OrgLookupDTO orgLookupDTO) {
        gttForOrgLookupRepository.processGTTLookupForOrganisation(orgLookupDTO.getCaseId(),
                List.of(EMP_ADDRESS_CORPORATE, EMP_ADDRESS_MAILING),
                List.of(CollecticaseConstants.ENTITY_CONTACT_TYPE_ATTY,
                        CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_O), CollecticaseConstants.INDICATOR.Y.name());
        return searchOrgLookup(orgLookupDTO.getOrgName(), orgLookupDTO.getUiAcctNbr(), orgLookupDTO.getFein());
    }

    public List<GTTForOrgLookupDAO> searchOrgLookup(String organisationName, String uiAcctNbr, String fein) {
        List<GTTForOrgLookupDAO> gttForOrgLookupList = null;
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GTTForOrgLookupDAO> query = builder
                .createQuery(GTTForOrgLookupDAO.class);
        Root<GTTForOrgLookupDAO> r = query.from(GTTForOrgLookupDAO.class);
        List<Predicate> predicates = new ArrayList<Predicate>();

        if (StringUtils.isNotBlank(organisationName)) {
            predicates.add(
                    builder.or(
                            builder.like(builder.upper(r
                                            .<String>get(CollecticaseConstants.ENTITY_NAME)),
                                    CollecticaseConstants.LIKE_OPERATOR
                                            + organisationName
                                            .toUpperCase()
                                            + CollecticaseConstants.LIKE_OPERATOR),
                            builder.like(builder.upper(r
                                            .<String>get(CollecticaseConstants.ENTITY_DBA_NAME)),
                                    CollecticaseConstants.LIKE_OPERATOR
                                            + organisationName
                                            .toUpperCase()
                                            + CollecticaseConstants.LIKE_OPERATOR)

                    )
            );
        }

        if (StringUtils.isNotBlank(uiAcctNbr)) {
            predicates.add(builder.like(builder.upper(r
                            .<String>get(CollecticaseConstants.ENTITY_UI_ACC_NBR)),
                    CollecticaseConstants.LIKE_OPERATOR
                            + uiAcctNbr
                            .toUpperCase()
                            + CollecticaseConstants.LIKE_OPERATOR));
        }
        if (StringUtils.isNotBlank(fein)) {
            predicates.add(builder.like(builder.upper(r
                            .<String>get(CollecticaseConstants.ENTITY_FEIN_NBR)),
                    CollecticaseConstants.LIKE_OPERATOR
                            + fein
                            .toUpperCase()
                            + CollecticaseConstants.LIKE_OPERATOR));
        }
        query.select(r).where(predicates.toArray(new Predicate[]{}));
        query.orderBy(builder.asc(r.get(CollecticaseConstants.ENTITY_NAME)),
                builder.desc(r.get(CollecticaseConstants.ENTITY_SOURCE)));
        gttForOrgLookupList = entityManager.createQuery(query).getResultList();
        return gttForOrgLookupList;
    }

    public List<VwCcaseCaseloadDTO> caseLoadByMetrics(Long staffId, Boolean newCase, Boolean highMediumPriority,
                                                      Boolean overdue, Boolean bankruptcy) {
        StringBuilder sb = new StringBuilder();
        sb.append(""" 
                    SELECT new com.ssi.ms.collecticase.dto.VwCcaseCaseloadDTO(
                    CASE WHEN vwCcaseCaseload.bankrupt = 'Y' THEN 'B' END as claimantBankrupt,
                    CASE WHEN vwCcaseCaseload.bankrupt = 'Y' THEN 'Bankrupt' END as claimantBankruptDesc,
                    CASE WHEN vwCcaseCaseload.frd = 'Y' THEN 'F' END as claimantFraud,
                    CASE WHEN vwCcaseCaseload.frd = 'Y' THEN 'Fraud' END as claimantFraudDesc,
                    CASE WHEN vwCcaseCaseload.newCase = 'Y' THEN 'New' END as caseStatus,
                    vwCcaseCaseload.caseNo as caseNo,
                vwCcaseCaseload.claimantName || '-'|| cmtSsn as claimantName,
                vwCcaseCaseload.casePriorityDesc as casePriorityDesc,
                    vwCcaseCaseload.caseAge as caseAge,
                vwCcaseCaseload.mostRecentRemedy as mostRecentRemedy,
                    vwCcaseCaseload.caseCharacteristics as caseCharacteristics,
                vwCcaseCaseload.nextFollowupDate as nextFollowupRemedyWithDate,
                    CASE WHEN vwCcaseCaseload.frd = 'Y' THEN 'Fraud Only'
                WHEN vwCcaseCaseload.nfEarnings = 'Y' THEN 'Non-Fraud Earnings Only'
                WHEN vwCcaseCaseload.frdNfEarnings = 'Y' THEN 'Fraud and Non-Fraud Earnings Only'
                WHEN vwCcaseCaseload.nonFrd = 'Y' THEN 'Non-Fraud Only' END as claimantFraudStatus,
                    substr(vwCcaseCaseload.nextFollowupDate, 0, 10) as nextFollowupDate,
                substr(vwCcaseCaseload.nextFollowupDate, 12) as nextFollowupRemedy)
                from VwCcaseCaseloadDAO vwCcaseCaseload where
                """);
        sb.append(" vwCcaseCaseload.staffId = ");
        sb.append(staffId);
        if (newCase)
            sb.append(" and vwCcaseCaseload.newCase = 'Y' ");
        if (highMediumPriority)
            sb.append(" and vwCcaseCaseload.casePriority in (3926, 3929) ");
        if (overdue) sb.append(" and vwCcaseCaseload.overdue = 'Y' ");
        if (bankruptcy)
            sb.append(" and vwCcaseCaseload.bankrupt = 'Y' ");

        List<VwCcaseCaseloadDTO> vwCcaseCaseloadDTOList = entityManager.createQuery(sb.toString()).getResultList();
        return vwCcaseCaseloadDTOList;
    }
}