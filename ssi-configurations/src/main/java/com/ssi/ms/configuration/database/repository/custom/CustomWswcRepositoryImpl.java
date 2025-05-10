package com.ssi.ms.configuration.database.repository.custom;

import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListItemResDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListReqDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListResDTO;
import com.ssi.ms.platform.dto.PaginationDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.WSWC_SORTBY_FIELDMAPPING;
import static com.ssi.ms.configuration.util.ConfigUtilFunction.IS_CONFIG_ACTIVE;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

public class CustomWswcRepositoryImpl implements CustomWswcRepository {
	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final String INACTIVE_QUERY = """
	SELECT WSWC_ID, FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD, WSWC_SCENARIO_DESC, WSWC_SYS_OVERWRITE_IND,
		WSWC_EFFECTIVE_DT, WSWC_EXPIRATION_DT, WSWC_REASON_CD, ALV_SHORT_DESC_TXT AS WSWC_REASON_DESC,
		(SELECT (COUNT (*) - 1) FROM WS_AUTO_WVR_CONFIG_WSWC WHERE FK_LOF_ID=a.FK_LOF_ID
		AND WSWC_SCENARIO_NUM_EVENT_CD=a.WSWC_SCENARIO_NUM_EVENT_CD
		AND (TRUNC(sysdate) > WSWC_EXPIRATION_DT OR TRUNC(sysdate) < WSWC_EFFECTIVE_DT)) AS CHILD_LIST
	FROM (
		SELECT wswc.*, RANK () OVER (
			PARTITION BY FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD ORDER BY WSWC_EFFECTIVE_DT DESC, WSWC_ID DESC) rnk
		FROM WS_AUTO_WVR_CONFIG_WSWC wswc WHERE
			(TRUNC(sysdate) > wswc.WSWC_EXPIRATION_DT OR TRUNC(sysdate) < wswc.WSWC_EFFECTIVE_DT)
	) a JOIN ALLOW_VAL_ALV ON ALV_ID=WSWC_REASON_CD where a.rnk=1
			""";

	private static final String ALL_QUERY = """
	SELECT WSWC_ID, FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD, WSWC_SCENARIO_DESC, WSWC_SYS_OVERWRITE_IND,
	WSWC_EFFECTIVE_DT, WSWC_EXPIRATION_DT, WSWC_REASON_CD, ALV_SHORT_DESC_TXT AS WSWC_REASON_DESC,
		(SELECT (COUNT (*) - 1) FROM WS_AUTO_WVR_CONFIG_WSWC
		WHERE FK_LOF_ID=a.FK_LOF_ID AND WSWC_SCENARIO_NUM_EVENT_CD=a.WSWC_SCENARIO_NUM_EVENT_CD) AS CHILD_LIST
	FROM (
		SELECT WSWC_ID, FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD, WSWC_SCENARIO_DESC, WSWC_SYS_OVERWRITE_IND,
		WSWC_EFFECTIVE_DT, WSWC_EXPIRATION_DT, WSWC_REASON_CD
		FROM WS_AUTO_WVR_CONFIG_WSWC
			WHERE TRUNC(SYSDATE) BETWEEN WSWC_EFFECTIVE_DT AND COALESCE(WSWC_EXPIRATION_DT, TRUNC(SYSDATE))
		UNION
		SELECT WSWC_ID, FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD, WSWC_SCENARIO_DESC, WSWC_SYS_OVERWRITE_IND,
		WSWC_EFFECTIVE_DT, WSWC_EXPIRATION_DT, WSWC_REASON_CD
		FROM (
			SELECT wswc.*, RANK () OVER (
				PARTITION BY FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD ORDER BY WSWC_EFFECTIVE_DT DESC, WSWC_ID DESC) rnk
			FROM WS_AUTO_WVR_CONFIG_WSWC wswc WHERE
				NOT EXISTS(
					SELECT 1 FROM WS_AUTO_WVR_CONFIG_WSWC wswc1
					WHERE wswc.FK_LOF_ID=wswc1.FK_LOF_ID AND wswc.WSWC_SCENARIO_NUM_EVENT_CD=wswc1.WSWC_SCENARIO_NUM_EVENT_CD
					AND TRUNC(SYSDATE) BETWEEN WSWC_EFFECTIVE_DT AND COALESCE(WSWC_EXPIRATION_DT, TRUNC(SYSDATE))
				)
		)
	) a JOIN ALLOW_VAL_ALV ON ALV_ID=WSWC_REASON_CD
	""";

	private final Function<String, Long> getTotalItemCount = (criteriaStr) ->
			Optional.of("""
                            SELECT count(distinct concat(FK_LOF_ID, WSWC_SCENARIO_NUM_EVENT_CD))
                            FROM WS_AUTO_WVR_CONFIG_WSWC wswc
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
	public ConfigWswcListResDTO filterWswcBasedLookupCriteria(ConfigWswcListReqDTO wswcReqDTO, Date systemDate) {
		List<ConfigWswcListItemResDTO> wswcItemParList;
		final StringBuilder querySb = new StringBuilder();
		Long totalItemCount = 0L;
		if (wswcReqDTO.getPagination() != null && wswcReqDTO.getPagination().getNeedTotalCount()) {
			totalItemCount = getTotalItemCount.apply(
					"WHERE "
					+ (ConfigurationConstants.ACTIVE.ALL.name().equals(wswcReqDTO.getActive()) ? "1=1"
							: " (TRUNC(sysdate) > wswc.WSWC_EXPIRATION_DT OR TRUNC(sysdate) < wswc.WSWC_EFFECTIVE_DT)")
			);
		}

		if (ConfigurationConstants.ACTIVE.N.name().equals(wswcReqDTO.getActive())) {
			querySb.append(INACTIVE_QUERY);
		} else {
			querySb.append(ALL_QUERY);
		}
		querySb.append(" order by ")
				.append(WSWC_SORTBY_FIELDMAPPING.getOrDefault(wswcReqDTO.getSortBy().getField(), "WSWC_ID"))
				.append(" ")
				.append(StringUtils.trimToEmpty(wswcReqDTO.getSortBy().getDirection()))
				.append(buildPagination.apply(wswcReqDTO.getPagination()));

		wswcItemParList = jdbcTemplate.query(querySb.toString(),
				(rs, rowNum) -> new ConfigWswcListItemResDTO(rs.getLong("WSWC_ID"),
						rs.getString("WSWC_SCENARIO_DESC"), rs.getString("WSWC_REASON_DESC"),
						rs.getString("WSWC_SYS_OVERWRITE_IND"),
						rs.getDate("WSWC_EFFECTIVE_DT"), rs.getDate("WSWC_EXPIRATION_DT"),
						rs.getInt("CHILD_LIST"))).stream()
				.map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
				.map(dto -> dto.withDeleteFlag(checkFutureDate.test(localDateToDate.apply(dto.getStartDate()), systemDate)))
				.toList();


		return ConfigWswcListResDTO.builder()
				.pagination(wswcReqDTO.getPagination() != null ? wswcReqDTO.getPagination().withTotalItemCount(totalItemCount) : null)
				.sortBy(wswcReqDTO.getSortBy())
				.wswcSummaryList(wswcItemParList).build();
	}
}
