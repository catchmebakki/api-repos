package com.ssi.ms.configuration.database.repository.custom;

import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.dto.parameter.ConfigParListItemResDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParListReqDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParListResDTO;
import com.ssi.ms.platform.dto.PaginationDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.PARAMETER_SORTBY_FIELDMAPPING;
import static com.ssi.ms.configuration.util.ConfigUtilFunction.IS_CONFIG_ACTIVE;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

/**
 * @author Praveenraja Paramsivam
 * Custom repository interface for performing specialized lookup operations on MSL reference lists.
 * This interface defines additional methods to retrieve specific data from the MSL reference lists.
 */
public class CustomParameterParRepositoryImpl implements CustomParameterParRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String INACTIVE_QUERY = """
	SELECT PAR_ID, PAR_NAME, PAR_SHORT_NAME, PAR_NUMERIC_VALUE, PAR_ALPHA_VAL_TXT, PAR_EFFECTIVE_DT, PAR_EXPIRATION_DT,
		(SELECT (COUNT (*) - 1) FROM PARAMETER_PAR WHERE PAR_SHORT_NAME = par.PAR_SHORT_NAME) AS CHILD_LIST,
		(SELECT COUNT (1) FROM PARAMETER_PAR WHERE PAR_SHORT_NAME = par.PAR_SHORT_NAME AND PAR_EXPIRATION_DT IS NULL)
			AS OPENENDED_EXISTS,
		(SELECT COUNT (*) FROM PARAMETER_PAR rPar WHERE rPar.PAR_SHORT_NAME = par.PAR_SHORT_NAME
			AND TRUNC (SYSDATE) BETWEEN rPar.PAR_EFFECTIVE_DT AND COALESCE (rPar.PAR_EXPIRATION_DT, TRUNC (SYSDATE)))
			AS REINSTATE
	FROM (
		SELECT par.*, RANK () OVER (PARTITION BY PAR_SHORT_NAME ORDER BY PAR_EFFECTIVE_DT DESC, PAR_ID DESC) rnk
		FROM PARAMETER_PAR par
			WHERE par.PAR_CATEGORY_CD IN ( CAT_CD_VAL ) AND UPPER(par.PAR_NAME) like '%BY_NAME_VAL%'
			AND (TRUNC (SYSDATE) > par.PAR_EXPIRATION_DT OR TRUNC (SYSDATE) < par.PAR_EFFECTIVE_DT)
	) par WHERE rnk = 1
	""";

	private static final String ALL_QUERY = """
	SELECT * FROM (
		SELECT PAR_ID, PAR_NAME, PAR_SHORT_NAME, PAR_NUMERIC_VALUE, PAR_ALPHA_VAL_TXT, PAR_EFFECTIVE_DT, PAR_EXPIRATION_DT,
			(SELECT (COUNT(*) - 1) FROM PARAMETER_PAR WHERE PAR_SHORT_NAME = par.PAR_SHORT_NAME) AS CHILD_LIST,
			(SELECT COUNT (1) FROM PARAMETER_PAR WHERE PAR_SHORT_NAME = par.PAR_SHORT_NAME AND PAR_EXPIRATION_DT IS NULL)
				AS OPENENDED_EXISTS,
			1 AS REINSTATE
	    FROM PARAMETER_PAR par WHERE par.PAR_CATEGORY_CD IN ( CAT_CD_VAL ) AND UPPER(par.PAR_NAME) like '%%BY_NAME_VAL%'
	    AND TRUNC (SYSDATE) BETWEEN PAR_EFFECTIVE_DT AND COALESCE (PAR_EXPIRATION_DT, TRUNC (SYSDATE))
		UNION
		SELECT PAR_ID, PAR_NAME, PAR_SHORT_NAME, PAR_NUMERIC_VALUE, PAR_ALPHA_VAL_TXT, PAR_EFFECTIVE_DT, PAR_EXPIRATION_DT,
			(SELECT (COUNT (*) - 1) FROM PARAMETER_PAR WHERE PAR_SHORT_NAME = par.PAR_SHORT_NAME) AS CHILD_LIST,
			(SELECT COUNT (1) FROM PARAMETER_PAR WHERE PAR_SHORT_NAME = par.PAR_SHORT_NAME AND PAR_EXPIRATION_DT IS NULL)
				AS OPENENDED_EXISTS,
			(SELECT COUNT (*) FROM PARAMETER_PAR rPar WHERE rPar.PAR_SHORT_NAME = par.PAR_SHORT_NAME
				AND TRUNC (SYSDATE) BETWEEN rPar.PAR_EFFECTIVE_DT AND COALESCE(rPar.PAR_EXPIRATION_DT, TRUNC (SYSDATE)))
				AS REINSTATE
	    FROM (
			SELECT par.*, RANK () OVER (PARTITION BY PAR_SHORT_NAME ORDER BY PAR_EFFECTIVE_DT DESC, PAR_ID DESC) rnk
			FROM PARAMETER_PAR par
				WHERE par.PAR_CATEGORY_CD IN ( CAT_CD_VAL ) AND UPPER(par.PAR_NAME) like '%BY_NAME_VAL%'
				AND (TRUNC(SYSDATE) > par.PAR_EXPIRATION_DT OR TRUNC (SYSDATE) < par.PAR_EFFECTIVE_DT)
				AND NOT EXISTS (SELECT 1 FROM PARAMETER_PAR aPar WHERE aPar.PAR_SHORT_NAME = par.PAR_SHORT_NAME
					AND TRUNC (SYSDATE) BETWEEN aPar.PAR_EFFECTIVE_DT AND COALESCE (aPar.PAR_EXPIRATION_DT, TRUNC (SYSDATE)))
	    ) par WHERE rnk = 1
	)
	""";

	private final Function<String, Long> getTotalItemCount = (criteriaStr) ->
			Optional.of("""
                            SELECT count(distinct PAR_SHORT_NAME)
                            FROM PARAMETER_PAR par
                            """ + criteriaStr)
					.map(queryStr -> jdbcTemplate.queryForObject(queryStr, Long.class))
					.orElseGet(() -> null);

	private final Function<PaginationDTO, String> buildPagination = paginationDTO -> {
		String paginate = "";
		if (paginationDTO != null) {
			paginate = " OFFSET " + (paginationDTO.getPageNumber() - 1) * paginationDTO.getPageSize()
					+ " ROWS FETCH NEXT " + paginationDTO.getPageSize() + " ROWS ONLY ";
		}
        return paginate;
    };

	@Override
	public ConfigParListResDTO filterParameterBasedLookupCriteria(ConfigParListReqDTO parReqDTO, Date systemDate) {
		List<ConfigParListItemResDTO> parameterParList;
		final StringBuilder querySb = new StringBuilder();
		String parCategoryCdStr = parReqDTO.getParCategoryCd().toString();
		parCategoryCdStr = parCategoryCdStr.substring(1, parCategoryCdStr.length() - 1);
		final String byName = StringUtils.trimToEmpty(parReqDTO.getByName()).toUpperCase();
		Long totalItemCount = 0L;
		if (parReqDTO.getPagination() != null && parReqDTO.getPagination().getNeedTotalCount()) {
			totalItemCount = getTotalItemCount.apply(
					"WHERE par.PAR_CATEGORY_CD in ( " + parCategoryCdStr + " ) AND UPPER(par.PAR_NAME) like '%" + byName
					+ (ConfigurationConstants.ACTIVE.ALL.name().equals(parReqDTO.getActive()) ? "%'"
							: "%' and (TRUNC(sysdate) > par.PAR_EXPIRATION_DT OR TRUNC(sysdate) < par.PAR_EFFECTIVE_DT)")
			);
		}

		if (ConfigurationConstants.ACTIVE.N.name().equals(parReqDTO.getActive())) {
			querySb.append(INACTIVE_QUERY.replace("CAT_CD_VAL", parCategoryCdStr)
					.replace("BY_NAME_VAL", StringUtils.trimToEmpty(parReqDTO.getByName()).toUpperCase()));
		} else {
			querySb.append(ALL_QUERY.replaceAll("CAT_CD_VAL", parCategoryCdStr)
					.replaceAll("BY_NAME_VAL", StringUtils.trimToEmpty(parReqDTO.getByName()).toUpperCase()));
		}
		querySb.append(" order by ")
				.append(PARAMETER_SORTBY_FIELDMAPPING.getOrDefault(parReqDTO.getSortBy().getField(), "PAR_NAME"))
				.append(" ")
				.append(StringUtils.trimToEmpty(parReqDTO.getSortBy().getDirection()))
				.append(buildPagination.apply(parReqDTO.getPagination()));

		parameterParList = jdbcTemplate.query(querySb.toString(),
				(rs, rowNum) -> new ConfigParListItemResDTO(rs.getLong("PAR_ID"),
						rs.getString("PAR_NAME"), rs.getString("PAR_SHORT_NAME"),
						rs.getBigDecimal("PAR_NUMERIC_VALUE"), rs.getString("PAR_ALPHA_VAL_TXT"),
						rs.getDate("PAR_EFFECTIVE_DT"), rs.getDate("PAR_EXPIRATION_DT"),
						rs.getInt("CHILD_LIST"), rs.getInt("REINSTATE"))).stream()
				.map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
				.map(dto -> dto.withDeleteFlag(checkFutureDate.test(localDateToDate.apply(dto.getStartDate()), systemDate)))
				.toList();


		return ConfigParListResDTO.builder()
				.pagination(parReqDTO.getPagination() != null ? parReqDTO.getPagination().withTotalItemCount(totalItemCount) : null)
				.sortBy(parReqDTO.getSortBy())
				.parameterParList(parameterParList).build();
	}
}
