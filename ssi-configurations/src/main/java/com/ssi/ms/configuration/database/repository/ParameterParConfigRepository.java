package com.ssi.ms.configuration.database.repository;

import com.ssi.ms.configuration.database.dao.ParameterParDAO;
import com.ssi.ms.configuration.database.repository.custom.CustomParameterParRepository;
import com.ssi.ms.configuration.dto.parameter.ConfigParListItemResDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ParameterParConfigRepository extends CrudRepository<ParameterParDAO, Long>,
        JpaRepository<ParameterParDAO, Long>, CustomParameterParRepository {

    @Query("""
            from ParameterParDAO par where par.parCategoryCdAlvDAO.alvId in ( :parCategoryCd )
            and UPPER(par.parName) LIKE UPPER(CONCAT('%',:parName,'%'))
            and trunc(sysdate) between par.parEffectiveDate and COALESCE(par.parExpirationDate, TRUNC (SYSDATE))
            """)
    Page<ParameterParDAO> findActiveByParNameParCategoryCD(@Param("parCategoryCd") List<Long> parCategoryCd,
                                                           @Param("parName") String parName, Pageable pageable);

    @Query("""
            from ParameterParDAO par where par.parCategoryCdAlvDAO.alvId in ( :parCategoryCd )
            and par.parShortName = :parShortName and par.parExpirationDate = :parExpirationDate order by parEffectiveDate asc
            """)
    List<ParameterParDAO> findByParShortNameParCategoryCDParExpDate(@Param("parCategoryCd") Long parCategoryCd,
                                                                    @Param("parShortName") String parShortName,
                                                                    @Param("parExpirationDate") Date parExpirationDate);

    @Query("""
            select new com.ssi.ms.configuration.dto.parameter.ConfigParListItemResDTO(parId, parName, parShortName,
                parNumericValue, parAlphaValText, parEffectiveDate, parExpirationDate)
            from ParameterParDAO par where parId != :parId
            and par.parShortName in (
                select parShortName from ParameterParDAO where parId = :parId
            )
            and par.parCategoryCdAlvDAO.alvId in (
                select par.parCategoryCdAlvDAO.alvId from ParameterParDAO where parId = :parId
            ) order by par.parEffectiveDate desc
            """)
    List<ConfigParListItemResDTO> findChildListByParId(@Param("parId") Long parId);

    @Query("""
            select new com.ssi.ms.configuration.dto.parameter.ConfigParListItemResDTO(parId, parName, parShortName,
                parNumericValue, parAlphaValText, parEffectiveDate, parExpirationDate)
            from ParameterParDAO par where parId != :parId
            and par.parShortName in (
                select parShortName from ParameterParDAO where parId = :parId
            )
            and par.parCategoryCdAlvDAO.alvId in (
                select par.parCategoryCdAlvDAO.alvId from ParameterParDAO where parId = :parId
            )
            and trunc(sysdate) between par.parEffectiveDate and COALESCE(par.parExpirationDate, TRUNC (SYSDATE))
            order by par.parEffectiveDate desc
            """)
    List<ConfigParListItemResDTO> findActiveChildListByParId(@Param("parId") Long parId);

    @Query("""
            select new com.ssi.ms.configuration.dto.parameter.ConfigParListItemResDTO(parId, parName, parShortName,
                parNumericValue, parAlphaValText, parEffectiveDate, parExpirationDate)
            from ParameterParDAO par where parId != :parId
            and par.parShortName in (
                select parShortName from ParameterParDAO where parId = :parId
            )
            and par.parCategoryCdAlvDAO.alvId in (
                select par.parCategoryCdAlvDAO.alvId from ParameterParDAO where parId = :parId
            )
            AND (TRUNC (SYSDATE) > par.parExpirationDate OR TRUNC (SYSDATE) < par.parEffectiveDate)
            order by par.parEffectiveDate desc
            """)
    List<ConfigParListItemResDTO> findInactiveChildListByParId(@Param("parId") Long parId);

    @Query("""
            SELECT DISTINCT par.parName FROM ParameterParDAO par WHERE par.parCategoryCdAlvDAO.alvId = :parCategoryCd
                AND par.parName is not null ORDER BY par.parName ASC
            """)
    List<String> getParNameListByCategory(@Param("parCategoryCd") Long parCategoryCd);

    @Query("""
            FROM ParameterParDAO par WHERE par.parShortName = :parShortName and par.parCategoryCdAlvDAO.alvId = :parCategoryCd
            AND TRUNC(SYSDATE) BETWEEN par.parEffectiveDate AND COALESCE(par.parExpirationDate, TRUNC(SYSDATE))
            """)
    ParameterParDAO findActiveParameterByShortName(@Param("parShortName") String parShortName,
                                                   @Param("parCategoryCd") Long parCategoryCd);

    @Query("""
            FROM ParameterParDAO par WHERE par.parShortName = :parShortName and par.parCategoryCdAlvDAO.alvId = :parCategoryCd
            AND :effectiveUntilDate > par.parEffectiveDate AND (par.parExpirationDate is null or :effectiveUntilDate <= par.parExpirationDate)
            """)
    ParameterParDAO findEffActiveParameterByShortName(@Param("parShortName") String parShortName,
                                                      @Param("parCategoryCd") Long parCategoryCd,
                                                      @Param("effectiveUntilDate") Date effectiveUntilDate);

    @Query("""
            FROM ParameterParDAO par WHERE par.parShortName = :parShortName and par.parCategoryCdAlvDAO.alvId = :parCategoryCd
            AND :startDate <= par.parEffectiveDate AND (par.parExpirationDate <= coalesce( :endDate, par.parExpirationDate)
                OR (par.parExpirationDate is null and :endDate is null))
            """)
    List<ParameterParDAO> findEffActiveParameterByShortName(@Param("parShortName") String parShortName,
                                                      @Param("parCategoryCd") Long parCategoryCd,
                                                      @Param("startDate") Date startDate,
                                                      @Param("endDate") Date endDate);

    @Query("""
            FROM ParameterParDAO par WHERE par.parShortName = :parShortName and par.parCategoryCdAlvDAO.alvId = :parCategoryCd
            AND par.parExpirationDate is NULL
            """)
    ParameterParDAO findOpenEndedParameterByShortName(@Param("parShortName") String parShortName,
                                                      @Param("parCategoryCd") Long parCategoryCd);

    @Query("""
            FROM ParameterParDAO par WHERE par.parShortName = :parShortName and par.parCategoryCdAlvDAO.alvId = :parCategoryCd
                AND (par.parExpirationDate is null or par.parExpirationDate > trunc(sysdate))
            """)
    List<ParameterParDAO> findActiveParametersByShortName(@Param("parShortName") String parShortName,
                                                          @Param("parCategoryCd") Long parCategoryCd);
}
