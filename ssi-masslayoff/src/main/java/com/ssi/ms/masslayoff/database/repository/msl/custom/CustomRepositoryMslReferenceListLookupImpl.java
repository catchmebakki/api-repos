package com.ssi.ms.masslayoff.database.repository.msl.custom;

import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant;
import com.ssi.ms.masslayoff.dto.lookup.LookupItemResDTO;
import com.ssi.ms.masslayoff.dto.lookup.LookupListResDTO;
import com.ssi.ms.masslayoff.dto.lookup.LookupReqDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static com.ssi.ms.masslayoff.constant.PaginationAndSortByConstant.MASSLAYOFF_LOOKUP_SORTBY_FIELDMAPPING;
/**
 * @author Praveenraja Paramsivam
 * Implementation of the CustomRepositoryMslReferenceListLookup interface.
 * This class provides specialized methods for performing lookup operations on MSL reference lists.
 */
@Repository
public class CustomRepositoryMslReferenceListLookupImpl implements CustomRepositoryMslReferenceListLookup {

    @PersistenceContext
    private final EntityManager entityManager;
    /**
     * Calculates and returns the total item count based on the provided EntityManager and criteria string.
     *
     * @param entityManagerLocal {@link EntityManager} The EntityManager used to execute the query.
     * @param criteriaStr {@link String} The criteria string used to construct the query.
     * @return {@link Long} The total item count based on the provided criteria.
     */
    private final BiFunction<EntityManager, String, Long> getTotalItemCount = (entityManagerLocal, criteriaStr) ->
            Optional.of("""
                            SELECT count(*)
                            FROM MslRefListMlrlDAO MLRL
                            """ + criteriaStr)
                    .map(queryStr -> entityManagerLocal.createQuery(queryStr).getSingleResult())
                    .map(resultObj -> Long.parseLong(resultObj + ""))
                    .orElseGet(() -> null);
    private final Function<LookupReqDTO, String> buildCriteria = lookupReqDTO -> {
       final StringBuilder builder = new StringBuilder();
        if (lookupReqDTO.getMslId() > 0) {
            builder.append(" and MLRL.mlrlMslNum = ").append(lookupReqDTO.getMslId());
        }
        if (StringUtils.isNotBlank(lookupReqDTO.getEmpUiAccLoc())) {
            builder.append(" and MLRL.mlrlEmpAcLoc like '").append(lookupReqDTO.getEmpUiAccLoc()).append("'");
        }
        if (StringUtils.isNotBlank(lookupReqDTO.getEmpUiAcctNbr())) {
            builder.append(" and MLRL.mlrlEmpAcNum like '").append(lookupReqDTO.getEmpUiAcctNbr()).append("'");
        }
        if (null != lookupReqDTO.getLayoffDateRange()) {
            if (null != lookupReqDTO.getLayoffDateRange().getStartDate()) {
                builder.append(" and MLRL.mlrlMslDate >= TO_DATE ('").append(lookupReqDTO.getLayoffDateRange().getStartDate())
                        .append("', 'YYYY-MM-DD')");
            }
            if (null != lookupReqDTO.getLayoffDateRange().getEndDate()) {
                builder.append(" and MLRL.mlrlMslDate <= TO_DATE ('").append(lookupReqDTO.getLayoffDateRange().getEndDate())
                        .append("', 'YYYY-MM-DD')");
            }
        }
        if (null != lookupReqDTO.getRecallDateRange()) {
            if (null != lookupReqDTO.getRecallDateRange().getStartDate()) {
                builder.append(" and MLRL.mlrlRecallDate >= TO_DATE ('").append(lookupReqDTO.getRecallDateRange().getStartDate())
                        .append("', 'YYYY-MM-DD')");
            }
            if (null != lookupReqDTO.getRecallDateRange().getEndDate()) {
                builder.append(" and MLRL.mlrlRecallDate <= TO_DATE ('").append(lookupReqDTO.getRecallDateRange().getEndDate())
                        .append("', 'YYYY-MM-DD')");
            }
        }
        if (lookupReqDTO.isCurrentAndFutureLayoffInd()) {
            builder.append(" and (MLRL.mlrlMslDate >= TRUNC(SYSDATE) or MLRL.mlrlRecallDate >= TRUNC(SYSDATE)) ");
        }
        if (StringUtils.isNotBlank(lookupReqDTO.getClaimantSsn())) {
            builder.append("""
                            and  MLRL.mlrlId in (select mlec.mslRefListMlrlDAO.mlrlId
                                                   from MslEntryCmtMlecDAO mlec
                                                   where mlec.mlecSsn like '""")
                    .append(lookupReqDTO.getClaimantSsn())
                    .append("')");
        }
        if (lookupReqDTO.isPendingLayoffInd()) {
            builder.append(" and MLRL.mlrlStatusCd = " + AlvMassLayoffEnumConstant.MassLayoffStatusCd.PENDING.getCode() + " ");
        }
        return Optional.of(builder.toString())
                .filter(StringUtils::isNotBlank)
                .map(str -> str.replaceFirst("and", "where"))
                .orElseGet(() -> "");
    };
    /**
     * Supplier for retrieving the value case of MassLayoffStatusCd enum constants.
     * Uses a lambda expression to stream through the MassLayoffStatusCd enum values and return the values.
     */
    private final Supplier<String> getMlrlStatusCdValueCase = () -> Arrays.stream(AlvMassLayoffEnumConstant.MassLayoffStatusCd.values())
            .map(massLayoffStatusCd -> " WHEN MLRL.mlrlStatusCd = "
                    + massLayoffStatusCd.getCode() + " THEN '" + massLayoffStatusCd.name() + "'")
            .collect(Collectors.joining());
    /**
     * Custom implementation of the repository for MSL reference list lookup.
     * @param entityManager {@link EntityManager} The EntityManager to be used by the repository implementation.
     */
    @Autowired
    public CustomRepositoryMslReferenceListLookupImpl(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }
    /**
     * Filter the MSL reference list based on the provided lookup criteria.
     * @param lookupReqDTO {@link LookupReqDTO} The LookupReqDTO containing the lookup criteria.
     * @return {@link LookupListResDTO} The filtered LookupListResDTO containing the matching reference list items.
     */
    @Override
    public LookupListResDTO filterMslReferenceListBasedLookupCriteria(LookupReqDTO lookupReqDTO) {
        List<LookupItemResDTO> massLayoffList;
        final StringBuilder querySb = new StringBuilder();
        final String criteriaStr = buildCriteria.apply(lookupReqDTO);
        Long totalItemCount = null;

        if (lookupReqDTO.getPagination().getNeedTotalCount()) {
            totalItemCount = getTotalItemCount.apply(entityManager, criteriaStr);
        }

        querySb.append("""
                SELECT new com.ssi.ms.masslayoff.dto.lookup.LookupItemResDTO(
                    MLRL.mlrlId,
                    (
                        select max(musmDao.musmId)
                            from MslUploadSummaryMusmDAO musmDao
                            where musmDao.mslRefListMlrlDAO.mlrlId = MLRL.mlrlId
                    ) as musmId,
                    (
                        select musmDao2.musmNumErrs
                            from MslUploadSummaryMusmDAO musmDao2
                            where musmDao2.mslRefListMlrlDAO.mlrlId = MLRL.mlrlId
                                and musmDao2.musmId  =
                                    (select max(musmDao3.musmId)
                                        from MslUploadSummaryMusmDAO musmDao3
                                        where musmDao3.mslRefListMlrlDAO.mlrlId = MLRL.mlrlId)
                    ) as errorCount,
                    MLRL.mlrlMslNum ,
                    MLRL.mlrlEmpAcNum ,
                    MLRL.mlrlEmpAcLoc ,
                    TRUNC(MLRL.mlrlMslDate),
                    TRUNC(MLRL.mlrlRecallDate),
                    TRUNC(MLRL.mlrlMslEffDate),
                    MLRL.mlrlStatusCd,
                    CASE """
                            +  getMlrlStatusCdValueCase.get()
                    +   """
                       END AS mlrlStatusCdValue,
                    MLRL.mlrlDiInd,
                    (Select count(*) from MslEntryCmtMlecDAO mlec
                        where mlec.mslRefListMlrlDAO.mlrlId = MLRL.mlrlId) as noOfClaimants )
                FROM MslRefListMlrlDAO MLRL
                """);
        querySb.append(criteriaStr)
                .append(" order by ")
                .append(MASSLAYOFF_LOOKUP_SORTBY_FIELDMAPPING.get(lookupReqDTO.getSortBy().getField()))
                .append(" ")
                .append(StringUtils.isNotBlank(lookupReqDTO.getSortBy().getDirection())
                        ? lookupReqDTO.getSortBy().getDirection() : " ");


        massLayoffList = entityManager.createQuery(querySb.toString())
                .setFirstResult((lookupReqDTO.getPagination().getPageNumber() - 1)
                        * lookupReqDTO.getPagination().getPageSize())
                .setMaxResults(lookupReqDTO.getPagination().getPageSize())
                .getResultList();

        return LookupListResDTO.builder()
                .pagination(lookupReqDTO.getPagination().withTotalItemCount(totalItemCount))
                .sortBy(lookupReqDTO.getSortBy())
                .massLayoffList(massLayoffList).build();
    }

}
