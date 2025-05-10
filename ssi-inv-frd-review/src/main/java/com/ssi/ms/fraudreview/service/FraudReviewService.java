package com.ssi.ms.fraudreview.service;

import static com.ssi.ms.fraudreview.constant.ALVConstants.USER_STATUS_CODE_ACTIVE;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.ssi.ms.fraudreview.constant.ALVConstants;
import com.ssi.ms.fraudreview.constant.ErrorMessageConstants;
import com.ssi.ms.fraudreview.constant.FraudReviewConstants;
import com.ssi.ms.fraudreview.constant.RoleConstants;
import com.ssi.ms.fraudreview.database.dao.ClaimClmDAO;
import com.ssi.ms.fraudreview.database.dao.IdTheftActionsITADAO;
import com.ssi.ms.fraudreview.database.dao.StaffStfDAO;
import com.ssi.ms.fraudreview.database.repository.ClaimRepository;
import com.ssi.ms.fraudreview.database.repository.IdTheftActionsRepository;
import com.ssi.ms.fraudreview.database.repository.StaffRepository;
import com.ssi.ms.fraudreview.database.repository.sourceclaims.custom.CustomFraudReviewLookupRepository;
import com.ssi.ms.fraudreview.dto.BPCStaffDTO;
import com.ssi.ms.fraudreview.dto.IdTheftHijackReqDTO;
import com.ssi.ms.fraudreview.dto.IdTheftHijackReqItemDTO;
import com.ssi.ms.fraudreview.dto.caseassign.CaseAssignErrorMessage;
import com.ssi.ms.fraudreview.dto.caseassign.CaseAssignErrorMessageItem;
import com.ssi.ms.fraudreview.dto.caseassign.CaseAssignReqDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FraudReviewService {

	public static final List<Long> BPC_STAFF_ROLE_IDS = Stream.of(RoleConstants.BPC_SUPERVISOR, RoleConstants.BPC_INVESTIGATOR,
			RoleConstants.BPC_TRIAGE_PRELIMINARY_REVIEW, RoleConstants.BPC_TRIAGE_REVIEW)
			.collect(Collectors.toCollection(ArrayList::new));

	@Autowired
	private CustomFraudReviewLookupRepository customLookupRepo;

	@Autowired
	private ClaimRepository claimRepo;

	@Autowired
	private StaffRepository staffRepo;

	@Autowired
	private IdTheftActionsRepository idTheftActionRepo;

	/*
	 * Returns all BPC staff list if the user is a supervisor
	 * returns null otherwise
	 */
	public List<BPCStaffDTO> getBPCStaffList(String userId) {
		List<BPCStaffDTO> bpcStaffDTOList = null;

		final List<StaffStfDAO> supervisor = staffRepo.getStaffByUserAndRoleId(Long.valueOf(userId), RoleConstants.BPC_SUPERVISOR,
				USER_STATUS_CODE_ACTIVE);

		if (CollectionUtils.isNotEmpty(supervisor)) {
			final List<StaffStfDAO>  bpcStaffList = staffRepo.getBPCStaffByRoleId(BPC_STAFF_ROLE_IDS, USER_STATUS_CODE_ACTIVE);

			bpcStaffDTOList = bpcStaffList.stream().map(s -> {
				return new BPCStaffDTO(s.getFkUserId(), s.getStfFirstName(), s.getStfLastName());
			}).collect(toList());

			if (CollectionUtils.isEmpty(bpcStaffDTOList)) {
				log.error("No bpc staff records found for the given roleIds.");
				throw new NotFoundException("No bpc staff records found.",
						ErrorMessageConstants.BPC_STAFFLIST_EMPTY);
			}
		} else {
			log.error("getBPCStaffList result: " + bpcStaffDTOList + "for user " + userId);
		}
		log.info("getBPCStaffList result: " + bpcStaffDTOList + "for user " + userId);
		return bpcStaffDTOList;
	}

	public void assignCases(CaseAssignReqDTO caseAssignReqDTO, String userId) throws Exception {
		final ObjectMapper objectMapper = new ObjectMapper();
		String strAssignCaseRequest;
		try {
			objectMapper.registerModule(new JavaTimeModule());

			strAssignCaseRequest = objectMapper.writeValueAsString(caseAssignReqDTO);
			log.info("Input Assign Request Json :" + strAssignCaseRequest);

			// Call the SP
			final Map<String, Object> assignCaseResult = customLookupRepo.assignUserToClaimantFraudReview(
					strAssignCaseRequest);

			final String returnFlag = assignCaseResult.get("pout_return_flag") != null
					? assignCaseResult.get("pout_return_flag").toString() : null;

			final String returnMessage = assignCaseResult.get("pout_return_message") != null
					? assignCaseResult.get("pout_return_message").toString() : StringUtils.EMPTY;

			//Handle errors
			if (!FraudReviewConstants.SP_RETURN_FLAG_SUCCESS.equals(returnFlag)) {

				if (assignCaseResult.get("POUT_UPDATE_RESPONSE") != null) {
					final String strUpdResponseExceptionJson = assignCaseResult.get("POUT_UPDATE_RESPONSE").toString();
					final JsonNode jsonNode = objectMapper.readTree(strUpdResponseExceptionJson);
					final CaseAssignErrorMessage caseAssignErrorMessage = objectMapper.treeToValue(jsonNode,
							CaseAssignErrorMessage.class);

					final CaseAssignErrorMessageItem[] validationMessages  = caseAssignErrorMessage.getMessages();
					//Handle business validations
					if (validationMessages != null && validationMessages.length > 0) {
						final String concatenatedMessages = Arrays.asList(validationMessages).stream()
								.map(itm -> {
									return new StringBuffer().append("CmtId : ")
											.append(itm.getCmtId()).append(" ")
											.append(itm.getMessage()).toString();
								})
								.collect(Collectors.joining("; "));

						log.error("Case Assign Biz Validation messages " + concatenatedMessages);
						throw new CustomValidationException(concatenatedMessages, Map.of("assignCaseBizValidation",
								List.of(ErrorMessageConstants.ASSIGN_CASE_BIZ_VALDN_ERR_MSG)));
					}

					final CaseAssignErrorMessageItem[] dbExceptions  = caseAssignErrorMessage.getExceptions();
					//Handle DB Exceptions
					if (dbExceptions != null && dbExceptions.length > 0) {
						final String concatenatedMessages = Arrays.asList(dbExceptions).stream()
								.map(itm -> {
									return new StringBuffer().append("CmtId : ")
											.append(itm.getCmtId()).append(" ")
											.append(itm.getMessage()).toString();
								})
								.collect(Collectors.joining("; "));

						log.error("Case Assign DB Exception messages" + concatenatedMessages);

						throw new CustomValidationException(concatenatedMessages, Map.of("assignCaseDBError",
								List.of(ErrorMessageConstants.ASSIGN_CASE_DB_ERR_MSG)));
					}
					if (StringUtils.isNotEmpty(returnMessage)) {
						throw new CustomValidationException(returnMessage, Map.of("assignCaseSPError",
								List.of(ErrorMessageConstants.ASSIGN_CASE_BIZ_VALDN_ERR_MSG)));
					}
				}
			}
		} catch (JsonProcessingException e) {
			log.error("Case Assign : Error while mapping search results json:" + e.getMessage(), e);
			throw e;
		}
	}

	public void markAsIdTheft(IdTheftHijackReqDTO idTheftRequest, String userId) {
		if (idTheftRequest != null && CollectionUtils.isNotEmpty(idTheftRequest.getIdTheftRequestItems())) {
			log.info("MarkAsIdTheft request:  " + idTheftRequest);
			boolean allrecordsInserted = false;
			final List<IdTheftHijackReqItemDTO> idTheftRequestItems = idTheftRequest.getIdTheftRequestItems();

			final StaffStfDAO staffDAO = staffRepo.findByFkUserId(Long.parseLong(userId));
			if (staffDAO != null && staffDAO.getStfId() != null) {
				allrecordsInserted = idTheftRequestItems.stream()
						.map(reqItem -> {
							final Optional<ClaimClmDAO> optClaimclmDAO = claimRepo.findById(reqItem.getClmId());
							if (optClaimclmDAO.isPresent()) {
								final Date clmEffectiveDt = optClaimclmDAO.get().getClmEffectiveDt();
								final IdTheftActionsITADAO itaDAO = new IdTheftActionsITADAO();
								populateIdTheftActionsDAO(itaDAO, userId, staffDAO.getStfId(),
										clmEffectiveDt, reqItem);
								log.info("Creating ID Theft Action record " + itaDAO);
								idTheftActionRepo.save(itaDAO);
								return true;
							} else {
								log.error("Claim record not found for Id:" + reqItem.getClmId()
								+ "Skipped ITA record creation.");
								return false;
							}
						}).allMatch(val -> val);

				if (!allrecordsInserted) {
					log.error("Not all records are marked as IdTheft");
					throw new NotFoundException("One or more entries could not be marked as Id Theft",
							ErrorMessageConstants.MARK_ID_THEFT_RECORDS_SKIPPED);
				}
			} else {
				allrecordsInserted = false;
				log.error("Staff record not found for userId: " + userId);
				throw new NotFoundException("Staff record not found for the current user. ",
						ErrorMessageConstants.STAFF_RECORD_NOT_FOUND);
			}
		}
	}


	private void populateIdTheftActionsDAO(IdTheftActionsITADAO idTheftActionsDAO, String userId, Long staffId, Date clmEffectiveDate,
			IdTheftHijackReqItemDTO idTheftReqItem) {
		idTheftActionsDAO.setItaActionCd(ALVConstants.ITA_ACTION_CD_RECORD_IDTHEFT_HIJACKING);
		idTheftActionsDAO.setItaSourceCd(ALVConstants.ITA_SOURCE_CD_BPC_REVIEW);
		idTheftActionsDAO.setItaIdTheftType(FraudReviewConstants.ITA_ID_THEFT_TYPE_I);
		idTheftActionsDAO.setFkCmtId(idTheftReqItem.getCmtId());
		idTheftActionsDAO.setFkClmId(idTheftReqItem.getClmId());
		idTheftActionsDAO.setFkCapId(idTheftReqItem.getCapId());
		idTheftActionsDAO.setFkCmtiId(idTheftReqItem.getCmtiId());
		idTheftActionsDAO.setFkStaffId(staffId);
		idTheftActionsDAO.setItaIdTheftDt(clmEffectiveDate);
		idTheftActionsDAO.setItaStatusInd(FraudReviewConstants.ITA_STATUS_IND_UNPROCESSED);
		idTheftActionsDAO.setItaProcessStatus(FraudReviewConstants.ITA_PROCESS_STATUS_UNPROCESSED);
		idTheftActionsDAO.setItaCreatedBy(userId);
		idTheftActionsDAO.setItaCreatedUsing(FraudReviewConstants.ITA_CREATED_USING_FRD_REVIEW);
		idTheftActionsDAO.setItaLastUpdBy(userId);
		idTheftActionsDAO.setItaLastUpdUsing(FraudReviewConstants.ITA_CREATED_USING_FRD_REVIEW);
	}

}
