package com.ssi.ms.fraudreview.service;

import java.sql.Clob;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;
import com.ssi.ms.fraudreview.constant.FraudReviewConstants;
import com.ssi.ms.fraudreview.database.repository.sourceclaims.custom.CustomFraudReviewLookupRepository;
import com.ssi.ms.fraudreview.dto.lookup.LookupReqDTO;
import com.ssi.ms.fraudreview.dto.lookup.LookupResDTO;
import com.ssi.ms.fraudreview.util.FraudReviewUtil;
import com.ssi.ms.platform.exception.custom.CustomValidationException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LookupService {

	@Autowired
	private CustomFraudReviewLookupRepository customLookupRepo;

	@Transactional
	public LookupResDTO getSearchResult(LookupReqDTO lookupReqDTO, String userId) throws Exception {
		LookupResDTO lookupResDTO = null;
		final ObjectMapper objectMapper = new ObjectMapper();

		String strLookupCriteria;
		try {
			objectMapper.registerModule(new JavaTimeModule());

			lookupReqDTO = processLookupReqDTO(lookupReqDTO, userId);

			strLookupCriteria = objectMapper.writeValueAsString(lookupReqDTO);
			System.out.println("Input Json :" + strLookupCriteria);

			final Integer pageNumber = lookupReqDTO.getPageNumber() != null
					? lookupReqDTO.getPageNumber() : FraudReviewConstants.DEFAULT_PAGE_NUMBER;
			final Integer pageSize = lookupReqDTO.getPageSize() != null ? lookupReqDTO.getPageSize()
					: FraudReviewConstants.DEFAULT_PAGE_SIZE;

			// Call the SP
			final Map<String, Object> clmApplnSearchResult = customLookupRepo.searchClaimApplicationList(
					strLookupCriteria, pageNumber, pageSize);

			final String spReturnFlag = clmApplnSearchResult.get("POUT_RETURN_FLAG") != null
					? clmApplnSearchResult.get("POUT_RETURN_FLAG").toString() : "";
			final String spReturnMessage = clmApplnSearchResult.get("POUT_RETURN_MESSAGE") != null
					? clmApplnSearchResult.get("POUT_RETURN_MESSAGE").toString() : "";

			if (FraudReviewConstants.SP_RETURN_FLAG_SUCCESS.equals(spReturnFlag)) {
				if (MapUtils.isNotEmpty(clmApplnSearchResult) && clmApplnSearchResult.get("POUT_RECORDS") != null) {

					final String strSearchRecords =  FraudReviewUtil.clobToString(
							(Clob) clmApplnSearchResult.get("POUT_RECORDS"));
					final boolean isOutputJsonValid = strSearchRecords != null && strSearchRecords.length() > 2;
					if (isOutputJsonValid) {
						System.out.println("POUT_RECORDS : "  + strSearchRecords);
						final JsonNode jsonNode = objectMapper.readTree(strSearchRecords);
						lookupResDTO = objectMapper.treeToValue(jsonNode, LookupResDTO.class);

					} else {
						log.info("SP returned empty resultset");
					}
				}
			} else {
				log.error("DBeError during the fraud review lookup storedprocedure execution");
				throw new CustomValidationException(spReturnMessage, Map.of("lookupDBError",
						List.of(ErrorMessageConstants.LOOKUP_DB_ERR_MSG)));

			}
		} catch (JsonProcessingException e) {
			log.error("Error while mapping search results json:" + e.getMessage(), e);
			throw e;
		}

		return lookupResDTO;
	}

	private LookupReqDTO processLookupReqDTO(LookupReqDTO lookupReqDTO, String userId) {

		if (lookupReqDTO.getLookup().isItemsAssignedToMe() && StringUtils.isNotEmpty(userId)) {
			lookupReqDTO.getLookup().setAssignedTo(userId);
		}

		return lookupReqDTO;
	}

}
