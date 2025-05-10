package com.ssi.ms.configuration.database.repository.custom;

import com.ssi.ms.configuration.dto.wswc.ConfigWswcListReqDTO;
import com.ssi.ms.configuration.dto.wswc.ConfigWswcListResDTO;

import java.util.Date;

public interface CustomWswcRepository {

	ConfigWswcListResDTO filterWswcBasedLookupCriteria(ConfigWswcListReqDTO wswcReqDTO, Date systemDate);

}
