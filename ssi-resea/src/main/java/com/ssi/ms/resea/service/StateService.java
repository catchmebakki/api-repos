package com.ssi.ms.resea.service;

import com.ssi.ms.resea.constant.ReseaConstants;
import com.ssi.ms.resea.dto.StateResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class StateService extends ReseaBaseService {

    public List<StateResDTO> getStates(Long countryCode) {
        List<StateResDTO> stateList = null;
        if (countryCode == ReseaConstants.COUNTRY_CD_USA) { //USA States
            stateList = stateRepo.getStates("N").stream()
                    .map(dao -> stateMapper.listDaoToDto(dao)).toList();
        } else if (countryCode == ReseaConstants.COUNTRY_CD_CANADA) { //Canada States
            stateList = stateRepo.getStates("Y").stream()
                    .map(dao -> stateMapper.listDaoToDto(dao)).toList();
        }
        return stateList;
    }

}
