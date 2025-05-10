package com.ssi.ms.resea.service;

import com.ssi.ms.resea.constant.NmiConstants;
import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.dto.NonMonIssuesNmiListReqDTO;
import com.ssi.ms.resea.dto.NonMonIssuesNmiListResDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Service
public class NmiService extends ReseaBaseService {

    public List<NonMonIssuesNmiListResDTO> getChildNmiList(Long nmiId) {
        List<NonMonIssuesNmiListResDTO> nmiList = null;
        if (nmiId == 0L) {
            nmiList = nmiRepo.getParentNmiList().stream()
                    .map(dao -> nmiMapper.listDaoToDto(dao)).toList();
        } else {
            nmiList = nmiRepo.getChildNmiList(nmiId).stream()
                    .map(dao -> nmiMapper.listDaoToDto(dao)).toList();
        }
        return nmiList;
    }

    public List<NonMonIssuesNmiListResDTO> getParentNmiListBasedOnModuleAndPage(NonMonIssuesNmiListReqDTO nmiReqDTO) {
        List<NonMonIssuesNmiListResDTO> nmiList = null;
        if (StringUtils.equals(ReseaConstants.MODULE_NAME_RESEA, nmiReqDTO.getModule())
                && StringUtils.equals(ReseaConstants.PAGE_NAME_RESEA_RESCHEDULE, nmiReqDTO.getPage())) {
            final List<Long> parentNmiId = Stream.of(NmiConstants.RESEA_CREATE_ISSUE_NMI_LIST.values())
                    .map(NmiConstants.RESEA_CREATE_ISSUE_NMI_LIST::getNmiId)
                    .collect(Collectors.toList());
            nmiList = nmiRepo.getParentNmiListBasedOnModuleAndPage(parentNmiId).stream()
                    .map(dao -> nmiMapper.listDaoToDto(dao)).toList();
        } else if (StringUtils.equals(ReseaConstants.MODULE_NAME_RESEA, nmiReqDTO.getModule())
                && StringUtils.equals(ReseaConstants.PAGE_NAME_RESEA_APPOINTMENT_DET, nmiReqDTO.getPage())) {
            final List<Long> parentNmiId = Stream.of(NmiConstants.RESEA_CREATE_ISSUE_NMI_LIST.values())
                    .map(NmiConstants.RESEA_CREATE_ISSUE_NMI_LIST::getNmiId)
                    .filter(value -> !value.equals(2L))  //Exclude Actively Seeking work for Appointment Details page
                    .collect(Collectors.toList());
            nmiList = nmiRepo.getParentNmiListBasedOnModuleAndPage(parentNmiId).stream()
                    .map(dao -> nmiMapper.listDaoToDto(dao)).toList();
        } else if (StringUtils.equals(ReseaConstants.MODULE_NAME_RESEA, nmiReqDTO.getModule())
                && StringUtils.equals(ReseaConstants.PAGE_NAME_RESEA_NEW_ACTIVITY, nmiReqDTO.getPage())) {
            final List<Long> parentNmiId = Stream.of(NmiConstants.RESEA_CREATE_ISSUE_ADD_ACTIVITY.values())
                    .map(NmiConstants.RESEA_CREATE_ISSUE_ADD_ACTIVITY::getNmiId)
                    .collect(Collectors.toList());
            nmiList = nmiRepo.getParentNmiListBasedOnModuleAndPage(parentNmiId).stream()
                    .map(dao -> nmiMapper.listDaoToDto(dao)).toList();
        }
        return nmiList;
    }


    public List<NonMonIssuesNmiListResDTO> getChildNmiListBasedOnModuleAndPage(NonMonIssuesNmiListReqDTO nmiReqDTO) {
        List<NonMonIssuesNmiListResDTO> childNmiList = null;
        if (StringUtils.equals(ReseaConstants.MODULE_NAME_RESEA, nmiReqDTO.getModule())
                && (StringUtils.equals(ReseaConstants.PAGE_NAME_RESEA_APPOINTMENT_DET, nmiReqDTO.getPage())
                    || StringUtils.equals(ReseaConstants.PAGE_NAME_RESEA_RESCHEDULE, nmiReqDTO.getPage())
                    || StringUtils.equals(ReseaConstants.PAGE_NAME_RESEA_NEW_ACTIVITY, nmiReqDTO.getPage()))) {
            childNmiList = getChildNmiList(nmiReqDTO.getIssueId());
            if (null != nmiReqDTO.getIssueId() && NmiConstants.RESEA_CREATE_ISSUE_NMI_LIST
                    .ACTIVELY_SEEKING_WORK.getNmiId().longValue() == nmiReqDTO.getIssueId()) {
                final List<Long> includeChildNmiList = Stream.of(NmiConstants.ACTIVELY_SEEKING_WORK.values())
                        .map(NmiConstants.ACTIVELY_SEEKING_WORK::getNmiId)
                        .toList();
                childNmiList = childNmiList.stream()
                        .filter(dto -> includeChildNmiList.contains(dto.getIssueId()))
                        .collect(Collectors.toList());
            }
        }
        return childNmiList;
    }
}
