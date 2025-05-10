package com.ssi.ms.configuration.service;

import com.ssi.ms.common.service.UserService;
import com.ssi.ms.configuration.constant.ConfigurationConstants.ACTIVE;
import com.ssi.ms.configuration.database.dao.AllowCatAlcDAO;
import com.ssi.ms.configuration.database.dao.AllowValAlvDAO;
import com.ssi.ms.configuration.database.mapper.AllowValAlvMapper;
import com.ssi.ms.configuration.database.repository.AllowValAlvRepository;
import com.ssi.ms.configuration.dto.alv.AllowValAlvResDTO;
import com.ssi.ms.configuration.dto.alv.ConfigAlvListReqDTO;
import com.ssi.ms.configuration.dto.alv.ConfigAlvListResDTO;
import com.ssi.ms.configuration.dto.alv.ConfigAlvReorderItemsDTO;
import com.ssi.ms.configuration.dto.alv.ConfigAlvReorderReqDTO;
import com.ssi.ms.configuration.dto.alv.ConfigAlvSaveReqDTO;
import com.ssi.ms.configuration.validator.ConfigAlvValidator;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALC_NMI_DISPLAY_CD;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALVMODIFICATIONTYPE.CHANGE;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALVMODIFICATIONTYPE.DEACTIVATE;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALVMODIFICATIONTYPE.REACTIVATE;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.ALV_INACTIVE_SORT_ORDER_NBR;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.DATE_TIME_FORMATTER;
import static com.ssi.ms.configuration.constant.ConfigurationConstants.GENERATECOMMENTS;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.ALV_ID_NOT_FOUND;
import static com.ssi.ms.configuration.constant.ErrorMessageConstant.DISPLAY_ON_CD_INVALID;
import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.GET_DEFAULT_PAGINATION;
import static com.ssi.ms.configuration.constant.PaginationAndSortByConstant.GET_DEFAULT_SORT_BY;
import static com.ssi.ms.configuration.util.DisplayOnUtilFunction.GET_DISPLAY_ON_COMBINATION_ID;
import static com.ssi.ms.configuration.util.DisplayOnUtilFunction.getAlvDisplayOnList;

@Slf4j
@Service
public class ConfigAlvService {
    private final Function<ConfigAlvListReqDTO, Tuple2<PageRequest, ConfigAlvListReqDTO>> getPageRequest = allowValAlvDTO -> {
        if (null == allowValAlvDTO.getPagination()) {
            allowValAlvDTO = allowValAlvDTO.withPagination(GET_DEFAULT_PAGINATION.get());
        }
        if (null == allowValAlvDTO.getSortBy()) {
            allowValAlvDTO = allowValAlvDTO.withSortBy(GET_DEFAULT_SORT_BY.apply(Map.of(CommonConstant.DEFAULT, "alvSortOrderNbr")));
        }
        return Tuples.of(PageRequest.of(allowValAlvDTO.getPagination().getPageNumber() - 1,
                allowValAlvDTO.getPagination().getPageSize(),
                Sort.by(Sort.Direction.fromOptionalString(allowValAlvDTO.getSortBy().getDirection())
                                .orElseGet(() -> Sort.Direction.ASC),
                        allowValAlvDTO.getSortBy().getField())), allowValAlvDTO);
    };
    @Autowired
    private AllowValAlvRepository allowValAlvRepository;
    @Autowired
    private AllowValAlvMapper allowValAlvMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private ConfigAlvValidator configAlvValidator;

    public ConfigAlvListResDTO getAlvsByAlc(ConfigAlvListReqDTO alvReqDTO) {
        Page<AllowValAlvDAO> pagedResult = null;
        final Tuple2<PageRequest, ConfigAlvListReqDTO> tupel2OfPageReqAndCmtDto = getPageRequest.apply(alvReqDTO);
        final AllowCatAlcDAO alcDao = new AllowCatAlcDAO();
        alcDao.setAlcId(alvReqDTO.getAlcId());
        if (ACTIVE.ALL.name().equals(alvReqDTO.getActive())) {
            pagedResult = allowValAlvRepository.findByAllowCatAlcDAO(alcDao, tupel2OfPageReqAndCmtDto.getT1());
        } else {
            pagedResult = allowValAlvRepository.findByAllowCatAlcDAOAndAlvActiveInd(alcDao,
                    alvReqDTO.getActive(), tupel2OfPageReqAndCmtDto.getT1());
        }
        if (pagedResult == null) {
            throw new NotFoundException("Alvs not found for the ALC_ID:" + alvReqDTO.getAlcId(), ALV_ID_NOT_FOUND);
        }

        final List<AllowValAlvResDTO> allowValAlvDTO = pagedResult.getContent().stream()
                .map(dao -> allowValAlvMapper.daoToSummaryDto(dao))
                .toList();

        return ConfigAlvListResDTO.builder()
                .allowValAlvList(allowValAlvDTO)
                .pagination(tupel2OfPageReqAndCmtDto.getT2().getPagination().withTotalItemCount(pagedResult.getTotalElements()))
                .build();
    }

    public AllowValAlvResDTO getAlvDetails(Long alvId) {
        return allowValAlvRepository.findById(alvId)
                .map(dao -> allowValAlvMapper.daoToDto(dao))
                .map(dto -> dto.withAlvDisplayOnList(getAlvDisplayOnList.apply(dto.getAlvDisplayOn())))
                .orElseThrow(() -> new NotFoundException("Alv_ID not found: " + alvId, ALV_ID_NOT_FOUND));
    }

    @Transactional(timeout = 30)
    public void saveAlvDetails(ConfigAlvSaveReqDTO alvReqDTO, String userId) throws NotFoundException {
        final AllowValAlvDAO alvUpdateDAO = allowValAlvRepository.findById(alvReqDTO.getAlvId())
                .orElseThrow(() -> new NotFoundException("Alv not found for the ID:" + alvReqDTO.getAlvId(), ALV_ID_NOT_FOUND));
        // Validation Start
        final String generatedComments = GENERATECOMMENTS.apply(new String[]{
                StringUtils.trimToEmpty(alvUpdateDAO.getAlvChangeLog()),
                userService.getUserName(Long.valueOf(userId)),
                LocalDateTime.now().format(DATE_TIME_FORMATTER),
                alvReqDTO.getComments()});
        final HashMap<String, List<String>> errorMap = configAlvValidator.validateAlvSave(
                alvReqDTO, alvUpdateDAO, generatedComments);
        if (!errorMap.isEmpty()) {
            throw new CustomValidationException("ALV Modification failed.", errorMap);
        }
        // Validation End

        if (REACTIVATE.name().equals(alvReqDTO.getModificationType())) {
            final AllowValAlvDAO displayOnAlvDAO = allowValAlvRepository.findById(
                    GET_DISPLAY_ON_COMBINATION_ID.apply(alvReqDTO.getDisplayOnList()))
                    .orElseThrow(() -> new NotFoundException("Invalid Display On Code:" + alvReqDTO.getDisplayOnList(), DISPLAY_ON_CD_INVALID));
            alvUpdateDAO.setAlvActiveInd(ACTIVE.Y.name());
            alvUpdateDAO.setAlvShortDecTxt(alvReqDTO.getName());
            alvUpdateDAO.setAlvSpShortDescTxt(alvReqDTO.getSpanishName());
            alvUpdateDAO.setAlvDecipherCode(alvReqDTO.getAlvDecipherCd());
            alvUpdateDAO.setAlvDisplayOnAlvDao(displayOnAlvDAO);
            alvUpdateDAO.setAlvLongDescTxt(alvReqDTO.getDescription());
            alvUpdateDAO.setAlvSortOrderNbr(0L);
            commonSave(alvUpdateDAO,
                    userId,
                    generatedComments
            );
        } else if (DEACTIVATE.name().equals(alvReqDTO.getModificationType())) {
            alvUpdateDAO.setAlvActiveInd(ACTIVE.N.name());
            alvUpdateDAO.setAlvSortOrderNbr(ALV_INACTIVE_SORT_ORDER_NBR);
            commonSave(alvUpdateDAO,
                    userId,
                    generatedComments
            );
        } else if (CHANGE.name().equals(alvReqDTO.getModificationType())) {
            final AllowValAlvDAO displayOnAlvDAO = allowValAlvRepository.findById(
                            GET_DISPLAY_ON_COMBINATION_ID.apply(alvReqDTO.getDisplayOnList()))
                    .orElseThrow(() -> new NotFoundException("Invalid Display On Code:" + alvReqDTO.getDisplayOnList(), DISPLAY_ON_CD_INVALID));
            alvUpdateDAO.setAlvShortDecTxt(alvReqDTO.getName());
            alvUpdateDAO.setAlvSpShortDescTxt(alvReqDTO.getSpanishName());
            alvUpdateDAO.setAlvDecipherCode(alvReqDTO.getAlvDecipherCd());
            alvUpdateDAO.setAlvDisplayOnAlvDao(displayOnAlvDAO);
            alvUpdateDAO.setAlvLongDescTxt(alvReqDTO.getDescription());
            commonSave(alvUpdateDAO,
                    userId,
                    generatedComments
            );
        }
    }

    public void reorderAlvs(ConfigAlvReorderReqDTO reorderAlvDTO, String userId) {
        final List<AllowValAlvDAO> alvDaos = new ArrayList<>();
        final Map<Long, Long> alvIdOrderMap = reorderAlvDTO.getReorderAlvList().stream().collect(
                Collectors.toMap(ConfigAlvReorderItemsDTO::getAlvId, ConfigAlvReorderItemsDTO::getAlvSortOrderNbr));
        allowValAlvRepository.findAllById(alvIdOrderMap.keySet()).forEach(
                dao -> {
                    dao.setAlvSortOrderNbr(alvIdOrderMap.get(dao.getAlvId()));
                    alvDaos.add(dao);
                }
        );
        allowValAlvRepository.saveAll(alvDaos);
    }

    private void commonSave(AllowValAlvDAO alvDAO,
                                        String userId,
                                        String usrComments) {
        alvDAO.setAlvChangeLog(usrComments);
        allowValAlvRepository.save(alvDAO);
    }

    public List<String> getAlcAlvDecipherCdList(Long alcId) {
        return allowValAlvRepository.getAlvDecipherCdByAlcId(alcId);
    }

    public List<AllowValAlvResDTO> getDisplayOnAlvs() {
         return allowValAlvRepository.getDisplayOnList(ALC_NMI_DISPLAY_CD).stream()
                .map(dao -> allowValAlvMapper.daoToDescDto(dao)).toList();
    }
}
