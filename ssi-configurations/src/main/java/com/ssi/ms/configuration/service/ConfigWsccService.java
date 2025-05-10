package com.ssi.ms.configuration.service;

import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.constant.ConfigurationConstants.ACTIVE;
import com.ssi.ms.configuration.constant.ConfigurationConstants.WSCCMODIFICATIONTYPE;
import com.ssi.ms.configuration.database.dao.WsCpsConfigWsccDAO;
import com.ssi.ms.configuration.database.mapper.WsCpsConfigWsccMapper;
import com.ssi.ms.configuration.database.repository.WsCpsConfigWsccRepository;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccChildListReqDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListItemResDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListReqDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccListResDTO;
import com.ssi.ms.configuration.dto.wscc.ConfigWsccSaveReqDTO;
import com.ssi.ms.configuration.dto.wscc.WsCpsConfigWsccResDTO;
import com.ssi.ms.configuration.validator.ConfigWsccValidator;
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
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.WSCC_ID_NOT_FOUND;
import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.GET_DEFAULT_PAGINATION;
import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.GET_DEFAULT_SORT_BY;
import static com.ssi.ms.configuration.util.ConfigUtilFunction.IS_CONFIG_ACTIVE;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;

@Slf4j
@Service
public class ConfigWsccService {
    @PersistenceContext
    private final EntityManager entityManager;
    @Autowired
    private WsCpsConfigWsccRepository wsccRepository;
    @Autowired
    private WsCpsConfigWsccMapper wsccMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ParameterParRepository commonParRepository;
    @Autowired
    private ConfigWsccValidator configWsccValidator;
    private final Function<ConfigWsccListReqDTO, Tuple2<PageRequest, ConfigWsccListReqDTO>> getPageRequest = configWsccListReqDTO -> {
        if (null == configWsccListReqDTO.getPagination()) {
            configWsccListReqDTO = configWsccListReqDTO.withPagination(GET_DEFAULT_PAGINATION.get());
        }
        if (null == configWsccListReqDTO.getSortBy()) {
            configWsccListReqDTO =
                    configWsccListReqDTO.withSortBy(GET_DEFAULT_SORT_BY.apply(Map.of(CommonConstant.DEFAULT,
                            "clmProgSpecCpsDAO.cpsProgramCdAlvDAO.alvShortDecTxt")));
        }
        return Tuples.of(PageRequest.of(configWsccListReqDTO.getPagination().getPageNumber() - 1,
                        configWsccListReqDTO.getPagination().getPageSize(),
                        Sort.by(Sort.Direction.fromOptionalString(configWsccListReqDTO.getSortBy().getDirection())
                                        .orElseGet(() -> Sort.Direction.ASC),
                                configWsccListReqDTO.getSortBy().getField())),
                configWsccListReqDTO);
    };
    public ConfigWsccListResDTO getWorkSearchReqSummary(ConfigWsccListReqDTO wsccReqDTO) {
        final Tuple2<PageRequest, ConfigWsccListReqDTO> tupel2OfPageReqAndCmtDto = getPageRequest.apply(wsccReqDTO);
        Page<WsCpsConfigWsccDAO> pagedResult = null;
        final Date systemDate = commonParRepository.getCurrentDate();
        ConfigWsccListResDTO wswcListResDTO;
        if (ACTIVE.Y.name().equals(wsccReqDTO.getActive())) {
            pagedResult = wsccRepository.findActiveWsccDaos(tupel2OfPageReqAndCmtDto.getT1());
            assert pagedResult != null;
            final List<ConfigWsccListItemResDTO> configWsccListItemResDTO = pagedResult.getContent().stream()
                    .map(dao -> wsccMapper.daoToSummaryDto(dao))
                    .map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
                    .map(dto -> dto.withDeleteFlag(checkFutureDate.test(localDateToDate.apply(dto.getStartDate()), systemDate)))
                    .toList();

            wswcListResDTO = ConfigWsccListResDTO.builder()
                    .wsccSummaryList(configWsccListItemResDTO)
                    .pagination(tupel2OfPageReqAndCmtDto.getT2().getPagination().withTotalItemCount(pagedResult.getTotalElements()))
                    .build();
        } else {
            if (null == wsccReqDTO.getSortBy()) {
                wsccReqDTO = wsccReqDTO.withSortBy(GET_DEFAULT_SORT_BY.apply(Map.of(CommonConstant.DEFAULT, "wswcId")));
            }
            wswcListResDTO = wsccRepository.filterWsccBasedLookupCriteria(wsccReqDTO, systemDate);
        }

        return wswcListResDTO;
    }

    public WsCpsConfigWsccResDTO getWorkSearchReqDetails(Long wsccId) {
        final Date systemDate = commonParRepository.getCurrentDate();
        return wsccRepository.findById(wsccId)
                .map(parDao -> wsccMapper.daoToDto(parDao))
                .map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
                .orElseThrow(() -> new NotFoundException("Minimum Weekly Work Search Requirement not found for the ID: "
                        + wsccId, WSCC_ID_NOT_FOUND));
    }

    @Transactional(timeout = 30)
    public void saveWorkSearchReqDetails(ConfigWsccSaveReqDTO wsccReqDTO, String userId) {
        final WsCpsConfigWsccDAO wsccDAO = wsccRepository.findById(wsccReqDTO.getWsccId())
                .orElseThrow(() -> new NotFoundException("Minimum Weekly Work Search Requirement not found for the ID: "
                        + wsccReqDTO.getWsccId(), WSCC_ID_NOT_FOUND));
        // Validation Start
        final Date systemDate = commonParRepository.getCurrentDate();
        final HashMap<String, List<String>> errorMap = configWsccValidator.validateWsccSave(
                wsccReqDTO, wsccDAO, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Work Search Requirement Modification failed.", errorMap);
        }
        // Validation End

        if (WSCCMODIFICATIONTYPE.STARTDATE.name().equals(wsccReqDTO.getModificationType())) {
            commonSave(wsccDAO, false,
                    userId,
                    wsccReqDTO.getModificationDate(),
                    wsccReqDTO.getComments()
            );
        } else if (WSCCMODIFICATIONTYPE.CONFIGURATION.name().equals(wsccReqDTO.getModificationType())) {
        	//Expire the existing entry
            final Date expDt = wsccDAO.getWsccExpirationDt();
            wsccDAO.setWsccExpirationDt(localDateToDate.apply(wsccReqDTO
                    .getModificationDate().minusDays(1)));
            commonSave(wsccDAO, false,
                    userId,
                    null,
                    wsccReqDTO.getComments()
            );

            //Create a new entry with open expiration date
            final WsCpsConfigWsccDAO newWsccDAO = new WsCpsConfigWsccDAO();
            BeanUtils.copyProperties(wsccDAO, newWsccDAO);
            newWsccDAO.setWsccMinIcWsReq(wsccReqDTO.getInitialClaim());
            newWsccDAO.setWsccMinAcWsReq(wsccReqDTO.getAdditionalClaim());
            newWsccDAO.setWsccMinWsIncrFreq(wsccReqDTO.getIncrementFrequency());
            newWsccDAO.setWsccMinWsIncrVal(wsccReqDTO.getIncrementVal());
            newWsccDAO.setWsccExpirationDt(expDt);
            commonSave(newWsccDAO, true,
                    userId,
                    wsccReqDTO.getModificationDate(),
                    wsccReqDTO.getComments()
            );
        }
    }

    private void commonSave(WsCpsConfigWsccDAO wsccDAO,
                                        boolean create, // Inserting a new entry
                                        String userId,
                                        LocalDate effectiveDate,
                                        String usrComments
    ) {
        if (create) {
            entityManager.detach(wsccDAO);
            wsccDAO.setWsccId(null);
            wsccDAO.setWsccCreatedBy(userId);
            wsccDAO.setWsccCreatedUsing(CommonConstant.SYSTEM);

        }

        final String generatedComments = GENERATECOMMENTS.apply(new String[]{
                StringUtils.trimToEmpty(wsccDAO.getWsccComments()),
                userService.getUserName(Long.valueOf(userId)),
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
                StringUtils.trimToEmpty(usrComments)});
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        configWsccValidator.validateGeneratedComments(generatedComments, errorMap);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Work Search Requirement Modification failed.", errorMap);
        }
        wsccDAO.setWsccComments(generatedComments);

        if (effectiveDate != null) {
            wsccDAO.setWsccEffectiveDt(localDateToDate.apply(effectiveDate));
        }
        wsccDAO.setWsccLastUpdBy(userId);
        wsccDAO.setWsccLastUpdUsing(CommonConstant.SYSTEM);
        wsccRepository.save(wsccDAO);
    }

    @Autowired
    public ConfigWsccService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }

    public List<ConfigWsccListItemResDTO> getChildWsccById(ConfigWsccChildListReqDTO wsccReqDTO) {
        final Date systemDate = commonParRepository.getCurrentDate();
        List<ConfigWsccListItemResDTO> wsccListDTO = null;
        if (ConfigurationConstants.ACTIVE.ALL.name().equals(wsccReqDTO.getActive())) {
            wsccListDTO = wsccRepository.findChildListByWsccId(wsccReqDTO.getWsccId());
        } else if (ConfigurationConstants.ACTIVE.N.name().equals(wsccReqDTO.getActive())) {
            wsccListDTO = wsccRepository.findInactiveChildListByWsccId(wsccReqDTO.getWsccId());
        } else {
            wsccListDTO = wsccRepository.findActiveChildListByWsccId(wsccReqDTO.getWsccId());
        }
        return wsccListDTO.stream()
                .map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
                .map(dto -> dto.withDeleteFlag(checkFutureDate.test(localDateToDate.apply(dto.getStartDate()), systemDate)))
                .toList();
    }

    public void deleteWscc(Long wsccId, String userId) {
        final WsCpsConfigWsccDAO wsccDAO = wsccRepository.findById(wsccId)
                .orElseThrow(() -> new NotFoundException("Work Search Requirement not found for the ID:"
                        + wsccId, WSCC_ID_NOT_FOUND));
        // Validation Start
        final Date systemDate = commonParRepository.getCurrentDate();
        final HashMap<String, List<String>> errorMap = configWsccValidator.validateWsccDelete(wsccDAO, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Work Search Requirement Delete failed.", errorMap);
        }
        // Validation End
        final List<WsCpsConfigWsccDAO> wsccList = wsccRepository.findByWsccCategoryAndExpDate(
                wsccDAO.getWsccId(),
                localDateToDate.apply(dateToLocalDate.apply(wsccDAO.getWsccEffectiveDt()).minusDays(1)));
        if (wsccList != null && !wsccList.isEmpty()) {
            final WsCpsConfigWsccDAO nextFutureWscc = wsccList.get(0);
            final Date oldExpDt = nextFutureWscc.getWsccExpirationDt();
            nextFutureWscc.setWsccExpirationDt(wsccDAO.getWsccExpirationDt());
            commonSave(wsccDAO, false, userId, null,
                    DELETE_EXP_DT_AUTO_UPDATE + dateToString.apply(oldExpDt) + "'");
        }
        wsccRepository.delete(wsccDAO);
    }
}
