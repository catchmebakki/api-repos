package com.ssi.ms.configuration.service;

import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.constant.ConfigurationConstants.ACTIVE;
import com.ssi.ms.configuration.constant.ConfigurationConstants.WSWCMODIFICATIONTYPE;
import com.ssi.ms.configuration.database.dao.AllowValAlvDAO;
import com.ssi.ms.configuration.database.dao.LocalOfficeLofDAO;
import com.ssi.ms.configuration.database.dao.WsAutoWvrConfigWswcDAO;
import com.ssi.ms.configuration.database.mapper.WsAutoWvrConfigWswcMapper;
import com.ssi.ms.configuration.database.repository.AllowValAlvRepository;
import com.ssi.ms.configuration.database.repository.LocalOfficeLofRepository;
import com.ssi.ms.configuration.database.repository.WsAutoWvrConfigWswcRepository;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcChildListReqDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListItemResDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListReqDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListResDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcSaveReqDTO;
import com.ssi.ms.configuration.dto.wswc.WsAutoWvrConfigWswcResDTO;
import com.ssi.ms.configuration.validator.ConfigWswcValidator;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.ssi.ms.configuration.constant.ConfigurationConstants.DATE_TIME_FORMATTER;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.GENERATECOMMENTS;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.DELETE_EXP_DT_AUTO_UPDATE;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.BUSINESS_UNIT_INVALID;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.BUSINESS_UNIT_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.REASON_CD_INVALID;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.REASON_CD_MANDATORY;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.WSWC_ID_NOT_FOUND;
import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.GET_DEFAULT_PAGINATION;
import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.GET_DEFAULT_SORT_BY;
import static com.ssi.ms.configuration.util.ConfigUtilFunction.IS_CONFIG_ACTIVE;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

@Slf4j
@Service
public class ConfigWswcService {
    @Autowired
    private WsAutoWvrConfigWswcRepository wswcRepository;
    @Autowired
    private LocalOfficeLofRepository lofRepository;
    @Autowired
    private AllowValAlvRepository alvRepository;
    @Autowired
    private WsAutoWvrConfigWswcMapper wswcMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ParameterParRepository commonParRepository;
    @Autowired
    private ConfigWswcValidator configWswcValidator;
    @PersistenceContext
    private final EntityManager entityManager;
    private final Function<ConfigWswcListReqDTO, Tuple2<PageRequest, ConfigWswcListReqDTO>> getPageRequest = configWswcListReqDTO -> {
        if (null == configWswcListReqDTO.getPagination()) {
            configWswcListReqDTO = configWswcListReqDTO.withPagination(GET_DEFAULT_PAGINATION.get());
        }
        if (null == configWswcListReqDTO.getSortBy()) {
            configWswcListReqDTO =
                    configWswcListReqDTO.withSortBy(GET_DEFAULT_SORT_BY.apply(Map.of(CommonConstant.DEFAULT, "wswcId")));
        }
        return Tuples.of(PageRequest.of(configWswcListReqDTO.getPagination().getPageNumber() - 1,
                        configWswcListReqDTO.getPagination().getPageSize(),
                        Sort.by(Sort.Direction.fromOptionalString(configWswcListReqDTO.getSortBy().getDirection())
                                        .orElseGet(() -> Sort.Direction.ASC),
                                configWswcListReqDTO.getSortBy().getField())),
                configWswcListReqDTO);
    };
    public ConfigWswcListResDTO getWorkSearchWaiverSummary(ConfigWswcListReqDTO wswcReqDTO) {
        final Tuple2<PageRequest, ConfigWswcListReqDTO> tupel2OfPageReqAndCmtDto = getPageRequest.apply(wswcReqDTO);
        Page<WsAutoWvrConfigWswcDAO> pagedResult = null;
        ConfigWswcListResDTO wswcListResDTO;
        final Date systemDate = commonParRepository.getCurrentDate();
        if (ACTIVE.Y.name().equals(wswcReqDTO.getActive())) {
            pagedResult = wswcRepository.findActiveWswcDaos(tupel2OfPageReqAndCmtDto.getT1());
            assert pagedResult != null;
            final List<ConfigWswcListItemResDTO> configWswcListItemResDTO = pagedResult.getContent().stream()
                    .map(dao -> wswcMapper.daoToSummaryDto(dao))
                    .map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
                    .map(dto -> dto.withDeleteFlag(checkFutureDate.test(localDateToDate.apply(dto.getStartDate()), systemDate)))
                    .toList();

            wswcListResDTO = ConfigWswcListResDTO.builder()
                    .wswcSummaryList(configWswcListItemResDTO)
                    .pagination(tupel2OfPageReqAndCmtDto.getT2().getPagination().withTotalItemCount(pagedResult.getTotalElements()))
                    .build();
        } else {
            if (null == wswcReqDTO.getSortBy()) {
                wswcReqDTO = wswcReqDTO.withSortBy(GET_DEFAULT_SORT_BY.apply(Map.of(CommonConstant.DEFAULT, "wswcId")));
            }
            wswcListResDTO = wswcRepository.filterWswcBasedLookupCriteria(wswcReqDTO, systemDate);
        }
        return wswcListResDTO;
    }

    public WsAutoWvrConfigWswcResDTO getWorkSearchWaiverDetails(Long wswcId) {
        final Date systemDate = commonParRepository.getCurrentDate();
        return wswcRepository.findById(wswcId)
                .map(parDao -> wswcMapper.daoToDto(parDao))
                .map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
                .orElseThrow(() -> new NotFoundException("Minimum Weekly Work Search Waiver not found for the ID: " + wswcId, WSWC_ID_NOT_FOUND));
    }

    @Transactional(timeout = 30)
    public void saveWorkSearchWaiverDetails(ConfigWswcSaveReqDTO wswcReqDTO, String userId) {
        final WsAutoWvrConfigWswcDAO wswcDAO = wswcRepository.findById(wswcReqDTO.getWswcId())
                .orElseThrow(() -> new NotFoundException("Minimum Weekly Work Search Waiver not found for the ID:"
                        + wswcReqDTO.getWswcId(), WSWC_ID_NOT_FOUND));
        // Validation Start
        final Date systemDate = commonParRepository.getCurrentDate();
        final HashMap<String, List<String>> errorMap = configWswcValidator.validateWswcSave(
                wswcReqDTO, wswcDAO, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Work Search Requirement Modification failed.", errorMap);
        }
        // Validation End
        if (WSWCMODIFICATIONTYPE.DEACTIVATE.name().equals(wswcReqDTO.getModificationType())) {
            wswcDAO.setWswcExpirationDt(localDateToDate.apply(wswcReqDTO.getModificationDate()));
            commonSave(wswcDAO, false,
                    userId,
                    null,
                    wswcReqDTO.getComments()
            );
        } else if (WSWCMODIFICATIONTYPE.REACTIVATE.name().equals(wswcReqDTO.getModificationType())) {
            wswcDAO.setWswcExpirationDt(null);
            commonSave(wswcDAO, true,
                    userId,
                    wswcReqDTO.getModificationDate(),
                    wswcReqDTO.getComments()
            );
        } else if (WSWCMODIFICATIONTYPE.STARTDATE.name().equals(wswcReqDTO.getModificationType())) {
            commonSave(wswcDAO, false,
                    userId,
                    wswcReqDTO.getModificationDate(),
                    wswcReqDTO.getComments()
            );
        } else if (WSWCMODIFICATIONTYPE.ENDDATE.name().equals(wswcReqDTO.getModificationType())) {
            wswcDAO.setWswcExpirationDt(localDateToDate.apply(wswcReqDTO.getModificationDate()));
            commonSave(wswcDAO, false,
                    userId,
                    null,
                    wswcReqDTO.getComments()
            );
        } else if (WSWCMODIFICATIONTYPE.CONFIGURATION.name().equals(wswcReqDTO.getModificationType())) {
            if (wswcReqDTO.getBusinessUnit() == null) {
                throw new NotFoundException("Business Unit must be selected", BUSINESS_UNIT_MANDATORY);
            }
            if (wswcReqDTO.getReasonCd() == null) {
                throw new NotFoundException("Reason must be selected" + wswcReqDTO.getWswcId(), REASON_CD_MANDATORY);
            }
            final LocalOfficeLofDAO lofDAO = lofRepository.findById(wswcReqDTO.getBusinessUnit())
                    .orElseThrow(() -> new NotFoundException("Invalid Business Unit:" + wswcReqDTO.getBusinessUnit(), BUSINESS_UNIT_INVALID));

            final AllowValAlvDAO reasonAlvDAO = alvRepository.findById(wswcReqDTO.getReasonCd())
                    .orElseThrow(() -> new NotFoundException("Invalid Reason Code:" + wswcReqDTO.getReasonCd(), REASON_CD_INVALID));

            //Expire the existing entry
            final Date expDt = wswcDAO.getWswcExpirationDt();
            wswcDAO.setWswcExpirationDt(localDateToDate.apply(wswcReqDTO.getModificationDate().minusDays(1)));
            commonSave(wswcDAO, false,
                    userId,
                    null,
                    wswcReqDTO.getComments()
            );

            //Create a new entry with open expiration date
            final WsAutoWvrConfigWswcDAO newWswcDAO  = new WsAutoWvrConfigWswcDAO();
            BeanUtils.copyProperties(wswcDAO, newWswcDAO);
            newWswcDAO.setWswcReasonAlvDAO(reasonAlvDAO);
            newWswcDAO.setLocalOfficeLofDAO(lofDAO);
            newWswcDAO.setWswcSysOverwriteInd(wswcReqDTO.getAutoOverwrite());
            newWswcDAO.setWswcExpirationDt(expDt);
            commonSave(newWswcDAO, true,
                    userId,
                    wswcReqDTO.getModificationDate(),
                    wswcReqDTO.getComments()
            );
        }
    }

    private void commonSave(WsAutoWvrConfigWswcDAO wswcDAO,
                                        boolean create, // Inserting a new entry
                                        String userId,
                                        LocalDate effectiveDate,
                                        String usrComments
    ) {
        if (create) {
            entityManager.detach(wswcDAO);
            wswcDAO.setWswcId(null);
            wswcDAO.setWswcCreatedBy(userId);
            wswcDAO.setWswcCreatedUsing(CommonConstant.SYSTEM);
        }

        final String generatedComments = GENERATECOMMENTS.apply(new String[]{
                StringUtils.trimToEmpty(wswcDAO.getWswcUsrComments()),
                userService.getUserName(Long.valueOf(userId)),
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
                StringUtils.trimToEmpty(usrComments)});
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        configWswcValidator.validateGeneratedComments(generatedComments, errorMap);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Work Search Waiver Modification failed.", errorMap);
        }
        wswcDAO.setWswcUsrComments(generatedComments);

        if (effectiveDate != null) {
            wswcDAO.setWswcEffectiveDt(localDateToDate.apply(effectiveDate));
        }
        wswcDAO.setWswcLastUpdBy(userId);
        wswcDAO.setWswcLastUpdUsing(CommonConstant.SYSTEM);
        wswcRepository.save(wswcDAO);
    }

    @Autowired
    public ConfigWswcService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }

    public List<ConfigWswcListItemResDTO> getChildWswcById(ConfigWswcChildListReqDTO wswcReqDTO) {
        final Date systemDate = commonParRepository.getCurrentDate();
        List<ConfigWswcListItemResDTO> wswcListDTO = null;
        if (ConfigurationConstants.ACTIVE.ALL.name().equals(wswcReqDTO.getActive())) {
            wswcListDTO = wswcRepository.findChildListByWswcId(wswcReqDTO.getWswcId());
        } else if (ConfigurationConstants.ACTIVE.N.name().equals(wswcReqDTO.getActive())) {
            wswcListDTO = wswcRepository.findInactiveChildListByWswcId(wswcReqDTO.getWswcId());
        } else {
            wswcListDTO = wswcRepository.findActiveChildListByWswcId(wswcReqDTO.getWswcId());
        }
        return wswcListDTO.stream()
                .map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
                .map(dto -> dto.withDeleteFlag(checkFutureDate.test(localDateToDate.apply(dto.getStartDate()), systemDate)))
                .toList();
    }

    public void deleteWswc(Long wswcId, String userId) {
        final WsAutoWvrConfigWswcDAO wswcDAO = wswcRepository.findById(wswcId)
                .orElseThrow(() -> new NotFoundException("Work Search Waiver not found for the ID:"
                        + wswcId, WSWC_ID_NOT_FOUND));
        // Validation Start
        final Date systemDate = commonParRepository.getCurrentDate();
        final HashMap<String, List<String>> errorMap = configWswcValidator.validateWswcDelete(wswcDAO, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Work Search Waiver Delete failed.", errorMap);
        }
        // Validation End
        final List<WsAutoWvrConfigWswcDAO> wswcList = wswcRepository.findByWswcCategoryAndExpDate(
                wswcDAO.getWswcId(),
                localDateToDate.apply(dateToLocalDate.apply(wswcDAO.getWswcEffectiveDt()).minusDays(1)));
        if (wswcList != null && !wswcList.isEmpty()) {
            final WsAutoWvrConfigWswcDAO nextFutureWswc = wswcList.get(0);
            final Date oldExpDt = nextFutureWswc.getWswcExpirationDt();
            nextFutureWswc.setWswcExpirationDt(wswcDAO.getWswcExpirationDt());
            commonSave(wswcDAO, false, userId, null,
                    DELETE_EXP_DT_AUTO_UPDATE + dateToString.apply(oldExpDt) + "'");
        }
        wswcRepository.delete(wswcDAO);
    }
}
