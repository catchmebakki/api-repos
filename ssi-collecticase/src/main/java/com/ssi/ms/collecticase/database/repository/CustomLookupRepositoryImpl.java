package com.ssi.ms.collecticase.database.repository;


import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.GTTForOrgLookupDAO;
import com.ssi.ms.collecticase.database.dao.GttForCaselookupDAO;
import com.ssi.ms.collecticase.database.dao.VwCcaseCaseloadDAO;
import com.ssi.ms.collecticase.dto.OrgLookupDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.Predicate;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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
        gttForOrgLookupRepository.processGTTLookupForOrganisation(orgLookupDTO.getCaseId(), List.of(EMP_ADDRESS_CORPORATE,
                EMP_ADDRESS_MAILING), List.of(  CollecticaseConstants.ENTITY_CONTACT_TYPE_ATTY,
                CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_O), CollecticaseConstants.INDICATOR.Y.name());
        return searchOrgLookup(orgLookupDTO.getOrgName(), orgLookupDTO.getUiAcctNbr(), orgLookupDTO.getFein());
    }

    public List<GTTForOrgLookupDAO> searchOrgLookup(String organisationName, String uiAcctNbr, String fein)
    {
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
                                            .<String> get(CollecticaseConstants.ENTITY_NAME)),
                                    CollecticaseConstants.LIKE_OPERATOR
                                            + organisationName
                                            .toUpperCase()
                                            + CollecticaseConstants.LIKE_OPERATOR),
                            builder.like(builder.upper(r
                                            .<String> get(CollecticaseConstants.ENTITY_DBA_NAME)),
                                    CollecticaseConstants.LIKE_OPERATOR
                                            + organisationName
                                            .toUpperCase()
                                            + CollecticaseConstants.LIKE_OPERATOR)

                    )
            );
        }

        if (StringUtils.isNotBlank(uiAcctNbr)) {
            predicates.add(builder.like(builder.upper(r
                            .<String> get(CollecticaseConstants.ENTITY_UI_ACC_NBR)),
                    CollecticaseConstants.LIKE_OPERATOR
                            + uiAcctNbr
                            .toUpperCase()
                            + CollecticaseConstants.LIKE_OPERATOR));
        }
        if (StringUtils.isNotBlank(fein)) {
            predicates.add(builder.like(builder.upper(r
                            .<String> get(CollecticaseConstants.ENTITY_FEIN_NBR)),
                    CollecticaseConstants.LIKE_OPERATOR
                            + fein
                            .toUpperCase()
                            + CollecticaseConstants.LIKE_OPERATOR));
        }
        query.select(r).where(predicates.toArray(new Predicate[] {}));
        query.orderBy(builder.asc(r.get(CollecticaseConstants.ENTITY_NAME)),
                builder.desc(r.get(CollecticaseConstants.ENTITY_SOURCE)));
        gttForOrgLookupList = entityManager.createQuery(query).getResultList();
        return gttForOrgLookupList;
    }
}