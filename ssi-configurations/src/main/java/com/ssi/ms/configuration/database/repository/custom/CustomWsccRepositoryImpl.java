package com.ssi.ms.configuration.database.repository.custom;

import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListItemResDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListReqDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListResDTO;
import com.ssi.ms.platform.dto.PaginationDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.WSWCC_SORTBY_FIELDMAPPING;
import static com.ssi.ms.configuration.util.ConfigUtilFunction.IS_CONFIG_ACTIVE;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

public class CustomWsccRepositoryImpl implements CustomWsccRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String INACTIVE_QUERY = """
	SELECT WSCC_ID, ALV_SHORT_DESC_TXT AS WSCC_PROGRAM_DESC, WSCC_MIN_IC_WS_REQ, WSCC_MIN_AC_WS_REQ,
	WSCC_MIN_WS_INCR_FREQ, WSCC_MIN_WS_INCR_VAL, WSCC_EFFECTIVE_DT, WSCC_EXPIRATION_DT,
		(SELECT (COUNT (*) - 1) FROM WS_CPS_CONFIG_WSCC WHERE FK_CPS_ID=a.FK_CPS_ID
		AND (TRUNC(sysdate) > WSCC_EXPIRATION_DT OR TRUNC(sysdate) < WSCC_EFFECTIVE_DT)) AS CHILD_LIST
	FROM (
		SELECT wscc.*, RANK () OVER (
			PARTITION BY FK_CPS_ID ORDER BY WSCC_EFFECTIVE_DT DESC, WSCC_ID DESC) rnk
		FROM WS_CPS_CONFIG_WSCC wscc WHERE
			(TRUNC(sysdate) > wscc.WSCC_EXPIRATION_DT OR TRUNC(sysdate) < wscc.WSCC_EFFECTIVE_DT)
	) a
	JOIN CLM_PROG_SPEC_CPS cps on a.FK_CPS_ID=cps.CPS_ID
	JOIN ALLOW_VAL_ALV alv ON alv.ALV_ID=cps.CPS_PROGRAM_CD
	WHERE a.rnk=1
			         		""";

	private static final String ALL_QUERY = """
	SELECT WSCC_ID, ALV_SHORT_DESC_TXT AS WSCC_PROGRAM_DESC, WSCC_MIN_IC_WS_REQ, WSCC_MIN_AC_WS_REQ, WSCC_MIN_WS_INCR_FREQ,
	WSCC_MIN_WS_INCR_VAL, WSCC_EFFECTIVE_DT, WSCC_EXPIRATION_DT,
			(SELECT (COUNT (*) - 1) FROM WS_CPS_CONFIG_WSCC WHERE FK_CPS_ID=a.FK_CPS_ID) AS CHILD_LIST
		FROM (
		   SELECT WSCC_ID, FK_CPS_ID, WSCC_MIN_IC_WS_REQ, WSCC_MIN_AC_WS_REQ, WSCC_MIN_WS_INCR_FREQ, WSCC_MIN_WS_INCR_VAL,
		   WSCC_EFFECTIVE_DT, WSCC_EXPIRATION_DT
		   FROM WS_CPS_CONFIG_WSCC
			   WHERE TRUNC(SYSDATE) BETWEEN WSCC_EFFECTIVE_DT AND COALESCE(WSCC_EXPIRATION_DT, TRUNC(SYSDATE))
		   UNION
		   SELECT WSCC_ID, FK_CPS_ID, WSCC_MIN_IC_WS_REQ, WSCC_MIN_AC_WS_REQ, WSCC_MIN_WS_INCR_FREQ, WSCC_MIN_WS_INCR_VAL,
		   WSCC_EFFECTIVE_DT, WSCC_EXPIRATION_DT
		   FROM (
			   SELECT wscc.*, RANK () OVER (PARTITION BY FK_CPS_ID ORDER BY WSCC_EFFECTIVE_DT DESC, WSCC_ID DESC) rnk
			   FROM WS_CPS_CONFIG_WSCC wscc WHERE
			   NOT EXISTS(SELECT 1 FROM WS_CPS_CONFIG_WSCC wscc1
				   WHERE wscc.FK_CPS_ID=wscc1.FK_CPS_ID
				   AND TRUNC(SYSDATE) BETWEEN WSCC_EFFECTIVE_DT AND COALESCE(WSCC_EXPIRATION_DT, TRUNC(SYSDATE)))
		   ) WHERE rnk=1
	   ) a
	   JOIN CLM_PROG_SPEC_CPS cps on a.FK_CPS_ID=cps.CPS_ID
	   JOIN ALLOW_VAL_ALV alv ON alv.ALV_ID=cps.CPS_PROGRAM_CD
			""";

	private final Function<String, Long> getTotalItemCount = (criteriaStr) ->
			Optional.of("""
                            SELECT count(distinct FK_CPS_ID)
                            FROM WS_CPS_CONFIG_WSCC wswc
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
	public ConfigWsccListResDTO filterWsccBasedLookupCriteria(ConfigWsccListReqDTO wswcReqDTO, Date systemDate) {
		List<ConfigWsccListItemResDTO> wsccItemParList;
		final StringBuilder querySb = new StringBuilder();
		Long totalItemCount = 0L;
		if (wswcReqDTO.getPagination() != null && wswcReqDTO.getPagination().getNeedTotalCount()) {
			totalItemCount = getTotalItemCount.apply(
					"WHERE "
					+ (ConfigurationConstants.ACTIVE.ALL.name().equals(wswcReqDTO.getActive()) ? "1=1"
							: " (TRUNC(sysdate) > wswc.WSCC_EXPIRATION_DT OR TRUNC(sysdate) < wswc.WSCC_EFFECTIVE_DT)")
			);
		}

		if (ConfigurationConstants.ACTIVE.N.name().equals(wswcReqDTO.getActive())) {
			querySb.append(INACTIVE_QUERY);
		} else {
			querySb.append(ALL_QUERY);
		}
		querySb.append(" order by ")
				.append(WSWCC_SORTBY_FIELDMAPPING.getOrDefault(wswcReqDTO.getSortBy().getField(), "WSCC_ID"))
				.append(" ")
				.append(StringUtils.trimToEmpty(wswcReqDTO.getSortBy().getDirection()))
				.append(buildPagination.apply(wswcReqDTO.getPagination()));

		wsccItemParList = jdbcTemplate.query(querySb.toString(),
				(rs, rowNum) -> new ConfigWsccListItemResDTO(rs.getLong("WSCC_ID"),
						rs.getString("WSCC_PROGRAM_DESC"),
						rs.getLong("WSCC_MIN_IC_WS_REQ"),
						rs.getLong("WSCC_MIN_AC_WS_REQ"),
						rs.getLong("WSCC_MIN_WS_INCR_FREQ"),
						rs.getLong("WSCC_MIN_WS_INCR_VAL"),
						rs.getDate("WSCC_EFFECTIVE_DT"), rs.getDate("WSCC_EXPIRATION_DT"),
						rs.getInt("CHILD_LIST"))).stream()
				.map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
				.map(dto -> dto.withDeleteFlag(checkFutureDate.test(localDateToDate.apply(dto.getStartDate()), systemDate)))
				.toList();


		return ConfigWsccListResDTO.builder()
				.pagination(wswcReqDTO.getPagination() != null ? wswcReqDTO.getPagination().withTotalItemCount(totalItemCount) : null)
				.sortBy(wswcReqDTO.getSortBy())
				.wsccSummaryList(wsccItemParList).build();
	}
}
