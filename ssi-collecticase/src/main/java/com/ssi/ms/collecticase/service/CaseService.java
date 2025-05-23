package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.constant.CollecticaseConstants;
import com.ssi.ms.collecticase.database.dao.CcaseActivitiesCmaDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCasesCmcDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmaNoticesCmnDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCmeIndividualCmiDAO;
import com.ssi.ms.collecticase.database.dao.CcaseCraCorrespondenceCrcDAO;
import com.ssi.ms.collecticase.database.dao.CcaseEntityCmeDAO;
import com.ssi.ms.collecticase.database.dao.CcaseOrganizationCmoDAO;
import com.ssi.ms.collecticase.database.dao.CcaseRemedyActivityCraDAO;
import com.ssi.ms.collecticase.database.dao.CorrespondenceCorDAO;
import com.ssi.ms.collecticase.database.dao.EmployerEmpDAO;
import com.ssi.ms.collecticase.database.dao.GttForCaselookupDAO;
import com.ssi.ms.collecticase.database.dao.StaffStfDAO;
import com.ssi.ms.collecticase.database.mapper.PageMapper;
import com.ssi.ms.collecticase.dto.ActivitiesSummaryDTO;
import com.ssi.ms.collecticase.dto.CaseCollectibleDebtsDTO;
import com.ssi.ms.collecticase.dto.CaseLookupDTO;
import com.ssi.ms.collecticase.dto.CaseNotesDTO;
import com.ssi.ms.collecticase.dto.CaseReassignDTO;
import com.ssi.ms.collecticase.dto.CcaseActivitiesCmaDTO;
import com.ssi.ms.collecticase.dto.CreateActivityDTO;
import com.ssi.ms.collecticase.dto.CreateCaseDTO;
import com.ssi.ms.collecticase.dto.GeneralActivityDTO;
import com.ssi.ms.collecticase.dto.GttForCaselookupDTO;
import com.ssi.ms.collecticase.dto.ReassignDTO;
import com.ssi.ms.collecticase.dto.StaffDTO;
import com.ssi.ms.collecticase.dto.VwCcaseCaseloadDTO;
import com.ssi.ms.collecticase.dto.VwCcaseHeaderDTO;
import com.ssi.ms.collecticase.dto.VwCcaseHeaderEntityDTO;
import com.ssi.ms.collecticase.dto.VwCcaseOpmDTO;
import com.ssi.ms.collecticase.dto.VwCcaseRemedyDTO;
import com.ssi.ms.collecticase.outputpayload.PaginationResponse;
import com.ssi.ms.collecticase.util.CollecticaseHelper;
import com.ssi.ms.collecticase.util.CollecticaseUtilFunction;
import com.ssi.ms.collecticase.util.PaginationUtil;
import com.ssi.ms.collecticase.validator.CaseLookupValidator;
import com.ssi.ms.collecticase.validator.GeneralActivityValidator;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import com.ssi.ms.platform.util.UtilFunction;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_SPECIFICS_TEMP_REDUCTION;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_SPECIFICS_TEMP_SUSPENSION;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_SENT_TEMP_PP_REDUCTION_LTR;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ACTIVITY_TYPE_SENT_TEMP_PP_SUSPENSION_LTR;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.BANKRUPTCY_SPECIALIST;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.CLAIMANT_REPRESENTS_FOR;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.CME_TYPE_IND;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.CME_TYPE_ORG;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.COLLECTION_LOF_ID;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.COLLECTION_SPECIALIST;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.COLLECTION_SUPERVISOR;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.COMM_METHOD_NOT_APPLICABLE;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.COR_RECEIPT_CLAIMANT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.COR_RECEIPT_EMPLOYER;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.COR_SOURCE_IFK_CD_FOR_CMC;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.EMPLOYER_REPRESENTS_FOR;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ENTITY_CONTACT_TYPE_ATTY;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ENTITY_CONTACT_TYPE_EMP;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_I;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ENTITY_CONTACT_TYPE_REP_O;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.INDICATOR;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.NHUIS_RETURN_SUCCESS;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.NOTICE_OF_CHANGED_WG;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.NOTICE_OF_COURT_ORDERED_WG;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.NOTICE_OF_GARNISHMENT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.NOTICE_OF_SUSPENDED_WG;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.ONLINE_COE_TXT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_CRC_ID;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_CLM_ID;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_CMT_ID;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_COE_STRING;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_COR_COE_IND;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_COR_DEC_ID_IFK;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_CD;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_COR_RECEIP_IFK;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_COR_STATUS_CD;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_COR_TS;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_EMP_ID;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_FORCED_IND;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.PIN_WLP_I720_RPT_ID;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.POUT_SQL_STRING;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.POUT_WLP_O720_COR_ID;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.POUT_WLP_O720_RETURN_CD;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.REMEDY_PAYMENT_PLAN;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.REMEDY_WAGE_GARNISHMENT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.TEMP_REDUCTION_LIEN;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.TEMP_SUSPENSION_LIEN;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.TILE_SYMBOL;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.TIME_FORMAT_INPUT;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.USER_INTERFACE;
import static com.ssi.ms.collecticase.constant.CollecticaseConstants.USR_STATUS_CD;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.ACTIVITY_ID_NOT_FOUND;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.CASE_ID_NOT_FOUND;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.CASE_REMEDY_ACTIVITY_ID_NOT_FOUND;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.CMI_ID_NOT_FOUND;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.CMN_ID_NOT_FOUND;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.COR_ID_NOT_FOUND;
import static com.ssi.ms.collecticase.constant.ErrorMessageConstant.EMP_ID_NOT_FOUND;

@Slf4j
@Service
public class CaseService extends CollecticaseBaseService {

    @Autowired
    private GeneralActivityValidator generalActivityValidator;

    @Autowired
    private CaseLookupValidator caseLookupValidator;

    public List<VwCcaseHeaderDTO> getCaseHeaderInfoByCaseId(Long caseId) {
        return vwCcaseCaseloadRepository.getCaseHeaderInfoByCaseId(caseId).stream().map(dao ->
                vwCcaseHeaderMapper.daoToDto(dao)).toList();
    }

    public List<VwCcaseOpmDTO> getClaimantOpmInfoByCaseId(Long caseId) {
        return vwCcaseCaseloadRepository.getClaimantOpmInfoByCaseId(caseId).stream().map(dao ->
                vwCcaseOpmMapper.daoToDto(dao)).toList();
    }

    public List<VwCcaseRemedyDTO> getCaseRemedyInfoByCaseId(Long caseId) {
        return vwCcaseCaseloadRepository.getCaseRemedyInfoByCaseId(caseId).stream().map(dao ->
                vwCcaseRemedyMapper.daoToDto(dao)).toList();
    }

    public List<VwCcaseHeaderEntityDTO> getCaseEntityInfoByCaseId(Long caseId) {
        return vwCcaseCaseloadRepository.getCaseEntityInfoByCaseId(caseId).stream().map(dao ->
                vwCcaseHeaderEntityMapper.daoToDto(dao)).toList();
    }

    public List<CcaseActivitiesCmaDTO> getCaseNotesByCaseId(Long caseId) {
        return ccaseActivitiesCmaRepository.getCaseNotesByCaseId(caseId).stream().map(dao ->
                ccaseActivitiesCmaMapper.daoToDto(dao)).toList();
    }

    public PaginationResponse<VwCcaseCaseloadDTO> getCaseLoadByStaffId
            (Long staffId, int page, int size, String sortBy, boolean ascending) {

        Map<String, Object> pageContentMap = new HashMap<>();
        // Create a Sort object based on the provided sort field and direction (ascending or descending)
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create the Pageable object with pagination and sorting
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<VwCcaseCaseloadDTO> pageVwCcaseCaseloadDTO = vwCcaseCaseloadRepository
                .getCaseLoadByStaffId(staffId, pageable);
        List<VwCcaseCaseloadDTO> vwCcaseCaseloadDTOList = pageVwCcaseCaseloadDTO.stream().collect(Collectors.toList());

        // PageMapper to send Pagination attributes and content in PaginationResponse
        return PageMapper.toPaginationResponse(pageVwCcaseCaseloadDTO, vwCcaseCaseloadDTOList);
    }

    public PaginationResponse<ActivitiesSummaryDTO> getActivitiesDataByCaseId
            (Long caseId, int page, int size, String sortBy, boolean ascending) {

        // Create a Sort object based on the provided sort field and direction (ascending or descending)
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();

        // Create the Pageable object with pagination and sorting
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ActivitiesSummaryDTO> pageActivitiesSummaryDTO = ccaseActivitiesCmaRepository
                .getActivitiesDataByCaseId(caseId, pageable);
        List<ActivitiesSummaryDTO> activitiesSummaryDTOList = pageActivitiesSummaryDTO.stream()
                .collect(Collectors.toList());

        // PageMapper to send Pagination attributes and content in PaginationResponse
        return PageMapper.toPaginationResponse(pageActivitiesSummaryDTO, activitiesSummaryDTOList);
    }

    public Map<String, Object> createCollecticaseCase(CreateCaseDTO createCaseDTO) {
        return CollecticaseHelper.createCollecticaseCase(createCaseDTO, ccaseCasesCmcRepository);
    }

    public Map<String, Object> createCollecticaseActivity(CreateActivityDTO createActivityDTO) {
        return CollecticaseHelper.createCollecticaseActivity(createActivityDTO, ccaseActivitiesCmaRepository);
    }

    public void updateCaseActivitiesCma(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDB, CcaseEntityCmeDAO ccaseEntityCme,
            CcaseOrganizationCmoDAO organizationCmo, CcaseCmeIndividualCmiDAO individualCmi) {

        if (ccaseEntityCme != null && ccaseEntityCme.getCmeId() != null) {
            ccaseActivitiesCmaDB.setCmaEntityConttypeIfk(ccaseEntityCme.getCmeId());
        }
        if (organizationCmo != null && organizationCmo.getCmoId() != null) {
            ccaseActivitiesCmaDB.setFkCmoIdUc(organizationCmo.getCmoId());
        }
        if (individualCmi != null && individualCmi.getCmiId() != null) {
            ccaseActivitiesCmaDB.setFkCmiIdUc(individualCmi.getCmiId());
        }

        if (CLAIMANT_REPRESENTS_FOR.equals(ccaseActivitiesCmaDB.getCmaUpdContRepFor())) {
            ccaseActivitiesCmaDB.setCmaUpdContRepFor(ccaseActivitiesCmaDB.getCmaUpdContRepFor());
            ccaseActivitiesCmaDB.setFkEmpIdRepUc(null);
        } else if (StringUtils.isNotBlank(ccaseActivitiesCmaDB.getCmaUpdContRepFor())) {
            ccaseActivitiesCmaDB.setCmaUpdContRepFor(ccaseActivitiesCmaDB.getCmaUpdContRepFor());
            ccaseActivitiesCmaDB.setFkEmpIdRepUc(ccaseActivitiesCmaDB.getFkEmpIdRepUc());
        }

        // Update CMA Entity Contact when adding New ATTY or New Repo
        if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_ATTY, ccaseActivitiesCmaDB.getCmaEntityContTypeCd())
                || UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_O,
                ccaseActivitiesCmaDB.getCmaEntityContTypeCd())) {
            ccaseActivitiesCmaDB.setCmaEntityContact(organizationCmo != null ? organizationCmo.getCmoName() : null);
        }

        if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_I,
                ccaseActivitiesCmaDB.getCmaEntityContTypeCd())) {
            ccaseActivitiesCmaDB.setCmaEntityContact(individualCmi != null ?
                    (individualCmi.getCmiFirstName() + StringUtils.SPACE + individualCmi.getCmiLastName()) : null);
            // For Activity Rep(I), no possibility from UI (disabled the Org Section) to change the contact primary option
            // By default for Rep(I), make the contact(Rep(I)) to be Primary
            ccaseActivitiesCmaDB.setCmaUpdContPrimary(INDICATOR.Y.name());
        }

        // Clearing the default value for Individual's Country & State when Firstname and lastname is not specified
        if (individualCmi != null && (individualCmi.getCmiFirstName() == null
                && individualCmi.getCmiLastName() == null)) {
            ccaseActivitiesCmaDB.setCmaUpdContIndState(null);
            ccaseActivitiesCmaDB.setCmaUpdContIndCntry(null);
        }

        ccaseActivitiesCmaRepository.save(ccaseActivitiesCmaDB);
    }

    public CcaseCmeIndividualCmiDAO populateCaseIndData(
            CcaseActivitiesCmaDAO activitiesCma, CcaseEntityCmeDAO entityCme,
            CcaseOrganizationCmoDAO organizationCmo) {
        CcaseCmeIndividualCmiDAO individualCmi = new CcaseCmeIndividualCmiDAO();
        if (StringUtils.isNotBlank(activitiesCma.getCmaUpdContIndAddr1())
                && StringUtils.isNotBlank(activitiesCma.getCmaUpdContIndFName())
                && StringUtils.isNotBlank(activitiesCma.getCmaUpdContIndLName())) {
            if (activitiesCma.getFkCmiIdUc() != null) {
                individualCmi = ccaseCmeIndividualCmiRepository.findById(activitiesCma.getFkCmiIdUc())
                        .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + activitiesCma
                                .getFkCmiIdUc(), CMI_ID_NOT_FOUND));
            }
            individualCmi.setCmiIsPrimary(activitiesCma.getCmaUpdContPrimary());

            // For Activity Rep(I), no possibility from UI(disabled the Org Section) to change the contact primary option
            // By default for Rep(I), make the contact(Rep(I)) to be Primary
            if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_I,
                    activitiesCma
                            .getCmaEntityContTypeCd())) {
                individualCmi.setCmiIsPrimary(INDICATOR.Y.name());
            }
            individualCmi.setCmiIsMailingRcpt(activitiesCma.getCmaUpdContMailingRcpt());
            individualCmi.setCmiJobTitle(activitiesCma.getCmaUpdContJobTitle());
            individualCmi
                    .setCmiFirstName(activitiesCma.getCmaUpdContIndFName());
            individualCmi.setCmiLastName(activitiesCma.getCmaUpdContIndLName());
            individualCmi.setCmiSalutationCd(activitiesCma
                    .getCmaUpdContIndSalCd());
            individualCmi.setCmiAddrLn1(activitiesCma.getCmaUpdContIndAddr1());
            individualCmi.setCmiAddrLn2(activitiesCma.getCmaUpdContIndAddr2());
            individualCmi.setCmiCity(activitiesCma.getCmaUpdContIndCity());
            individualCmi.setCmiCountry(activitiesCma
                    .getCmaUpdContIndCntry());
            individualCmi.setCmiState(activitiesCma.getCmaUpdContIndState());
            individualCmi
                    .setCmiZipPostalCd(activitiesCma.getCmaUpdContIndZip());
            individualCmi.setCmiTeleWork(activitiesCma.getCmaUpdContIndTeleW());
            individualCmi.setCmiTeleWorkExt(activitiesCma
                    .getCmaUpdContIndWExt());
            individualCmi.setCmiTeleHome(activitiesCma.getCmaUpdContIndTeleH());
            individualCmi.setCmiTeleCell(activitiesCma.getCmaUpdContIndTeleC());
            individualCmi.setCmiFax(activitiesCma.getCmaUpdContIndFax());
            individualCmi.setCmiEmails(activitiesCma.getCmaUpdContIndEmails());
            individualCmi.setCmiCommPreference(activitiesCma
                    .getCmaUpdContIndPrefCd());
            individualCmi.setCmiCreatedBy(activitiesCma.getCmaCreatedBy());
            individualCmi
                    .setCmiCreatedUsing(activitiesCma.getCmaCreatedUsing());
            individualCmi.setCmiLastUpdBy(activitiesCma.getCmaLastUpdBy());
            individualCmi
                    .setCmiLastUpdUsing(activitiesCma.getCmaLastUpdUsing());
            individualCmi.setCcaseEntityCmeDAO(entityCme);
            individualCmi.setCcaseOrganizationCmoDAO(organizationCmo);
            individualCmi.setCmiActiveInd(INDICATOR.Y.name());
            activitiesCma.setFkCmiIdUc(individualCmi.getCmiId());
            ccaseCmeIndividualCmiRepository.save(individualCmi);

            // Update Primary Contact for Individuals
            if (INDICATOR.Y.name().equals(activitiesCma.getCmaUpdContPrimary())) {
                List<CcaseCmeIndividualCmiDAO> ccaseCmeIndividualCmiList = ccaseCmeIndividualCmiRepository
                        .getCaseEntityIndividualByCaseEntityId
                                (entityCme.getCmeId(), INDICATOR.Y.name());
                for (CcaseCmeIndividualCmiDAO cmi : ccaseCmeIndividualCmiList) {
                    CcaseCmeIndividualCmiDAO ind = new CcaseCmeIndividualCmiDAO();
                    if (!UtilFunction.compareLongObject.test(cmi.getCmiId(), activitiesCma.getFkCmiIdUc())) {
                        ind = ccaseCmeIndividualCmiRepository.findById(activitiesCma.getFkCmiIdUc())
                                .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + cmi.getCmiId(),
                                        CMI_ID_NOT_FOUND));
                        ind.setCmiIsPrimary(StringUtils.EMPTY);
                        ind.setCmiCreatedBy(activitiesCma.getCmaCreatedBy());
                        ind.setCmiCreatedUsing(activitiesCma.getCmaCreatedUsing());
                        ind.setCmiLastUpdBy(activitiesCma.getCmaLastUpdBy());
                        ind.setCmiLastUpdUsing(activitiesCma.getCmaLastUpdUsing());
                        ccaseCmeIndividualCmiRepository.save(ind);
                    }
                }
            }

            // Update Mailing Recipient for Individuals
            if (INDICATOR.Y.name().equals(activitiesCma.getCmaUpdContMailingRcpt())) {
                List<CcaseCmeIndividualCmiDAO> ccaseCmeIndividualCmiList = ccaseCmeIndividualCmiRepository
                        .getCaseEntityIndividualByCaseEntityId
                                (entityCme.getCmeId(), INDICATOR.Y.name());
                for (CcaseCmeIndividualCmiDAO cmi : ccaseCmeIndividualCmiList) {
                    CcaseCmeIndividualCmiDAO ind = new CcaseCmeIndividualCmiDAO();
                    if (!UtilFunction.compareLongObject.test(cmi.getCmiId(), activitiesCma.getFkCmiIdUc())) {
                        ind = ccaseCmeIndividualCmiRepository.findById(activitiesCma.getFkCmiIdUc())
                                .orElseThrow(() -> new NotFoundException("Invalid CMI ID:" + cmi.getCmiId(),
                                        CMI_ID_NOT_FOUND));
                        ind.setCmiIsMailingRcpt(StringUtils.EMPTY);
                        ind.setCmiCreatedBy(activitiesCma.getCmaCreatedBy());
                        ind.setCmiCreatedUsing(activitiesCma.getCmaCreatedUsing());
                        ind.setCmiLastUpdBy(activitiesCma.getCmaLastUpdBy());
                        ind.setCmiLastUpdUsing(activitiesCma.getCmaLastUpdUsing());
                        ccaseCmeIndividualCmiRepository.save(ind);
                    }
                }
            }


        }
        return individualCmi;
    }

    public CcaseEntityCmeDAO populateCaseEntityData(
            CcaseActivitiesCmaDAO activitiesCma, CcaseEntityCmeDAO entityCme,
            CcaseOrganizationCmoDAO organizationCmo) {
        entityCme.setCcaseCasesCmcDAO(activitiesCma.getCcaseCasesCmcDAO());
        if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_EMP,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCmeType(CME_TYPE_ORG);
            entityCme.setCmeRole(ENTITY_CONTACT_TYPE_EMP);
            EmployerEmpDAO employer = new EmployerEmpDAO();
            employer.setEmpId(activitiesCma.getFkEmpIdRepUc());
            entityCme.setEmployerEmpDAO(employer);
        } else if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_ATTY,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CME_TYPE_ORG);
            entityCme.setCmeRole(ENTITY_CONTACT_TYPE_ATTY);
        } else if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_O,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CME_TYPE_ORG);
            entityCme.setCmeRole(ENTITY_CONTACT_TYPE_REP_O);
        } else if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_I,
                activitiesCma.getCmaEntityContTypeCd())) {
            entityCme.setCcaseOrganizationCmoDAO(organizationCmo);
            entityCme.setCmeType(CME_TYPE_IND);
            entityCme.setCmeRole(ENTITY_CONTACT_TYPE_REP_I);
        }
        if (CLAIMANT_REPRESENTS_FOR.equals(activitiesCma.getCmaUpdContRepFor())) {
            entityCme.setCmeRepresents(activitiesCma.getCmaUpdContRepFor());
            entityCme.setEmployerEmpDAO(null);
        } else if (EMPLOYER_REPRESENTS_FOR.equals(activitiesCma.getCmaUpdContRepFor())) {
            entityCme.setCmeRepresents(activitiesCma.getCmaUpdContRepFor());
            EmployerEmpDAO employer = employerEmpRepository.findById(UtilFunction.stringToLong
                            .apply(activitiesCma.getCmaUpdContRepFor()))
                    .orElseThrow(() -> new NotFoundException("Invalid EMP ID:" + activitiesCma.getCmaUpdContRepFor(),
                            EMP_ID_NOT_FOUND));
            entityCme.setEmployerEmpDAO(employer);
        }
        entityCme.setCmeEmpRemarks(null);
        entityCme.setCmeActiveInd(INDICATOR.Y.name());
        entityCme.setCmeCreatedBy(activitiesCma.getCmaCreatedBy());
        entityCme.setCmeCreatedUsing(activitiesCma.getCmaCreatedUsing());
        entityCme.setCmeLastUpdBy(activitiesCma.getCmaLastUpdBy());
        entityCme.setCmeLastUpdUsing(activitiesCma.getCmaLastUpdUsing());
        ccaseEntityCmeRepository.save(entityCme);

        return entityCme;
    }

    public CcaseOrganizationCmoDAO populateCaseOrgData(
            CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
            CcaseOrganizationCmoDAO caseOrganizationCmoDAO) {
        if (UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_ATTY, ccaseActivitiesCmaDAO.getCmaEntityContTypeCd())
                || UtilFunction.compareLongObject.test(ENTITY_CONTACT_TYPE_REP_O,
                ccaseActivitiesCmaDAO.getCmaEntityContTypeCd())) {
            if (StringUtils.isNotBlank(ccaseActivitiesCmaDAO.getCmaUpdContOrgName())) {
                caseOrganizationCmoDAO
                        .setCmoName(ccaseActivitiesCmaDAO.getCmaUpdContOrgName());
                caseOrganizationCmoDAO
                        .setCmoUIAcctNbr(ccaseActivitiesCmaDAO.getCmaUIAcctNbr());
                caseOrganizationCmoDAO.setCmoFEINNbr(ccaseActivitiesCmaDAO.getCmaFeinNbr());
                caseOrganizationCmoDAO.setCmoAddrLn1(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgAddr1());
                caseOrganizationCmoDAO.setCmoAddrLn2(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgAddr2());
                caseOrganizationCmoDAO
                        .setCmoCity(ccaseActivitiesCmaDAO.getCmaUpdContOrgCity());
                caseOrganizationCmoDAO.setCmoCountry(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgCntry());
                caseOrganizationCmoDAO.setCmoState(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgState());
                caseOrganizationCmoDAO.setCmoZipPostalCd(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgZip());
                caseOrganizationCmoDAO.setCmoTeleNum(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgTele());
                caseOrganizationCmoDAO.setCmoFax(ccaseActivitiesCmaDAO.getCmaUpdContOrgFax());
                caseOrganizationCmoDAO.setCmoCommPreference(ccaseActivitiesCmaDAO.getCmaUpdContOrgPrefCd());
                caseOrganizationCmoDAO.setCmoWebsite(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgWebsite());
                caseOrganizationCmoDAO.setCmoRemarks(ccaseActivitiesCmaDAO
                        .getCmaUpdContOrgRemark());
                caseOrganizationCmoDAO
                        .setCmoCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
                caseOrganizationCmoDAO.setCmoCreatedUsing(ccaseActivitiesCmaDAO
                        .getCmaCreatedUsing());
                caseOrganizationCmoDAO
                        .setCmoLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                caseOrganizationCmoDAO.setCmoLastUpdUsing(ccaseActivitiesCmaDAO
                        .getCmaLastUpdUsing());

                if (ccaseActivitiesCmaDAO.getFkEmpIdRepUc() != null) {
                    EmployerEmpDAO employerEmpDAO = new EmployerEmpDAO();
                    employerEmpDAO.setEmpId(ccaseActivitiesCmaDAO.getFkEmpIdRepUc());
                    caseOrganizationCmoDAO.setEmployerEmpDAO(employerEmpDAO);
                }

                ccaseOrganizationCmoRepository.save(caseOrganizationCmoDAO);
            }
        }
        return caseOrganizationCmoDAO;
    }

    public void processCorrespondence(List<Map<String, Object>> sendNoticesList, List<String> resendNoticeList,
                                      List<String> manualNoticeList, CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO,
                                      Long cwgId) {
        CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO;
        CorrespondenceCorDAO correspondenceCorDAO;
        Long crcId;
        List<Integer> multipleRecieptList = new ArrayList<Integer>();
        Map<String, Object> createCorrespondence;
        for (Map<String, Object> inputParamMap : sendNoticesList) {
            if (inputParamMap.get(PIN_CRC_ID).equals(NOTICE_OF_CHANGED_WG)
                    || inputParamMap.get(PIN_CRC_ID)
                    .equals(NOTICE_OF_SUSPENDED_WG)
                    || inputParamMap.get(PIN_CRC_ID)
                    .equals(NOTICE_OF_GARNISHMENT)
                    || inputParamMap.get(PIN_CRC_ID)
                    .equals(NOTICE_OF_COURT_ORDERED_WG)) {
                multipleRecieptList.add(COR_RECEIPT_EMPLOYER);
                multipleRecieptList.add(COR_RECEIPT_CLAIMANT);
            } else {
                multipleRecieptList.add(COR_RECEIPT_EMPLOYER);
            }
            for (Integer receiptVal : multipleRecieptList) {
                crcId = (Long) inputParamMap.get(PIN_CRC_ID);
                ccaseCraCorrespondenceCrcDAO = new CcaseCraCorrespondenceCrcDAO();
                ccaseCraCorrespondenceCrcDAO.setCrcId(crcId);
                if (Objects.equals(receiptVal, COR_RECEIPT_CLAIMANT)) {
                    inputParamMap.put(PIN_WLP_I720_COR_RECEIP_IFK,
                            ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getClaimantCmtDAO().getCmtId());
                }
                createCorrespondence = correspondenceCorRepository.createCorrespondence(
                        (Integer) inputParamMap.get(PIN_WLP_I720_RPT_ID),
                        (Integer) inputParamMap.get(PIN_WLP_I720_CLM_ID),
                        (Integer) inputParamMap.get(PIN_WLP_I720_EMP_ID),
                        (Integer) inputParamMap.get(PIN_WLP_I720_CMT_ID),
                        (String) inputParamMap.get(PIN_WLP_I720_COR_COE_IND),
                        (String) inputParamMap.get(PIN_WLP_I720_FORCED_IND),
                        (Integer) inputParamMap.get(PIN_WLP_I720_COR_STATUS_CD),
                        (Integer) inputParamMap.get(PIN_WLP_I720_COR_DEC_ID_IFK),
                        (Integer) inputParamMap.get(PIN_WLP_I720_COR_RECEIP_IFK),
                        (Integer) inputParamMap.get(PIN_WLP_I720_COR_RECEIP_CD),
                        (Timestamp) inputParamMap.get(PIN_WLP_I720_COR_TS),
                        (String) inputParamMap.get(PIN_WLP_I720_COE_STRING));

                if (createCorrespondence != null
                        && createCorrespondence.get(POUT_WLP_O720_RETURN_CD) != null
                        && NHUIS_RETURN_SUCCESS.equals((Integer) createCorrespondence.get(POUT_WLP_O720_RETURN_CD))) {
                    Long corId = ((Integer) createCorrespondence.get(POUT_WLP_O720_COR_ID)).longValue();
                    correspondenceCorDAO = correspondenceCorRepository.findById(corId)
                            .orElseThrow(() -> new NotFoundException("Invalid COR ID:" + corId, COR_ID_NOT_FOUND));

                    if (UtilFunction.compareLongObject.test(ccaseActivitiesCmaDAO.getCmaRemedyType(),
                            REMEDY_WAGE_GARNISHMENT)) {
                        if (ccaseActivitiesCmaDAO.getFkEmpIdWg() != null &&
                                CollecticaseUtilFunction.greaterThanLongObject
                                        .test(ccaseActivitiesCmaDAO.getFkEmpIdWg(), 0L)) {//SAT25570
                            correspondenceCorDAO.setCorSourceIfk(COR_SOURCE_IFK_CD_FOR_CMC);
                            correspondenceCorDAO.setCorSourceIfkCd(ccaseActivitiesCmaDAO
                                    .getCcaseCasesCmcDAO().getCmcId());
                            correspondenceCorDAO.setCorReceipIfk(receiptVal.longValue());
                        }
                    } else {
                        correspondenceCorDAO.setCorSourceIfk(COR_SOURCE_IFK_CD_FOR_CMC);
                        correspondenceCorDAO.setCorSourceIfkCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId());
                        correspondenceCorDAO.setCorReceipIfk(COR_RECEIPT_CLAIMANT.longValue());
                    }
                    correspondenceCorRepository.save(correspondenceCorDAO);
                    createCMN(ccaseActivitiesCmaDAO, corId, ccaseCraCorrespondenceCrcDAO, cwgId);
                }
            }
        }
        for (String cmnId : resendNoticeList) {
            createResendCMN(ccaseActivitiesCmaDAO, UtilFunction.stringToLong.apply(cmnId));
        }
        for (String manualCrcId : manualNoticeList) {
            createManualCMN(ccaseActivitiesCmaDAO, UtilFunction.stringToLong.apply(manualCrcId), cwgId);
            if (UtilFunction.compareLongObject.test(TEMP_REDUCTION_LIEN,
                    UtilFunction.stringToLong.apply(manualCrcId))) {
                addSystemActivity(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO(),
                        ACTIVITY_TYPE_SENT_TEMP_PP_REDUCTION_LTR,
                        REMEDY_PAYMENT_PLAN, ACTIVITY_SPECIFICS_TEMP_REDUCTION,
                        ccaseActivitiesCmaDAO.getCmaActivityNotes(), ccaseActivitiesCmaDAO.getCmaPriority());
            }
            if (UtilFunction.compareLongObject.test(TEMP_SUSPENSION_LIEN,
                    UtilFunction.stringToLong.apply(manualCrcId))) {
                addSystemActivity(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO(),
                        ACTIVITY_TYPE_SENT_TEMP_PP_SUSPENSION_LTR,
                        REMEDY_PAYMENT_PLAN,
                        ACTIVITY_SPECIFICS_TEMP_SUSPENSION,
                        ccaseActivitiesCmaDAO.getCmaActivityNotes(), ccaseActivitiesCmaDAO.getCmaPriority());
            }
        }
    }

    public void addSystemActivity(CcaseCasesCmcDAO ccaseCasesCmcDAO,
                                  Long activityTypeCd, Long remedyCd,
                                  String activitySpecifics, String activityNotes, Long casePriority) {
        CreateActivityDTO createActivityDTO = new CreateActivityDTO();
        createActivityDTO.setCaseId(ccaseCasesCmcDAO.getCmcId());
        createActivityDTO.setActivityDt(commonRepository.getCurrentDate());
        createActivityDTO.setActivityTime(TIME_FORMAT_INPUT
                .format(commonRepository.getCurrentDate()));
        createActivityDTO.setActivityTypeCd(activityTypeCd);
        createActivityDTO.setRemedyTypeCd(remedyCd);
        createActivityDTO.setCaseCharacteristics(ccaseCasesCmcDAO.getCmcCaseCharacteristics());
        createActivityDTO.setActivitySpecifics(activitySpecifics);
        createActivityDTO.setActivityNotes(activityNotes);
        createActivityDTO.setCommunicationMethod(COMM_METHOD_NOT_APPLICABLE);
        createActivityDTO.setActivityCmtRepCd(ccaseCasesCmcDAO.getCmcCmtRepTypeCd());
        if (casePriority == null) {
            createActivityDTO.setActivityCasePriority(ccaseCasesCmcDAO.getCmcCasePriority());
        } else {
            createActivityDTO.setActivityCasePriority(casePriority);
        }
        createActivityDTO.setCallingUser(ccaseCasesCmcDAO.getCmcLastUpdBy());
        createActivityDTO.setUsingProgramName(ccaseCasesCmcDAO.getCmcLastUpdUsing());

        createActivity(createActivityDTO);
    }

    private void createActivity(CreateActivityDTO createActivityDTO) {
        createCollecticaseActivity(createActivityDTO);
    }

    private void createCMN(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Long corId,
                           CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO, Long cwgId) {
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(INDICATOR.N.name());
        CorrespondenceCorDAO correspondenceCorDAO = new CorrespondenceCorDAO();
        correspondenceCorDAO.setCorId(corId);
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(correspondenceCorDAO);
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(ccaseCraCorrespondenceCrcDAO);
        ccaseCmaNoticesCmnDAO.setFkCwgId(cwgId);
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }

    public void createResendCMN(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO, Long cmnId) {
        CcaseCmaNoticesCmnDAO existingCmnDAO = ccaseCmaNoticesCmnRepository.findById(cmnId)
                .orElseThrow(() -> new NotFoundException("Invalid Activity Notices ID:" + cmnId, CMN_ID_NOT_FOUND));
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCmaDAO);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCmaDAO.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCmaDAO.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(INDICATOR.Y.name());
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(existingCmnDAO.getCorrespondenceCorDAO());
        ccaseCmaNoticesCmnDAO.setFkCwgId(existingCmnDAO.getFkCwgId());
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(existingCmnDAO.getCcaseCraCorrespondenceCrcDAO());
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }

    private void createManualCMN(CcaseActivitiesCmaDAO ccaseActivitiesCma, Long crcId, Long cwgId) {
        CcaseCmaNoticesCmnDAO ccaseCmaNoticesCmnDAO = new CcaseCmaNoticesCmnDAO();
        ccaseCmaNoticesCmnDAO.setCcaseActivitiesCmaDAO(ccaseActivitiesCma);
        ccaseCmaNoticesCmnDAO.setCmnAutoReqUi(USER_INTERFACE);
        ccaseCmaNoticesCmnDAO.setCmnCreatedBy(ccaseActivitiesCma.getCmaCreatedBy());
        ccaseCmaNoticesCmnDAO.setCmnCreatedUsing(ccaseActivitiesCma.getCmaCreatedUsing());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdBy(ccaseActivitiesCma.getCmaLastUpdBy());
        ccaseCmaNoticesCmnDAO.setCmnLastUpdUsing(ccaseActivitiesCma.getCmaLastUpdUsing());
        ccaseCmaNoticesCmnDAO.setCmnResendReq(INDICATOR.N.name());
        ccaseCmaNoticesCmnDAO.setCorrespondenceCorDAO(null);
        CcaseCraCorrespondenceCrcDAO ccaseCraCorrespondenceCrcDAO = new CcaseCraCorrespondenceCrcDAO();
        ccaseCraCorrespondenceCrcDAO.setCrcId(crcId);
        ccaseCmaNoticesCmnDAO.setFkCwgId(cwgId);
        ccaseCmaNoticesCmnDAO.setCcaseCraCorrespondenceCrcDAO(ccaseCraCorrespondenceCrcDAO);
        ccaseCmaNoticesCmnRepository.save(ccaseCmaNoticesCmnDAO);
    }

    public void processAutoCompleteAct(CcaseActivitiesCmaDAO ccaseActivitiesCmaDAO) {
        CcaseRemedyActivityCraDAO ccaseRemedyActivityCraDAO = null;
        CcaseActivitiesCmaDAO activitiesCmaDAO = null;
        List<CcaseActivitiesCmaDAO> ccaseActivitiesCmaDAOList = null;
        ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraRepository
                .getCaseRemedyActivityInfo(ccaseActivitiesCmaDAO.getCmaActivityTypeCd(),
                        ccaseActivitiesCmaDAO.getCmaRemedyType());
        if (ccaseRemedyActivityCraDAO.getCraAutoComplete() != null) {
            CcaseRemedyActivityCraDAO finalCcaseRemedyActivityCraDAO = ccaseRemedyActivityCraDAO;
            ccaseRemedyActivityCraDAO = ccaseRemedyActivityCraRepository
                    .findById(ccaseRemedyActivityCraDAO.getCraAutoComplete()).orElseThrow(() ->
                            new NotFoundException("Invalid Activity ID:" +
                                    finalCcaseRemedyActivityCraDAO.getCraAutoComplete(), ACTIVITY_ID_NOT_FOUND));
            ccaseActivitiesCmaDAOList = ccaseActivitiesCmaRepository
                    .getActivityByActivityCdAndRemedyCd(ccaseActivitiesCmaDAO.getCcaseCasesCmcDAO().getCmcId(),
                            ccaseRemedyActivityCraDAO.getCraActivityCd(), ccaseRemedyActivityCraDAO.getCraRemedyCd());
            for (CcaseActivitiesCmaDAO autoActivity : ccaseActivitiesCmaDAOList) {
                if (autoActivity.getFkEmpIdWg() == null || (UtilFunction.compareLongObject
                        .test(ccaseActivitiesCmaDAO.getFkEmpIdWg(), autoActivity.getFkEmpIdWg()))) {
                    activitiesCmaDAO = ccaseActivitiesCmaRepository.findById(autoActivity.getCmaId()).orElseThrow(() ->
                            new NotFoundException("Invalid Activity ID:" + autoActivity.getCmaId(),
                                    CASE_REMEDY_ACTIVITY_ID_NOT_FOUND));
                    activitiesCmaDAO.setCmaFollowupComplBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    activitiesCmaDAO.setCmaFollowupComplDt(commonRepository.getCurrentDate());
                    activitiesCmaDAO.setCmaFollowupComplete(INDICATOR.Y.name());
                    activitiesCmaDAO.setCmaFollowCompShNote(ccaseActivitiesCmaDAO.getCcaseRemedyActivityCraDAO()
                            .getCraAutoCompleteShNote());
                    activitiesCmaDAO.setCmaLastUpdBy(ccaseActivitiesCmaDAO.getCmaLastUpdBy());
                    activitiesCmaDAO.setCmaLastUpdUsing(ccaseActivitiesCmaDAO.getCmaLastUpdUsing());
                    ccaseActivitiesCmaRepository.save(activitiesCmaDAO);
                    break;
                }
            }
        }
    }

    public String processCOEString(
            GeneralActivityDTO generalActivityDTO) {
        String coeString = StringUtils.EMPTY;
        CaseCollectibleDebtsDTO caseCollectibleDebtsDTO = null;
        Map<String, Object> paramValueMap = new HashMap<String, Object>();
        BigDecimal opBalAmt = new BigDecimal("0.0");
        BigDecimal opFrdBalAmt = new BigDecimal("0.0");
        BigDecimal opNFBalAmt = new BigDecimal("0.0");
        BigDecimal opIntAmt = new BigDecimal("0.0");
        caseCollectibleDebtsDTO = vwCcaseCollectibleDebtsRepository
                .getCollectibleDebtsAmount(generalActivityDTO.getClaimantId(), BigDecimal.ZERO);
        coeString = coeString + ONLINE_COE_TXT;
        if (caseCollectibleDebtsDTO != null) {
            opBalAmt = caseCollectibleDebtsDTO.getOverpaymentBalanceAmount();
            opFrdBalAmt = caseCollectibleDebtsDTO.getOverpaymentFraudBalanceAmount();
            opNFBalAmt = caseCollectibleDebtsDTO.getOverpaymentNonFraudBalanceAmount();
            opIntAmt = caseCollectibleDebtsDTO.getOverpaymentInterestBalanceAmount();

        }
        coeString = coeString
                + TILE_SYMBOL
                + NumberFormat.getCurrencyInstance(Locale.US).format(opBalAmt)
                + TILE_SYMBOL
                + NumberFormat.getCurrencyInstance(Locale.US).format(opFrdBalAmt)
                + TILE_SYMBOL
                + NumberFormat.getCurrencyInstance(Locale.US).format(opNFBalAmt)
                + TILE_SYMBOL
                + NumberFormat.getCurrencyInstance(Locale.US).format(opIntAmt);
        return coeString;
    }

    public List<Long> getCaseRemedyActivityByCaseId(
            Long caseId, String activeIndicator) {
        return ccaseRemedyActivityCraRepository.getCaseRemedyActivityByCaseId(caseId, activeIndicator);
    }

    public List<Long> getRemedyActivityByCaseRemedyId(List<Long> caseRemedyId, String activeIndicator) {
        return ccaseRemedyActivityCraRepository.getCaseRemedyActivityByCaseRemedyId(caseRemedyId, activeIndicator);
    }

    public List<Long> getCaseActivityByRemedyType(List<Long> caseRemedyId, String activeIndicator, Long remedyTypeCd) {
        return ccaseRemedyActivityCraRepository.getCaseActivityByCaseRemedyId(caseRemedyId, activeIndicator,
                remedyTypeCd);
    }

    //Bak TODO object needs to check
    public List<StaffDTO> getStaffListByLofAndRole() {
        return staffStfRepository.getStaffListByLofAndRole(COLLECTION_LOF_ID, USR_STATUS_CD,
                Arrays.asList(COLLECTION_SUPERVISOR, COLLECTION_SPECIALIST, BANKRUPTCY_SPECIALIST));
    }

    public Map<String, Object> getCaseLoadSummary(@Valid Long staffId) {
        return ccaseCasesCmcRepository.getCaseLoadSummary(staffId);
    }

    //Bak TODO object needs to check
    public List<StaffDTO> getReassignStaffListByLofAndRole(@Valid Long staffId) {
        return staffStfRepository.getReassignStaffListByLofAndRole(COLLECTION_LOF_ID, USR_STATUS_CD,
                Arrays.asList(COLLECTION_SUPERVISOR, COLLECTION_SPECIALIST, BANKRUPTCY_SPECIALIST), staffId);
    }

    public List<CaseReassignDTO> getCaseReassignInfoByCaseId(@Valid Long caseId) {
        return vwCcaseCaseloadRepository.getCaseReassignInfoByCaseId(caseId);
    }

    public Map<String, String> getCLOverPaymentType() {
        // for case Lookup we shouldn't include bankruptcy so passing false
        return CollecticaseHelper.getFraudStatusValues(false);
    }

    public Map<String, String> getCLOverPaymentValue() {
        return CollecticaseHelper.getCLOverPaymentValue();
    }

    public Map<String, String> getCLFollowUpValue() {
        return CollecticaseHelper.getCLFollowUpValue();
    }

    public List<GttForCaselookupDTO> searchCaseLookup(CaseLookupDTO caseLookupDTO) {
        String poutSqlString;
        List<GttForCaselookupDAO> gttForCaselookupDAOList = null;
        List<GttForCaselookupDTO> gttForCaselookupDTOList = null;
        final HashMap<String, List<String>> errorMap = caseLookupValidator.validateCaseLookupDTO(caseLookupDTO);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Case Lookup Validation Failed.", errorMap);
        }

        Map<String, Object> caseLookup = CollecticaseHelper.getCaseLookupData(caseLookupDTO, ccaseCasesCmcRepository);
        if (caseLookup != null) {
            poutSqlString = (String) caseLookup.get(POUT_SQL_STRING);
            gttForCaselookupDAOList = customLookupRepository.processCaseLookupQuery(poutSqlString);
            gttForCaselookupDTOList = gttForCaselookupDAOList.stream().map(dao ->
                    gttForCaselookupMapper.daoToDto(dao)).toList();
        }
        return gttForCaselookupDTOList;
    }


    public void reassignCase(ReassignDTO reassignDTO) {
        CcaseCasesCmcDAO ccaseCasesCmcDAO = ccaseCasesCmcRepository.findById(reassignDTO.getCaseId())
                .orElseThrow(() -> new NotFoundException("Invalid Activity ID:" + reassignDTO.getCaseId(),
                        CASE_ID_NOT_FOUND));
        StaffStfDAO staffStfDAO = new StaffStfDAO();
        staffStfDAO.setStfId(reassignDTO.getStaffId());
        ccaseCasesCmcDAO.setStaffStfDAO(staffStfDAO);
        ccaseCasesCmcDAO.setCmcAssignedTs(commonRepository.getCurrentTimestamp());
        ccaseCasesCmcDAO.setCmcCaseNewInd(INDICATOR.Y.name());
        ccaseCasesCmcDAO.setCmcLastUpdBy(reassignDTO.getUserId());
        ccaseCasesCmcDAO.setCmcLastUpdUsing("REASSIGN_CASE");
        if (CollecticaseConstants.CASE_PRIORITY_IMMEDIATE.equals(reassignDTO.getCasePriority())) {
            ccaseCasesCmcDAO.setCmcCasePriority(CollecticaseConstants.CASE_PRIORITY_IMMDT);
        }
        ccaseCasesCmcRepository.save(ccaseCasesCmcDAO);
    }

    public Long validateSSN(@Valid String ssn) {
        return claimantCmtRepository.getClaimantByClaimantSSN(ssn);
    }

    public PaginationResponse<VwCcaseCaseloadDTO> getCaseLoadByMetric(Long staffId, Integer page,
                                                                      Integer size, String sortBy, Boolean ascending,
                                                                      String metricValue) {
        boolean newCase = false;
        boolean highMediumPriority = false;
        boolean overdue = false;
        boolean bankruptcy = false;
        // Create a Sort object based on the provided sort field and direction (ascending or descending)
        Sort sort = ascending ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        // Create the Pageable object with pagination and sorting
        Pageable pageable = PageRequest.of(page, size, sort);
        if (metricValue != null) {
            switch (metricValue) {
                case "pout_not_started" -> newCase = true;
                case "pout_bankruptcy_cases" -> bankruptcy = true;
                case "pout_overdue" -> overdue = true;
                case "pout_high_priority" -> highMediumPriority = true;
            }
        }

        List<VwCcaseCaseloadDTO> vwCcaseCaseloadDTOList = customLookupRepository.caseLoadByMetrics(staffId, newCase,
                highMediumPriority, overdue, bankruptcy);
        Page<VwCcaseCaseloadDTO> pageVwCcaseCaseloadDTO = PaginationUtil.listToPage(vwCcaseCaseloadDTOList, pageable);
        // PageMapper to send Pagination attributes and content in PaginationResponse
        return PageMapper.toPaginationResponse(pageVwCcaseCaseloadDTO, vwCcaseCaseloadDTOList);

    }

    public List<CaseNotesDTO> getCaseNotesInfoByCaseId(Long caseId) {
        return ccaseActivitiesCmaRepository.getCaseNotesInfoByCaseId(caseId);
    }
}