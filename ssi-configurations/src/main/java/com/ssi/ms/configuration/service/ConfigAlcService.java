package com.ssi.ms.configuration.service;

import com.ssi.ms.configuration.database.mapper.AllowCatAlcMapper;
import com.ssi.ms.configuration.database.repository.AllowCatAlcRepository;
import com.ssi.ms.configuration.dto.alc.AllowCatAlcResDTO;
import com.ssi.ms.configuration.dto.alc.ConfigAlcListReqDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class ConfigAlcService {
    @Autowired
    private AllowCatAlcMapper allowCatAlcMapper;
    @Autowired
    private AllowCatAlcRepository allowCatAlcRepository;

    public List<AllowCatAlcResDTO> getAlcsByCategory(ConfigAlcListReqDTO alcReqDTO) {
        final List<AllowCatAlcResDTO> alcDaos = new ArrayList<>();
        allowCatAlcRepository.findAllById(alcReqDTO.getAlcIds()).forEach(
                dao -> alcDaos.add(allowCatAlcMapper.daoToDto(dao))
        );
        return alcDaos;
    }

    public List<AllowCatAlcResDTO> getAlcsByCategory(Long alcCategoryCd) {
        final List<AllowCatAlcResDTO> alcDaos = new ArrayList<>();
        allowCatAlcRepository.findAllByAlcCategoryCd(alcCategoryCd).forEach(
                dao -> alcDaos.add(allowCatAlcMapper.daoToDto(dao))
        );
        return alcDaos;
    }
}
