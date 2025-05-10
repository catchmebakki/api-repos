package com.ssi.ms.resea.service;

import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.dto.ActivityTypeAllowValueResDTO;
import com.ssi.ms.resea.dto.AllowValAlvResDTO;
import com.ssi.ms.resea.dto.AllowValueResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.ssi.ms.resea.constant.ReseaAlvEnumConstant.ALC_TYPE.RSCS_REASSIGN_REASON_CD_REASSIGN_ALL;
import static com.ssi.ms.resea.constant.ReseaConstants.REASSIGN_ALL_DECIPHER_CD;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_PRE_RESEA;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_WAITLIST_REQ;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_HOLIDAY_ALV;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_TIMEOFF_ALV;

@Slf4j
@Service
public class AlvService extends  ReseaBaseService {

    public List<AllowValAlvResDTO> getActiveAlvsByAlc(Long alcId) {
         return alvRepo.getActiveAlvsByAlc(alcId).stream()
                .map(dao -> allowValAlvMapper.daoToShortDescDto(dao)).toList();
    }

    public List<AllowValueResDTO> getActiveAlvsByAlc(String alcName) {
        List<AllowValueResDTO> allowValueResDTOS = alvRepo.getActiveAlvsByAlc(ReseaAlvEnumConstant.ALC_TYPE.getByDescription(alcName).getCode())
                .stream().map(dao -> allowValAlvMapper.dropdownDaoToDto(dao))
                //Filtering is done to remove Holiday from Appointment Lookup Dropdown,
                // If this is reused, please make modifications as necessary
                .filter(dto -> (dto.getId() != RSIC_TIMESLOT_USAGE_HOLIDAY_ALV
                        && dto.getId() != RSIC_TIMESLOT_USAGE_TIMEOFF_ALV
                        && dto.getId() != RSCS_STAGE_PRE_RESEA
                        && !RSCS_STATUS_WAITLIST_REQ.equals(dto.getId())))
                .toList();
        if (RSCS_REASSIGN_REASON_CD_REASSIGN_ALL.getDescription().equals(alcName)) {
            allowValueResDTOS = allowValueResDTOS.stream()
                    .filter(dto -> REASSIGN_ALL_DECIPHER_CD.equals(dto.getDecipherCode()))
                    .toList();
        }
        return allowValueResDTOS;
    }

    public List<ActivityTypeAllowValueResDTO> getActivityTypeAlvs() {
        return alvRepo.getActivityTypeAlvs(ReseaAlvEnumConstant.ALC_TYPE.RSCA_TYPE_CD.getCode());
    }

}
