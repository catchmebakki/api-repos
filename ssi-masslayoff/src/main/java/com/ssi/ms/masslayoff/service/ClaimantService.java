package com.ssi.ms.masslayoff.service;

import com.ssi.ms.common.database.repository.ParameterParRepository;
import com.ssi.ms.constant.CommonConstant;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant.MslClaimantSourceCd;
import com.ssi.ms.masslayoff.constant.AlvMassLayoffEnumConstant.MslClaimantStatusCd;
import com.ssi.ms.masslayoff.constant.ErrorMessageConstant;
import com.ssi.ms.masslayoff.database.dao.CliamantCmtDAO;
import com.ssi.ms.masslayoff.database.dao.MassLayoffMslDAO;
import com.ssi.ms.masslayoff.database.dao.MslEmployeesMleDAO;
import com.ssi.ms.masslayoff.database.dao.MslEntryCmtMlecDAO;
import com.ssi.ms.masslayoff.database.dao.MslRefListMlrlDAO;
import com.ssi.ms.masslayoff.database.dao.MslUploadStagingMustDAO;
import com.ssi.ms.masslayoff.database.mapper.ClaimantCmtMapper;
import com.ssi.ms.masslayoff.database.mapper.ClaimantListToMslEntryCmtMlecMapper;
import com.ssi.ms.masslayoff.database.mapper.MrecClaimantToMslEntryCmtMlecMapper;
import com.ssi.ms.masslayoff.database.repository.CliamantRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MassLayoffMslRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslEmployeesMleRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslEntryCmtMlecRepository;
import com.ssi.ms.masslayoff.database.repository.msl.MslReferenceListMlrlRepository;
import com.ssi.ms.masslayoff.dto.claimant.ClaimantListIResDTO;
import com.ssi.ms.masslayoff.dto.claimant.ClaimantListItemResDTO;
import com.ssi.ms.masslayoff.dto.claimant.ClaimantListReqDTO;
import com.ssi.ms.masslayoff.dto.claimant.ClaimantReqDTO;
import com.ssi.ms.masslayoff.dto.claimant.ClaimantResDTO;
import com.ssi.ms.masslayoff.dto.claimant.MrecClaimantListReqDTO;
import com.ssi.ms.masslayoff.dto.claimant.MrecClaimantReqDTO;
import com.ssi.ms.platform.exception.custom.CustomValidationException;
import com.ssi.ms.platform.exception.custom.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static com.ssi.ms.constant.AlvCodeConstantParent.forAlvCode;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.CLAIMANT_MRLR_ALREADY_ASSOCIATED;
import static com.ssi.ms.masslayoff.constant.ErrorMessageConstant.CLAIMANT_NOT_FOUND;
import static com.ssi.ms.masslayoff.constant.PaginationAndSortByConstant.GET_DEFAULT_PAGINATION;
import static com.ssi.ms.masslayoff.constant.PaginationAndSortByConstant.GET_DEFAULT_SORTBY;
import static com.ssi.ms.masslayoff.constant.PaginationAndSortByConstant.MASSLAYOFF_CLAIMANT_LOOKUP_SORTBY_FIELDMAPPING;
import static com.ssi.ms.platform.util.UtilFunction.CompareIntegerObject;


/**
 * @author Praveenraja Paramsivam
 * ClaimantService provides services to all claimants operations.
 *
 */
@Slf4j
@Service
public class ClaimantService {

    @Autowired
    private MslEntryCmtMlecRepository mslEntryCmtMlecRepository;

    @Autowired
    private MassLayoffMslRepository mslRepository;

    @Autowired
    private MslEmployeesMleRepository mleRepository;

    @Autowired
    private ParameterParRepository parRepository;
    @Autowired
    private MslReferenceListMlrlRepository mlrlRepository;
    @Autowired
    private ClaimantListToMslEntryCmtMlecMapper claimantListToMslEntryCmtMlecMapper;
    @Autowired
    private MrecClaimantToMslEntryCmtMlecMapper mrecClaimantToMslEntryCmtMlecMapper;
    @Autowired
    private ClaimantCmtMapper claimantCmtMapper;
    @Autowired
    private CliamantRepository cliamantRepository;
    /**
     * getPageRequest function will return claimant list in the form of pagination.
     */
    private final Function<ClaimantListReqDTO, Tuple2<PageRequest, ClaimantListReqDTO>> getPageRequest = claimantListReqDTO -> {
        if (null == claimantListReqDTO.getPagination()) {
            claimantListReqDTO = claimantListReqDTO.withPagination(GET_DEFAULT_PAGINATION.get());
        }
        if (null == claimantListReqDTO.getSortBy()) {
            claimantListReqDTO =
                    claimantListReqDTO.withSortBy(GET_DEFAULT_SORTBY.apply(Map.of(CommonConstant.DEFAULT, CommonConstant.DEFAULT)));
        }
        return Tuples.of(PageRequest.of(claimantListReqDTO.getPagination().getPageNumber() - 1,
                        claimantListReqDTO.getPagination().getPageSize(),
                        Sort.by(Sort.Direction.fromOptionalString(claimantListReqDTO.getSortBy().getDirection())
                                        .orElseGet(() -> Sort.Direction.ASC),
                                MASSLAYOFF_CLAIMANT_LOOKUP_SORTBY_FIELDMAPPING.getOrDefault(claimantListReqDTO.getSortBy().getField(),
                                        "mlecSsn"))),
                claimantListReqDTO);
    };
    /**
     * Function for retrieving the MSL claimant status code based on the MSL claimant source code.
     */
    private final Function<MslClaimantSourceCd, MslClaimantStatusCd> getClaimantStatusCdForSourceCd = sourceCd ->
    CompareIntegerObject.test(MslClaimantSourceCd.STAFF_ENTERED.getCode(), sourceCd.getCode())
                || CompareIntegerObject.test(MslClaimantSourceCd.UPLOADED.getCode(), sourceCd.getCode())
                    ? MslClaimantStatusCd.CONFIRMED : MslClaimantStatusCd.PENDING;

    /**
     * Retrieve claimant details based on the provided ClaimantListReqDTO.
     *
     * @param claimantListReqDTO {@link ClaimantListReqDTO} The ClaimantListReqDTO containing criteria for retrieving claimant details.
     * @return {@link ClaimantListIResDTO} The ClaimantListIResDTO containing the response for the claimant details request.
     */
    public ClaimantListIResDTO getClaimantByIdMrlrId(ClaimantListReqDTO claimantListReqDTO) {

       final Tuple2<PageRequest, ClaimantListReqDTO>  tupel2OfPageReqAndCmtDto = getPageRequest.apply(claimantListReqDTO);
        final Page<MslEntryCmtMlecDAO> pagedResult;

        if (StringUtils.isBlank(claimantListReqDTO.getSsnNumber())) {
            final MslRefListMlrlDAO mslRefListMlrlDAO = new MslRefListMlrlDAO();
            mslRefListMlrlDAO.setMlrlId(claimantListReqDTO.getMrlrId());
            pagedResult = mslEntryCmtMlecRepository.findByMslRefListMlrlDAO(mslRefListMlrlDAO,
                    tupel2OfPageReqAndCmtDto.getT1());
        } else {
            final String ssn = claimantListReqDTO.getSsnNumber().replaceAll("-", "");
            final int ssnMinLength = 2, ssnMaxLength = 9;
            if (!NumberUtils.isParsable(ssn)
                    || claimantListReqDTO.getSsnNumber().length() < ssnMinLength
                    || claimantListReqDTO.getSsnNumber().length() > ssnMaxLength) {
                throw new CustomValidationException("Invalid SSN. Please Enter a valid full/partial SSN", Map.of("ssn",
                        List.of(ErrorMessageConstant.SSN_INVALID)));
            }
            pagedResult = mslEntryCmtMlecRepository.findByMrlIdAndSsn(claimantListReqDTO.getMrlrId(), ssn,
                    tupel2OfPageReqAndCmtDto.getT1());
        }
        final List<ClaimantListItemResDTO> claimantListItemResDTOList = pagedResult.getContent().stream()
                .map(claimantDao -> claimantListToMslEntryCmtMlecMapper.daoToDto(claimantDao))
                .map(claimantListItemResDTO -> claimantListItemResDTO
                        .withStatusCdValue(forAlvCode(MslClaimantStatusCd.class, claimantListItemResDTO.getStatusCd()).name())
                        .withSourceCdValue(forAlvCode(MslClaimantSourceCd.class, claimantListItemResDTO.getSourceCd()).name()))
                .toList();

        return ClaimantListIResDTO.builder()
                .claimantList(claimantListItemResDTOList)
                .pagination(tupel2OfPageReqAndCmtDto.getT2().getPagination().withTotalItemCount(pagedResult.getTotalElements()))
                .build();
    }

    /**
     * Confirm claimant list based on the provided MrecClaimantListReqDTO and staffId.
     *
     * @param mrecClaimantListReqDTO {@link MrecClaimantListReqDTO} The MrecClaimantListReqDTO containing the claimant list to confirm.
     * @param staffId {@link String} The staff ID associated with the confirmation.
     * @return {@link MrecClaimantListReqDTO} The updated MrecClaimantListReqDTO after confirmation.
     */
    public MrecClaimantListReqDTO confirmClaimantList(final MrecClaimantListReqDTO mrecClaimantListReqDTO, String staffId) {
        mslEntryCmtMlecRepository.updateStatusCdByIds(mrecClaimantListReqDTO.getMlecIdList(), MslClaimantStatusCd.CONFIRMED.getCode());
        return mrecClaimantListReqDTO;
    }

    /**
     * Add a claimant to the list based on the provided MrecClaimantReqDTO and staffId.
     *
     * @param mrecClaimantReqDTO {@link MrecClaimantReqDTO} The MrecClaimantReqDTO containing the claimant information to add.
     * @param staffId {@link String} The staff ID associated with the claimant addition.
     * @return {@link Long} The ID of the added claimant.
     */
    @Transactional
    public Long addClaimantToList(final MrecClaimantReqDTO mrecClaimantReqDTO, String staffId) {
        final MslEntryCmtMlecDAO dao = mrecClaimantToMslEntryCmtMlecMapper.dtoToDao(mrecClaimantReqDTO);
        return populateCommonFieldsAndSaveClaimants(dao, staffId, MslClaimantSourceCd.valueOf(mrecClaimantReqDTO.getSourceCdValue()))
                .getMlecId();
    }

    /**
     * This method will create claimants list for the upload claimants.
     * @param record {@link MslUploadStagingMustDAO} The MslEntryCmtMlecDAO record to be populated and saved.
     * @param mlrlDAO {@link MslRefListMlrlDAO} The MslRefListMlrlDAO containing the data to populate.
     * @param staffId {@link String} The staff ID associated with the claimant addition.
     */
    public void createCmtListForUploadClaimants(MslUploadStagingMustDAO record, MslRefListMlrlDAO mlrlDAO, String staffId) {
       final MslEntryCmtMlecDAO mslEntryCmtMlecDAO = new MslEntryCmtMlecDAO();
        mslEntryCmtMlecDAO.setMslRefListMlrl(mlrlDAO);
        mslEntryCmtMlecDAO.setMlecSsn(record.getMustSsn());
        mslEntryCmtMlecDAO.setMlecFirstName(record.getMustFirstName());
        mslEntryCmtMlecDAO.setMlecLastName(record.getMustLastName());
        populateCommonFieldsAndSaveClaimants(mslEntryCmtMlecDAO, staffId, MslClaimantSourceCd.UPLOADED);
    }
    /**
     * Populate common fields, associate claimants, and save an MSL entry record.
     * @param record {@link MslEntryCmtMlecDAO} The MslEntryCmtMlecDAO record to be populated and saved.
     * @param staffId {@link String} The staff ID associated with the record.
     * @param sourceCd {@link MslClaimantSourceCd} The MSL claimant source code associated with the record.
     * @return {@link MslEntryCmtMlecDAO} The populated and saved MslEntryCmtMlecDAO record.
     */
    @Transactional
    private  MslEntryCmtMlecDAO populateCommonFieldsAndSaveClaimants(MslEntryCmtMlecDAO record, String staffId, MslClaimantSourceCd sourceCd) {
        record.setMlecSourceCd(sourceCd.getCode());
        record.setMlecStatusCd(getClaimantStatusCdForSourceCd.apply(sourceCd).getCode());
        record.setMlecStatusCd(AlvMassLayoffEnumConstant.MslClaimantStatusCd.CONFIRMED.getCode());
        record.setMlecCreatedBy(staffId);
        record.setMlecCreatedUsing(CommonConstant.SYSTEM);
        record.setMlecCreatedTs(parRepository.getCurrentTimestamp());
        record.setMlecLastUpdBy(staffId);
        record.setMlecLastUpdUsing(CommonConstant.SYSTEM);
        record.setMlecLastUpdTs(parRepository.getCurrentTimestamp());
        final MslEntryCmtMlecDAO mlecDAO = mslEntryCmtMlecRepository.save(record);
        transferMleData(mlecDAO);
        return mlecDAO;
    }

    @Transactional
    private void transferMleData(final MslEntryCmtMlecDAO mlecDAO) {
        final MslRefListMlrlDAO mlrlDAO = mlrlRepository.findById(mlecDAO.getMslRefListMlrl().getMlrlId()).orElseThrow();
        if (AlvMassLayoffEnumConstant.MassLayoffStatusCd.FINAL.getCode().intValue() == mlrlDAO.getMlrlStatusCd()) {
            final MslEmployeesMleDAO mleDAO = new MslEmployeesMleDAO();
            final MassLayoffMslDAO mslDAO = mslRepository.findByMslNbr(mlrlDAO.getMlrlMslNum());
            Optional.ofNullable(mslDAO.getMslId()).ifPresent(mleDAO::setFkMslId);
            mleDAO.copyMlecData(mlecDAO);
            mleRepository.save(mleDAO);
        }
    }
    /**
     * This method will delete the claimant list based on MrecClaimantListReqDTO.
     * @param mrecClaimantListReqDTO {@link MrecClaimantListReqDTO} The MrecClaimantListReqDTO containing the claimant information to add.
     */
    public void deleteClaimantFromList(MrecClaimantListReqDTO mrecClaimantListReqDTO) {
       final var invalidMlecIds = mslEntryCmtMlecRepository.findByMslRefStatus(mrecClaimantListReqDTO.getMlecIdList(),
                MslClaimantStatusCd.CONFIRMED.getCode());
        if (CollectionUtils.isEmpty(invalidMlecIds)) {
            mslEntryCmtMlecRepository.deleteAllById(mrecClaimantListReqDTO.getMlecIdList());
        } else {
            throw new CustomValidationException("Claimant removal failed due to a validation error", Map.of("mlecIdList",
                    List.of(ErrorMessageConstant.DELETE_CLAIMANT_MLRL_FINAL)));
        }
    }


    /**
     * This method will return claimant details based on ClaimantReqDTO.
     * @param claimantReqDTO {@link ClaimantReqDTO} The ClaimantReqDTO containing the claimant information to add.
     * @return {@link ClaimantResDTO} The ClaimantResDTO containing the claimant information to add.
     */
    public ClaimantResDTO getClaimantDetail(ClaimantReqDTO claimantReqDTO) {
        List<String> associatedSsnList = null;
        if (null != claimantReqDTO.getValidateMrlrlId()) {
            associatedSsnList = mslEntryCmtMlecRepository.findSsnBySsnListAndMlrlId(List.of(claimantReqDTO.getSsn()),
                    claimantReqDTO.getValidateMrlrlId());
            if (CollectionUtils.isNotEmpty(associatedSsnList)) {
                throw new CustomValidationException(String.format("Claimant SSN %s Already associated to this MLRL_ID %d",
                        claimantReqDTO.getSsn(), claimantReqDTO.getValidateMrlrlId()), Map.of("validateMrlrlId",
                        List.of(CLAIMANT_MRLR_ALREADY_ASSOCIATED)));
            }
        }
        final CliamantCmtDAO claimantDetailsDao = cliamantRepository.findBySsn(claimantReqDTO.getSsn());
        if (null == claimantDetailsDao) {
            throw new NotFoundException("Claimant not found for the ssn:" + claimantReqDTO.getSsn(), CLAIMANT_NOT_FOUND);
        }
        return claimantCmtMapper.daoToDto(claimantDetailsDao);
    }
}
