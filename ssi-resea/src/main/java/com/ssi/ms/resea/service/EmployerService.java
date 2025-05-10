package com.ssi.ms.resea.service;

import com.ssi.ms.resea.constant.ReseaAlvEnumConstant;
import com.ssi.ms.resea.database.repository.ReseaJobReferralRsjrRepository;
import com.ssi.ms.resea.dto.AllowValAlvResDTO;
import com.ssi.ms.resea.dto.AllowValueResDTO;
import com.ssi.ms.resea.dto.EmployerReqDTO;
import com.ssi.ms.resea.dto.EmployerResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STAGE_PRE_RESEA;
import static com.ssi.ms.resea.constant.ReseaConstants.RSCS_STATUS_WAITLIST_REQ;
import static com.ssi.ms.resea.constant.ReseaConstants.RSIC_TIMESLOT_USAGE_HOLIDAY_ALV;

@Slf4j
@Service
public class EmployerService extends  ReseaBaseService {
    @Autowired
    ReseaJobReferralRsjrRepository rsjrRepository;
    public List<EmployerResDTO> getEmployerSearchByName(EmployerReqDTO employerReqDTO) {
        return rsjrRepository.getEmployerSearchByName("%"+employerReqDTO.getEmpName().toUpperCase()+"%",
                ReseaAlvEnumConstant.EAD_TYPE_CD.CORPORATE.getCode(),
                List.of(ReseaAlvEnumConstant.EMP_SOURCE_CD.UC_TAX.getCode(),
                        ReseaAlvEnumConstant.EMP_SOURCE_CD.UIM_UNIT.getCode(),
                        ReseaAlvEnumConstant.EMP_SOURCE_CD.UIN_CHARGABLE_ACC.getCode()))
                .stream().map(t -> new EmployerResDTO(t.get(0, BigDecimal.class).longValue(),
                        t.get(1, String.class) + t.get(2, String.class) + t.get(3, String.class)))
                .toList();
    }
}
