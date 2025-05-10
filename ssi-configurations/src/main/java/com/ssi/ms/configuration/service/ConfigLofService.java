package com.ssi.ms.configuration.service;

import com.ssi.ms.configuration.database.mapper.LocalOfficeLofMapper;
import com.ssi.ms.configuration.database.repository.LocalOfficeLofRepository;
import com.ssi.ms.configuration.dto.lof.BusinessUnitListResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ConfigLofService {
    @Autowired
    private LocalOfficeLofMapper lofMapper;
    @Autowired
    private LocalOfficeLofRepository lofRepository;

    public List<BusinessUnitListResDTO> getAllBusinessUnits() {
        return lofRepository.findAllActiveBusinessUnits().stream()
                .map(dao -> lofMapper.daoToDto(dao)).toList();
    }
}
