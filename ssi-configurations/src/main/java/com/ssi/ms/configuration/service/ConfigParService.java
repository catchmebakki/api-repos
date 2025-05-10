package com.ssi.ms.configuration.service;


import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.common.service.UserService;
import com.ssi.ms.configuration.constant.ConfigurationConstants;
import com.ssi.ms.configuration.database.dao.ParameterParDAO;
import com.ssi.ms.configuration.database.mapper.ParameterParMapper;
import com.ssi.ms.configuration.database.repository.ParameterParConfigRepository;
import com.ssi.ms.configuration.dto.parameter.ConfigParChildListReqDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParListItemResDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParListReqDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParListResDTO;
import com.ssi.ms.configuration.dto.parameter.ConfigParSaveReqDTO;
import com.ssi.ms.configuration.dto.parameter.ParameterParResDTO;
import com.ssi.ms.configuration.validator.ConfigParValidator;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import static com.ssi.ms.configuration.constant.ConfigurationConstants.DATE_TIME_FORMATTER;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.GENERATECOMMENTS;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.PARMODIFICATIONTYPE;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.DELETE_EXP_DT_AUTO_UPDATE;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.PAR_OVERLAP_EFF_DT_AUTO_UPDATE;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.PAR_ID_NOT_FOUND;
import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.GET_DEFAULT_SORT_BY;
import static com.ssi.ms.configuration.util.ConfigUtilFunction.IS_CONFIG_ACTIVE;
import static com.ssi.ms.configuration.validator.ParValidationPredicate.isDateTypeForParameter;
import static com.ssi.ms.configuration.validator.ParValidationPredicate.isNumericTypeForParameter;
import static com.ssi.ms.configuration.validator.ParValidationPredicate.isTextTypeForParameter;
import static com.ssi.ms.platform.util.DateUtil.checkFutureDate;
import static com.ssi.ms.platform.util.DateUtil.dateToLocalDate;
import static com.ssi.ms.platform.util.DateUtil.dateToString;
import static com.ssi.ms.platform.util.DateUtil.localDateToDate;
import static com.ssi.ms.platform.util.DateUtil.localDateToString;

@Slf4j
@Service
public class ConfigParService {
    @PersistenceContext
    private final EntityManager entityManager;

    @Autowired
    private ParameterParConfigRepository parameterParRepository;
    @Autowired
    private ParameterParRepository commonParRepository;
    @Autowired
    private ParameterParMapper parameterParMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigParValidator configParValidator;

    private final Function<ConfigParListReqDTO, Tuple2<PageRequest, ConfigParListReqDTO>> getPageRequest = configParListReqDTO -> {
        if (null == configParListReqDTO.getSortBy()) {
            configParListReqDTO =
                    configParListReqDTO.withSortBy(GET_DEFAULT_SORT_BY.apply(Map.of(CommonConstant.DEFAULT, "parLongName")));
        }
        return Tuples.of(PageRequest.of(configParListReqDTO.getPagination().getPageNumber() - 1,
                        configParListReqDTO.getPagination().getPageSize(),
                        Sort.by(Sort.Direction.fromOptionalString(configParListReqDTO.getSortBy().getDirection())
                                        .orElseGet(() -> Sort.Direction.ASC),
                                configParListReqDTO.getSortBy().getField())),
                configParListReqDTO);
    };

    public ConfigParListResDTO getParametersByCategory(ConfigParListReqDTO parReqDTO) {
        final ConfigParListResDTO parListResDTO;

        final Date systemDate = commonParRepository.getCurrentDate();
        if (ConfigurationConstants.ACTIVE.Y.name().equals(parReqDTO.getActive())) {
            final Page<ParameterParDAO> pagedResult;
            if (parReqDTO.getPagination() != null) {
                final Tuple2<PageRequest, ConfigParListReqDTO> tupel2OfPageReqAndCmtDto = getPageRequest.apply(parReqDTO);
                pagedResult = parameterParRepository.findActiveByParNameParCategoryCD(parReqDTO.getParCategoryCd(),
                        StringUtils.trimToEmpty(parReqDTO.getByName()), tupel2OfPageReqAndCmtDto.getT1());
            } else {
                pagedResult = parameterParRepository.findActiveByParNameParCategoryCD(parReqDTO.getParCategoryCd(),
                        StringUtils.trimToEmpty(parReqDTO.getByName()), Pageable.unpaged());
            }

            assert pagedResult != null;
            final List<ConfigParListItemResDTO> configParListItemResDTO = pagedResult.getContent().stream()
                    .map(parameterParDao -> {
                        String numericValue = null, textValue = null, dateValue = null;
                        if (parameterParDao.getParNumericValue() != null) {
                            numericValue = String.valueOf(parameterParDao.getParNumericValue());
                        }
                        if (parameterParDao.getParAlphaValText() != null) {
                            if (isDateTypeForParameter.test(parameterParDao.getParAlphaValText())) {
                                dateValue = parameterParDao.getParAlphaValText();
                            } else {
                                textValue = parameterParDao.getParAlphaValText();
                            }
                        }
                        return parameterParMapper.daoToSummaryDto(parameterParDao)
                                .withNumericValue(numericValue).withTextValue(textValue).withDateValue(dateValue);
                    })
                    .map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
                    .toList();

            if (parReqDTO.getPagination() != null) {
                parListResDTO = ConfigParListResDTO.builder()
                        .parameterParList(configParListItemResDTO)
                        .pagination(parReqDTO.getPagination()
                                .withTotalItemCount(pagedResult.getTotalElements()))
                        .build();
            } else {
                parListResDTO = ConfigParListResDTO.builder().parameterParList(configParListItemResDTO).build();
            }
        } else {
            if (null == parReqDTO.getSortBy()) {
                parReqDTO = parReqDTO.withSortBy(GET_DEFAULT_SORT_BY.apply(Map.of(CommonConstant.DEFAULT, "parLongName")));
            }
            parListResDTO = parameterParRepository.filterParameterBasedLookupCriteria(parReqDTO, systemDate);
        }
        return parListResDTO;
    }

    public ParameterParResDTO getParDetails(Long parId) {
        final Date systemDate = commonParRepository.getCurrentDate();
        return parameterParRepository.findById(parId)
                .map(parDao -> parameterParMapper.daoToDto(parDao))
                .map(dto -> dto.withNumericType(isNumericTypeForParameter.test(dto.getNumericValue())))
                .map(dto -> dto.withTextType(isTextTypeForParameter.test(dto.getTextValue())))
                .map(dto -> dto.withDateType(isDateTypeForParameter.test(dto.getDateValue())))
                .map(dto -> dto.withModTypeChangeFlag(dto.getParExpirationDate() == null
                        || checkFutureDate.test(localDateToDate.apply(dto.getParExpirationDate()), systemDate)))
                .map(dto -> dto.withModTypeEndDateFlag(dto.getParExpirationDate() == null))
                .map(dto -> dto.withModTypeReinstateFlag(dto.getParExpirationDate() != null
                                 && parameterParRepository.findActiveParametersByShortName(dto.getParShortName(),
                                                                Long.valueOf(dto.getParCategoryCd())).isEmpty()))
                .map(dto -> dto.withOpenEndedExistFlag(Optional.ofNullable(parameterParRepository
                                .findOpenEndedParameterByShortName(dto.getParShortName(), Long.valueOf(dto.getParCategoryCd())))
                                .map(dao -> !Objects.equals(dao.getParId(), dto.getParId())).orElse(false)))
                .orElseThrow(() -> new NotFoundException("Parameter ID not found: " + parId, PAR_ID_NOT_FOUND));
    }

    @Transactional(timeout = 30)
    public void saveParDetails(ConfigParSaveReqDTO parameterParDTO, String userId) {
        final ParameterParDAO parDAO = parameterParRepository.findById(parameterParDTO.getParId())
                .orElseThrow(() -> new NotFoundException("Parameter not found for the ID:"
                        + parameterParDTO.getParId(), PAR_ID_NOT_FOUND));

        // Validation Start
        final Date systemDate = commonParRepository.getCurrentDate();
        final boolean activeExists = Optional.ofNullable(parameterParRepository
                .findActiveParameterByShortName(parDAO.getParShortName(), parDAO.getParCategoryCdAlvDAO().getAlvId())).isPresent();

        final ParameterParDAO openPar = parameterParRepository.findOpenEndedParameterByShortName(
                parDAO.getParShortName(), parDAO.getParCategoryCdAlvDAO().getAlvId());
        final boolean anotherOpenEndedExists = openPar != null && !Objects.equals(openPar.getParId(), parDAO.getParId());

        final HashMap<String, List<String>> errorMap = configParValidator.validateParSave(
                parameterParDTO, parDAO, systemDate, activeExists, anotherOpenEndedExists);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Parameter Modification failed.", errorMap);
        }
        // Validation End

        if (PARMODIFICATIONTYPE.ENDDATE.name().equals(parameterParDTO.getModificationType())) {
            enddateParameter(parDAO, parameterParDTO, userId);
        } else if (PARMODIFICATIONTYPE.REINSTATE.name().equals(parameterParDTO.getModificationType())) {
            reinstateParameter(parDAO, parameterParDTO, userId);
        } else if (PARMODIFICATIONTYPE.CHANGE.name().equals(parameterParDTO.getModificationType())) {
            changeParameter(parDAO, parameterParDTO, userId, systemDate);
        }
    }

    private void copyParameterValues(ParameterParDAO parDAO, ConfigParSaveReqDTO parameterParDTO) {
        parDAO.setParName(parameterParDTO.getName());
        parDAO.setParNumericValue(parameterParDTO.getNumericValue());
        if (parameterParDTO.getTextValue() != null) {
            parDAO.setParAlphaValText(parameterParDTO.getTextValue());
        } else if (parameterParDTO.getDateValue() != null) {
            parDAO.setParAlphaValText(localDateToString.apply(parameterParDTO.getDateValue()));
        }
        parDAO.setParExpirationDate(localDateToDate.apply(parameterParDTO.getEffectiveUntilDt()));
    }

    private void reinstateParameter(ParameterParDAO parDAO, ConfigParSaveReqDTO parameterParDTO, String userId) {
        // Upon Reinstating, we create a new parameter entry with effective date
        copyParameterValues(parDAO, parameterParDTO);
        commonSave(parDAO, true, userId,
                parameterParDTO.getModificationDt(),
                parameterParDTO.getRemarks()
        );
    }

    private void enddateParameter(ParameterParDAO parDAO, ConfigParSaveReqDTO parameterParDTO, String userId) {
        parDAO.setParExpirationDate(localDateToDate.apply(parameterParDTO.getModificationDt()));
        commonSave(parDAO, false, userId,
                null,
                parameterParDTO.getRemarks()
        );
    }

    private void changeParameter(ParameterParDAO parDAO, ConfigParSaveReqDTO parameterParDTO, String userId, Date systemDate) {
        // Upon update, we end date the existing parameter entry effective and generate new parameter
        final Date modDt = localDateToDate.apply(parameterParDTO.getModificationDt());
        final Date effUntilDt = localDateToDate.apply(parameterParDTO.getEffectiveUntilDt());

        deleteOverlapEntries(parDAO, modDt, effUntilDt);
        adjustEffDtOfOverlapEntries(parDAO, effUntilDt, userId);

        if (effUntilDt != null && (parDAO.getParExpirationDate() == null
                || effUntilDt.before(parDAO.getParExpirationDate()))) {
        	final ParameterParDAO dupParDao = new ParameterParDAO();
        	BeanUtils.copyProperties(parDAO, dupParDao);
            commonSave(dupParDao, true, userId,
                    parameterParDTO.getEffectiveUntilDt().plusDays(1),
                    parameterParDTO.getRemarks());
        }

        if (parDAO.getParEffectiveDate().equals(modDt)) {
            // No Dates are changed, update existing parameter
            copyParameterValues(parDAO, parameterParDTO);
            commonSave(parDAO, false, userId,
                    null,
                    parameterParDTO.getRemarks());
        } else {
            parDAO.setParExpirationDate(localDateToDate.apply(parameterParDTO.getModificationDt()
                    .minusDays(1)));
            commonSave(parDAO, false, userId,
                    null,
                    parameterParDTO.getRemarks());
            //Create new parameter entry with effective date
            final ParameterParDAO newParDao = new ParameterParDAO();
            BeanUtils.copyProperties(parDAO, newParDao);
            copyParameterValues(newParDao, parameterParDTO);
            newParDao.setParRemarks(null);
            commonSave(newParDao, true, userId,
                    parameterParDTO.getModificationDt(),
                    parameterParDTO.getRemarks());
        }
    }

    private void deleteOverlapEntries(ParameterParDAO parDAO, Date modDt, Date effUntil) {
        final List<ParameterParDAO> overlapDaos = parameterParRepository.findEffActiveParameterByShortName(
                parDAO.getParShortName(), parDAO.getParCategoryCdAlvDAO().getAlvId(), modDt, effUntil);
        for (final ParameterParDAO delDao: overlapDaos) {
            if (!Objects.equals(delDao.getParId(), parDAO.getParId())) {
                parameterParRepository.delete(delDao);
            }
        }
    }

    private void adjustEffDtOfOverlapEntries(ParameterParDAO parDAO, Date effUntilDt, String userId) {
        if (effUntilDt != null) {
            final ParameterParDAO effUntilDao = parameterParRepository.findEffActiveParameterByShortName(
                    parDAO.getParShortName(), parDAO.getParCategoryCdAlvDAO().getAlvId(), effUntilDt);
            if (effUntilDao != null && !Objects.equals(effUntilDao.getParId(), parDAO.getParId())) {
                commonSave(effUntilDao, false, userId,
                        dateToLocalDate.apply(effUntilDt).plusDays(1),
                        PAR_OVERLAP_EFF_DT_AUTO_UPDATE
                        + dateToString.apply(effUntilDao.getParEffectiveDate()) + "'");
            }
        }
    }
    private void commonSave(ParameterParDAO parDAO,
                            boolean create, // Inserting a new entry
                            String userId,
                            LocalDate effectiveDate,
                            String usrComments) {

        if (create) {
            entityManager.detach(parDAO);
            parDAO.setParId(null);
            parDAO.setParCreatedBy(userId);
            parDAO.setParCreatedUsing(CommonConstant.SYSTEM);
        }

        final String generatedComments = GENERATECOMMENTS.apply(new String[]{
                StringUtils.trimToEmpty(parDAO.getParRemarks()),
                userService.getUserName(Long.valueOf(userId)),
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
                StringUtils.trimToEmpty(usrComments)});
        final HashMap<String, List<String>> errorMap = new HashMap<>();
        configParValidator.validateGeneratedComments(generatedComments, errorMap);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Parameter Modification failed.", errorMap);
        }
        if (effectiveDate != null) {
            parDAO.setParEffectiveDate(localDateToDate.apply(effectiveDate));
        }
        parDAO.setParRemarks(generatedComments);
        parDAO.setParLastUpdBy(userId);
        parDAO.setParLastUpdUsing(CommonConstant.SYSTEM);
        parameterParRepository.save(parDAO);
    }

    @Autowired
    public ConfigParService(EntityManager entityManager) {
        this.entityManager = entityManager.getEntityManagerFactory().createEntityManager();
    }

    public void deleteParameter(Long parId, String userId) {
        final ParameterParDAO parDAO = parameterParRepository.findById(parId)
                .orElseThrow(() -> new NotFoundException("Parameter not found for the ID:"
                        + parId, PAR_ID_NOT_FOUND));
        // Validation Start
        final Date systemDate = commonParRepository.getCurrentDate();
        final HashMap<String, List<String>> errorMap = configParValidator.validateParDelete(parDAO, systemDate);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("Parameter Delete failed.", errorMap);
        }
        // Validation End
        final List<ParameterParDAO> pastParList = parameterParRepository.findByParShortNameParCategoryCDParExpDate(
                parDAO.getParCategoryCdAlvDAO().getAlvId(), parDAO.getParShortName(),
                localDateToDate.apply(dateToLocalDate.apply(parDAO.getParEffectiveDate()).minusDays(1)));
        if (pastParList != null && !pastParList.isEmpty()) {
            final ParameterParDAO nextFuturePar = pastParList.get(0);
            final Date oldExpDt = nextFuturePar.getParExpirationDate();
            nextFuturePar.setParExpirationDate(parDAO.getParExpirationDate());
            commonSave(parDAO, false, userId, null,
                    DELETE_EXP_DT_AUTO_UPDATE + dateToString.apply(oldExpDt) + "'");
        }
        parameterParRepository.delete(parDAO);
    }

    public List<ConfigParListItemResDTO> getChildParametersById(ConfigParChildListReqDTO parReqDTO) {
        final Date systemDate = commonParRepository.getCurrentDate();
        List<ConfigParListItemResDTO> parListDTO = null;
        if (ConfigurationConstants.ACTIVE.ALL.name().equals(parReqDTO.getActive())) {
            parListDTO = parameterParRepository.findChildListByParId(parReqDTO.getParId());
        } else if (ConfigurationConstants.ACTIVE.N.name().equals(parReqDTO.getActive())) {
            parListDTO = parameterParRepository.findInactiveChildListByParId(parReqDTO.getParId());
        } else {
            parListDTO = parameterParRepository.findActiveChildListByParId(parReqDTO.getParId());
        }
        return parListDTO.stream()
                .map(dto -> dto.withEditFlag(IS_CONFIG_ACTIVE.test(localDateToDate.apply(dto.getEndDate()), systemDate)))
                .map(dto -> dto.withDeleteFlag(checkFutureDate.test(localDateToDate.apply(dto.getStartDate()), systemDate)))
                .toList();
    }

    public List<String> getParNameListByCategory(Long parCategoryCd) {
        return parameterParRepository.getParNameListByCategory(parCategoryCd);
    }
}
