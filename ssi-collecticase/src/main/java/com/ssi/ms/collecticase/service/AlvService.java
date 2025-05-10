package com.ssi.ms.collecticase.service;

import com.ssi.ms.collecticase.dto.AllowValAlvResDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class AlvService extends  CollecticaseBaseService {

    public List<AllowValAlvResDTO> getActiveAlvsByAlc(Long alcId) {
         return allowValAlvRepository.getActiveAlvsByAlc(alcId).stream()
                .map(dao -> allowValAlvMapper.daoToShortDescDto(dao)).toList();
    }
}
