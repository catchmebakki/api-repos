package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.dto.CcaseCraCorrespondenceCrcDTO;
import com.ssi.ms.collecticase.dto.CcaseCmaNoticesCmnDTO;
import com.ssi.ms.collecticase.dto.CreateCorrespondenceDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class CaseCorrespondenceService extends  CollecticaseBaseService {

    public List<CcaseCraCorrespondenceCrcDTO> getSendCorrespondenceForActivityRemedy(List<String> activeCorrespondenceList,
                                                                                     List<String> manualCorrespondenceList,
                                                                                     List<Long> activityCdList,
                                                                                     List<Long> remedyCdList) {
        return ccaseCraCorrespondenceCrcRepository.getSendCorrespondenceForActivityRemedy(activeCorrespondenceList,
                        manualCorrespondenceList, activityCdList, remedyCdList).stream()
                .map(dao -> ccaseCraCorrespondenceCrcMapper.dropdownDaoToDto(dao)).toList();
    }

    public List<CcaseCraCorrespondenceCrcDTO> getAllCaseCorrespondenceTypes(List<String> manualCorrespondenceList,
                                                                            List<String> activeCorrespondenceList) {
        return ccaseCraCorrespondenceCrcRepository.getAllCaseCorrespondenceTypes(manualCorrespondenceList, activeCorrespondenceList).stream()
                .map(dao -> ccaseCraCorrespondenceCrcMapper.dropdownDaoToDto(dao)).toList();
    }

    public List<CcaseCmaNoticesCmnDTO> getCaseCorrespondenceByCaseId(Long caseId,
                                                                            List<Long> correspondenceStatusList,
                                                                            Long caseCorrespondenceCrcId) {
        return ccaseCmaNoticesCmnRepository.getCaseCorrespondenceByCaseId(caseId, correspondenceStatusList, caseCorrespondenceCrcId).stream()
                .map(dao -> ccaseCmaNoticesCmnMapper.daoToDto(dao)).toList();
    }

    public Map<String, Object> createCorrespondence(CreateCorrespondenceDTO createCorrespondenceDTO) {
        return correspondenceCorRepository.createCorrespondence(createCorrespondenceDTO.getReportId(),
                createCorrespondenceDTO.getClaimId(), createCorrespondenceDTO.getEmployerId(),
                createCorrespondenceDTO.getClaimantId(), createCorrespondenceDTO.getCorCoeInd(),
                createCorrespondenceDTO.getCorForcedInd(), createCorrespondenceDTO.getCorStatusCd(),
                createCorrespondenceDTO.getCorDecId(), createCorrespondenceDTO.getCorReceipientIfk(),
                createCorrespondenceDTO.getCorReceipientCd(), createCorrespondenceDTO.getCorTimeStamp(),
                createCorrespondenceDTO.getCorCoeString());
    }
}
